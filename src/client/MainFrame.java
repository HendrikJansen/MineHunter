package client;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.Naming;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import common.Felder;
import common.SpielInterface;
import common.Spielfeld;
import exceptions.NameException;
import exceptions.NoPlayerException;
import exceptions.GameCompleteException;


public class MainFrame extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	Thread thread;
	GUI gtx;
	SpielInterface intf;
	String nickname;	
	Spielfeld feld;
	SpielModi modi;
	String siegDurch;
	int bombCount;
	final int anzBomb = 2;
	boolean notSearchedYet;
	boolean change;

	public MainFrame() {

		

		modi = SpielModi.SETZPHASE;

		bombCount = 0;
		change = true;
		notSearchedYet = false;

		gtx = new GUI();

		gtx.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int x = (int) (e.getX() / 30);
				int y = (int) (e.getY() / 30);
				if (modi == SpielModi.SETZPHASE) {
					setzeBomben(x, y);
					gtx.draw(feld);
					alleBombenGesetzt();
				} else {
					if (notSearchedYet && feld.canBePlaced(x, y) ) { 
						gtx.draw(aendereFeld(x, y));
						notSearchedYet = false;
						 if(change)
							 changeMyTurn();
					} else if (notSearchedYet) {
						zeigeOptionPane();
					}
				}
			}
		});

		add(gtx);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				try {
					intf.abmelden(nickname);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				System.exit(0);
			}
		});

		pack();
		setBounds(0, 0, 317, 339);

		setVisible(true);
	}

	public static void main(String args[]) {
		MainFrame m = new MainFrame();
		m.init();
		m.instanziereSpielfeld();
	}

	private void init() {
		connectionToHost();
		anmelden();
		thread = new Thread(this);
		thread.start();
	}
	
	private void connectionToHost(){
		try {
			intf = (SpielInterface) Naming.lookup("rmi://192.168.5.70/SpielServer");
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private void zeigeOptionPane() {
		JOptionPane.showMessageDialog(this, "Sie haben dieses Feld bereits ausgewaehlt!", "FeldException",
				JOptionPane.WARNING_MESSAGE);
	}

	public void instanziereSpielfeld() {
		feld = new Spielfeld();
		gtx.draw(feld);
	}

	public void run() {

		boolean running = true;
		while (running) {
			
			if (modi == SpielModi.SUCHPHASE) {				
					if(noWinnerYet()){
						JOptionPane.showMessageDialog(this, "Sie haben verloren!");
						sleep();
						modi = SpielModi.STANDBY;			
						notSearchedYet = false;						
					}else {
						if (isItMyTurn()) {
							if (!notSearchedYet) {
								JOptionPane.showMessageDialog(this, "Ihr Zug!");
							}
							notSearchedYet = true;
							sleep();
						}		
					}
							
			} else if (modi == SpielModi.SPIELBEENDET) {
				JOptionPane.showMessageDialog(this, "Sie haben gewonnen! "+siegDurch);
				modi = SpielModi.STANDBY;
			}else{
				sleep();
			}
		}
	}
	
	private String andererSpieler(){
		String opponent = "";
		try{
			opponent = intf.mitWemSpieleIch(nickname);
		}catch(NoPlayerException e){			
			andererSpieler();
			
		}
		catch(RemoteException e){
			e.printStackTrace();
		}	
		return opponent;
	}	

	private void setzeBomben(int x, int y) {
		if (feld.canBePlaced(x, y)) {
			bombCount++;
			feld.setField(x, y, Felder.BOMBE);
		} else {
			JOptionPane.showMessageDialog(this, "Sie koennen keine 2Bomben auf ein Feld legen!", "BombException",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void alleBombenGesetzt() {
		if (bombCount == anzBomb) {
			System.out.println("modi change");
			setzeSpielfeld();
			JOptionPane.showMessageDialog(this, "Ihr Gegner ist: "+andererSpieler());
			modi = SpielModi.SUCHPHASE;
			sleep();
		}	
	}
	
	private void setzeSpielfeld(){
		boolean run = true; 
		while(run) {
			try {				
				intf.setzeSpielfeld(nickname, feld);
				waitForAllSpielfelder();
				run = false;
			} catch(NoPlayerException e){
				sleep();
			}catch(RemoteException e){
				e.printStackTrace();
			}
		}
		instanziereSpielfeld();		
	}

	private void waitForAllSpielfelder() throws RemoteException {
		while (!intf.spielfeldInitialized()) {
			sleep();
		}
	}
	
	private void increaseBombCount(){
		try{
			intf.increaseBombCount(nickname);
		}catch(GameCompleteException e){
			modi = SpielModi.SPIELBEENDET;
			siegDurch = "Alle bomben gefunden!";
			change = false;
		}catch(RemoteException e){
			e.printStackTrace();
		}
	}

	private Spielfeld aendereFeld(int x, int y) {
		try {
			if (intf.searchAt(nickname, x, y)) {
				System.out.println("" + intf.searchAt(nickname, x, y));				
				feld.setField(x, y, Felder.BOMBE);
				change = false;
				increaseBombCount();
			} else {
				feld.setField(x, y, Felder.NORMAL);
				change = true;
				System.out.println("false");				
			}
		} catch (RemoteException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}		
		return feld;

	}

	private boolean isItMyTurn() {
		try {
			while (!intf.myTurn(nickname)) {
				if(noWinnerYet()){
					return false;
				}
				sleep();
			}			
		}catch (NoPlayerException e) {
			modi = SpielModi.SPIELBEENDET;
			siegDurch = "Sieg durch Aufgabe!";
			notSearchedYet = false;
			return false;
		}		
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return true;
		
	}

	private void changeMyTurn() {
		try {
			intf.changePlayerTurn(nickname);
		} catch (NoPlayerException e) {
			//sollte ich rausnehmen
		} catch(Exception e){
			e.printStackTrace();
		}

	}

	public void sleep() {
		try {
			Thread.sleep(400);
		} catch (Exception e) {
		}
	}
	
	private boolean noWinnerYet(){
		try{
			return intf.noWinnerYet(nickname);				
			
		}catch(NoPlayerException e){
			modi = SpielModi.SPIELBEENDET;
			siegDurch = "Sieg durch Aufgabe!";
			notSearchedYet = false;
			
		}catch(RemoteException e){
			e.printStackTrace();
		}
		return false;
	}

	private void anmelden() {		
			nickname = (String)JOptionPane.showInputDialog(this, "Enter a nickname!", "");
			try {			
				intf.anmelden(nickname);
				setTitle("Minehunter " + nickname );
			} catch (NameException e) {
				JOptionPane.showMessageDialog(this, "Der Name '" + nickname + "' ist bereits vergeben!", "NameException",
						JOptionPane.WARNING_MESSAGE);
				if(nickname.equals(e.getName()))
					anmelden();
			}catch(RemoteException e){
				System.out.println(e.getMessage());
				e.printStackTrace();
			}		
	}
}