package test;
import java.rmi.*;

import common.SpielInterface;
public class TestServer{	
	SpielInterface intf;
	
	private void runTest() {
		try{
			intf = (SpielInterface) Naming.lookup ("SpielServer");			
			intf.anmelden("Tom");
			intf.anmelden("Kim");
			
			intf.abmelden("Tom");
			intf.abmelden("Kim");
			
			intf.anmelden("Niko");
			intf.anmelden("Riko");	
			
			System.out.println(intf.mitWemSpieleIch("Riko"));
			intf.nachrichtSenden("Niko","Wie geht es dir?");
			intf.nachrichtSenden("Niko","Lalalallaa!");
			
			System.out.println(intf.nachrichtLesen("Riko"));
			
			intf.abmelden("Niko");
			intf.abmelden("Riko");
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}		
	}

	public static void main(String args[]){
		TestServer test = new TestServer();
		test.runTest();
	}
}