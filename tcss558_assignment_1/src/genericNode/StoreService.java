package genericNode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Set;

public class StoreService extends UnicastRemoteObject implements InterfaceStoreService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -853230389874314930L;
	private static boolean isShutDown = false;
	private static Hashtable<String, String> store = Server.getStore();
	
	protected StoreService() throws RemoteException {
		super();
	}
	
	public static boolean getIsShutDown () {
		return isShutDown;
	}
	
	@Override
	public String put(String key, String value) throws RemoteException {
		String putResponse;
		
		if (store.containsKey(key)) {
			putResponse = "Server response: key=" + key + " already exists, please try again with a new key";		
		} else {
			store.put(key, value);
			putResponse = "Server response: put key=" + key;
		}
		
		return putResponse;
	}

	@Override
	public String get(String key) throws RemoteException {
		String getResponse;
		
		if (store.containsKey(key)) {
			String value = store.get(key);
			getResponse = "Server response: get key=" + key + ", get value=" + value;
		} else {
			getResponse = "Server response: key=" + key + " doesn't exist";
		}
		
		return getResponse;
	}

	@Override
	public String del(String key) throws RemoteException {
		String delResponse;
		
		if (store.containsKey(key)) {
			store.remove(key);
			delResponse = "Server response: delete key=" + key;
		} else {
			delResponse = "Server response: key=" + key + " doesn't exist";
		}
		
		return delResponse;
	}

	@Override
	public String store() throws RemoteException {
		String storeResponse;
		
		if (store.isEmpty()) {
			storeResponse = "Server response: store is empty";
		} else {
			Set<String> keys = store.keySet();  
		    
			StringBuilder sb = new StringBuilder("Server response: ");
			for (String key : keys) {
				sb.append("{key=" + key + ", value=" + store.get(key) + "} ");
			}
			
			storeResponse = sb.toString();
		}
		
		return storeResponse;
	}

	@Override
	public String exit() throws RemoteException {
		String exitResponse;
		
		isShutDown = true;
		exitResponse = "Server response: See you again!";
		return exitResponse;
	}

	@Override
	public String operate(String operationInfo) throws RemoteException {
		String response = null;
		String operationInfoSplit[] = operationInfo.split(" ");
        String operation = operationInfoSplit[0];
		
		// Determine operation type
		if (operation.equals("put")) {
        	String key = operationInfoSplit[1];
        	String value = operationInfoSplit[2];
        	response = put(key, value);
        } else if (operation.equals("get")) {
        	String key = operationInfoSplit[1];
        	response = get(key);
        } else if (operation.equals("del")) {
        	String key = operationInfoSplit[1];
        	response = del(key);
        } else if (operation.equals("store")) {	        	
        	response = store();
        } else if (operation.equals("exit")) {	        	
        	response = exit();
        }
		
		return response;
	}
	
}
