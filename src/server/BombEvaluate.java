package server;


public class BombEvaluate {
	
	private int bombCount;
	public final int MAXBOMBS = 2;
	public BombEvaluate(){
		bombCount = 0;
	}
	public int getBombCount(){
		return bombCount;
	}
	
	public void setBombCount(int plusOne){
		bombCount = plusOne;
	}
	
	public boolean allBombsReached() {
		if(bombCount == MAXBOMBS){
			return true;
		}
		return false;
	}
	
}


