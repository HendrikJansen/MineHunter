package exceptions;

public class NameException extends Exception{
	private static final long serialVersionUID = 1L;
	String name;
	public NameException(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}	
}
