package server;
//package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.SpielInterface;
import common.Spielfeld;
import exceptions.NameException;
import exceptions.NoPlayerException;
import exceptions.GameCompleteException;

//import exceptions.GameCompleteException;
//import exceptions.NameException;
//import exceptions.NoPlayerException;

public class SpielImpl extends UnicastRemoteObject implements SpielInterface {

	private Spieler[] spieler = new Spieler[2];	
	

	protected SpielImpl() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void anmelden(String name) throws RemoteException, NameException {
		if (!this.isNamevalid(name)) {
			throw new NameException(name);
		}else{
			if (spieler[0] == null) {
				spieler[0] = new Spieler(name);
				spieler[0].setTurn(true);
				System.out.println("" + spieler[0].getNickName());
			} else if (spieler[1] == null) {
				spieler[1] = new Spieler(name);
				spieler[1].setTurn(false);
				System.out.println(spieler[1].getNickName());
			}
		}

		
	}

	@Override
	public void abmelden(String name) throws RemoteException {
		for (int i = 0; i < spieler.length; i++) {
			if (spieler[i] != null) {
				if (idCheck(spieler[i], name)) {
					spieler[i] = null;					
				}
			}
		}
	}

	@Override
	public void nachrichtSenden(String name, String nachricht) throws RemoteException {
		if (idCheck(spieler[0], name)) {
			spieler[1].setMessages(nachricht);
		} else if (idCheck(spieler[1], name)) {
			spieler[0].setMessages(nachricht);
		} // end of if-else

	}

	@Override
	public String mitWemSpieleIch(String name) throws RemoteException, NoPlayerException {
		if(!allPlayersActive()){
				throw new NoPlayerException();				
		}else{
			if (idCheck(spieler[0], name)) {
				return spieler[1].getNickName();
			} else if (idCheck(spieler[1], name)) {
				return spieler[0].getNickName();
			}
		}	
		return "";
	}

	private boolean isNamevalid(String name) {
		for (int i = 0; i < spieler.length; i++) {
			if (spieler[i] != null) {
				if (idCheck(spieler[i], name)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void changePlayerTurn(String name) throws RemoteException, NoPlayerException {
		if(!allPlayersActive()){
			throw new NoPlayerException();
		}else {
			if (idCheck(spieler[0], name)) {
				spieler[0].setTurn(false);
				spieler[1].setTurn(true);
			} else if (idCheck(spieler[1], name)) {
				spieler[1].setTurn(false);
				spieler[0].setTurn(true);
			}
		}
		
	}

	private boolean idCheck(Spieler gamer, String name) {
		if(gamer != null){
			if (gamer.getNickName().equals(name)) {
				return true;
			}else
				return false;
		}else 
			return false;
		
	}

	@Override
	public String nachrichtLesen(String name) throws RemoteException {
		for (int i = 0; i < spieler.length; i++) {
			if (idCheck(spieler[i], name)) {
				return spieler[i].getMessage();
			} // end of for
		}
		return null;
	}

	@Override
	public boolean searchAt(String name, int x, int y) throws RemoteException {
		if (idCheck(spieler[0], name)) {
			return spieler[0].getSpielfeld().bomb(x, y);
		} else if (idCheck(spieler[1], name)) {
			return spieler[1].getSpielfeld().bomb(x, y);
		}
		return true;
	}
	
	@Override
	public boolean noWinnerYet(String name) throws RemoteException, NoPlayerException{
		if(!allPlayersActive()){
			throw new NoPlayerException();
		}else{
			if (idCheck(spieler[0], name)) {			
				return spieler[1].getStatus();
			} else if (idCheck(spieler[1], name)) {
				return spieler[0].getStatus();			
			}
			return false;
		}
		
	}

	@Override
	public void setzeSpielfeld(String name, Spielfeld sf) throws NoPlayerException{
		if(!allPlayersActive()){
			throw new NoPlayerException();
		}else{
			if (idCheck(spieler[0], name)) {
				System.out.println("Feld von Spieler 1 gesetzt!");
				spieler[1].setSpielfeld(sf);
			} else if (idCheck(spieler[1], name)) {
				spieler[0].setSpielfeld(sf);
				System.out.println("Feld of Spieler 2 gesetzt!");
			}
		}
		
	}

	@Override
	public boolean spielfeldInitialized() throws RemoteException {
		for (int i = 0; i < spieler.length; i++) {
			if (spieler[i].getSpielfeld() == null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean myTurn(String name) throws RemoteException, NoPlayerException{
		if(!allPlayersActive()){
			throw new NoPlayerException();
		}else {
			if (idCheck(spieler[0], name)) {
				return spieler[0].getTurn();
			} else if (idCheck(spieler[1], name)) {
				return spieler[1].getTurn();
			}
		}	
		return false;
	}
	
	
	private boolean allPlayersActive() {
		for(int i=0;i<spieler.length;i++){
			if (spieler[i] == null){
				return false;
			}
		}
		return true;
		
	}
	private boolean won(Spieler gamer){
		return gamer.allBombsFound();
		
	}
	
	@Override 
	public void increaseBombCount(String name) throws RemoteException, GameCompleteException {
		if (idCheck(spieler[0], name)) {
			spieler[0].setBombCount(spieler[0].getBombCount()+1);
			if(won(spieler[0])){
				spieler[0].setStatus(true);
				System.out.println(""+spieler[0].getStatus());
				throw new GameCompleteException();
				
			}
		} else if (idCheck(spieler[1], name)) {
			spieler[1].setBombCount(spieler[1].getBombCount()+1);
			if(won(spieler[1])){
				spieler[1].setStatus(true);
				throw new GameCompleteException();
				
			}
		}
		
	}
	
}
