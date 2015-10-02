package common;

import exceptions.NameException;
import exceptions.NoPlayerException;
import exceptions.GameCompleteException;

import java.rmi.Remote;
import java.rmi.RemoteException;

//import exceptions.GameCompleteException;
//import exceptions.NameException;
//import exceptions.NoPlayerException;

public interface SpielInterface extends Remote {
  /*
   * mit einem eindeutigen Namen beim Spiel anmeden
   * throws NameException, wenn der Name schon vergeben wurde
   * throws GameCompleteException, wenn bereits zwei Spieler angemeldet sind
   */
  void anmelden(String name) throws RemoteException, NameException;
  
  /*
   * mit dem Namen beim Spiel abmelden
   * throws NameException, wenn der Name schon vergeben wurde
   */
  void abmelden(String name) throws RemoteException;
  
  /*
   * Nachricht an den anderen Spieler senden
   * throws NameException, wenn der Name schon vergeben wurde
   * throws NoPlayerException, wenn noch kein zweiter Spieler angemeldet ist
   */
  void nachrichtSenden(String name, String nachricht) throws RemoteException;
  
  /*
   * Nachricht abholen
   * throws NameException, wenn der Name schon vergeben wurde
   */
  String nachrichtLesen(String name) throws RemoteException;
  
  /*
   * gibt den Namen des anderen Spielers zurück
   * throws NameException, wenn der Name schon vergeben wurde
   * throws NoPlayerException, wenn noch kein zweiter Spieler angemeldet ist
   */
  String mitWemSpieleIch(String name) throws RemoteException, NoPlayerException;   
  
  /*
  * gibt zurück ob Bombe oder daneben 
  * Server wertet dieses aus und gibt true für bombe false für daneben
  */
  boolean searchAt(String name, int x, int y) throws RemoteException;
  
  /*
  *
  *
  */
  void setzeSpielfeld(String name, Spielfeld sf) throws RemoteException, NoPlayerException;
  
  /*
  *im Client wird periodisch an den Server angefragt, bis er am Zug ist
  *returned true falls er es ist
  */
  boolean myTurn(String name) throws RemoteException, NoPlayerException;
  
  /*
  * setzt den PlayerTurn von der übergebenen ID false  
  * setzt den des anderen Spielers auf true
  */  
  void changePlayerTurn(String name) throws RemoteException, NoPlayerException;
  
  /*
  * guckt ob die Spielfelder gesetzt sind  
  * gibt an den Spieler true zurück falls ja
  */ 
  boolean spielfeldInitialized() throws RemoteException;
  
  /*
   * 
   * 
   */
  void increaseBombCount(String name) throws RemoteException, GameCompleteException;
  
   boolean noWinnerYet(String name) throws RemoteException, NoPlayerException;

}
 

