package genericNode;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface InterfaceStoreService extends Remote {
	
	public String put(String key, String value) throws RemoteException;
	
	public String get(String key) throws RemoteException;
	
	public String del(String key) throws RemoteException;
	
	public String store() throws RemoteException;
	
	public String exit() throws RemoteException;
	
	public String operate(String operationInfo) throws RemoteException;
}
