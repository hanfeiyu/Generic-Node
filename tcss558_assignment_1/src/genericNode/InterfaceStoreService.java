package genericNode;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface InterfaceStoreService extends Remote {
	
	String put(String key, String value) throws RemoteException;
	
	String get(String key) throws RemoteException;
	
	String del(String key) throws RemoteException;
	
	String store() throws RemoteException;
	
	String exit() throws RemoteException;
	
	String operate(String operationInfo) throws RemoteException;
}
