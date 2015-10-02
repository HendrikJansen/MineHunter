package test;

import java.rmi.Naming;

import common.SpielInterface;

public class TestRMI {
	public static void main(String [] args){
		String ipADR = args[0];
		try {
			SpielInterface intf = (SpielInterface) Naming.lookup("rmi://"+ipADR+"/RechnerServer");
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
