package exceptions;

public class GameCompleteException extends Exception {	
	
	private static final long serialVersionUID = 1L;

	public GameCompleteException(){
		//do stuff
	}
	
	public String getMessage(){
		return "GameOver";
	}
}
