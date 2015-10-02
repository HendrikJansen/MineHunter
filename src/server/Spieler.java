package server;


import java.util.ArrayList;
import common.Spielfeld;
 
public class Spieler {
	private String nickName;
	private Spielfeld feld;
	private BombEvaluate bEvaluate;
	private ArrayList<String> messages = new ArrayList<String>();
	private boolean myturn;
	private boolean status;
	
	public Spieler(String pName) {
		feld = null;
		myturn = false;
		status = false;
		this.nickName = pName;	
		this.bEvaluate = new BombEvaluate();
	}
	
	public String getNickName() {
		return nickName;		
	}
	
	public void setNickName(String pName){
		nickName = 	pName;
	}

	public void setMessages(String pMessage){
		this.messages.add(pMessage);
	}
	
	public String getMessage(){
		String s = "";
		if(messages.isEmpty()){
			return null;
		}
		while (!messages.isEmpty()) {
			s += messages.remove(0);
			s += " ";
		}		
		return s;
	}
	
	public void setTurn(boolean turn){
		this.myturn = turn;
	}
	
	public boolean getTurn(){
		return myturn;
	}
	
	public Spielfeld getSpielfeld(){
		return feld;
	}
	
	public void setSpielfeld(Spielfeld pFeld){
		feld = pFeld;
	}
	
	public int getBombCount(){
		return bEvaluate.getBombCount();
	}
	
	public void setBombCount(int plusOne){
		bEvaluate.setBombCount(plusOne);
	}
	
	public boolean allBombsFound() {
		return bEvaluate.allBombsReached();
	}
	
	public boolean getStatus(){		
		return status;
	}
	
	public void setStatus(boolean pStatus){
		status = pStatus;
	}
}