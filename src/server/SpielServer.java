package server;
import java.rmi.Naming;
public class SpielServer{
	public static void main(String[] args){
        if (System.getSecurityManager () == null) 
        System.setSecurityManager(new SecurityManager ()); 
		try { 

			SpielImpl obj = new SpielImpl (); 
			Naming.rebind("SpielServer", obj); 

			System.out.println ("Server ist online!"); 
		}catch (Exception e) { 

		System.out.println (e.getMessage ()); 
		e.printStackTrace (); 
		}  
	}
}