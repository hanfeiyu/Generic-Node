package genericNode;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class StoreService extends UnicastRemoteObject implements InterfaceStoreService {
	
	// Server thread
	public class StoreServiceThread implements Callable<String> {
		
		private String operationInfo = null;
		
		public StoreServiceThread(String operationInfo) {
			this.operationInfo = operationInfo;
		}
		
		@Override
		public String call() throws Exception {
			return operate(operationInfo);
		}
	}
	
	// Serial version UID
	private static final long serialVersionUID = -853230389874314930L;
	
	// Connection to server central database
	private static Hashtable<String, String> store = Server.getStore();
	
	// RMI address
	private String rmiAddr = null;
	
	// RMI registry
	private Registry registry = null;
	
	
	// Store service constructor
	protected StoreService(String rmiAddr, Registry registry) throws RemoteException {
		super();
		this.rmiAddr = rmiAddr;
		this.registry = registry;
	}

	// Put function
	public String put(String key, String value) throws RemoteException {
		String putResponse = null;
		
		if (store.containsKey(key)) {
			putResponse = "Server response: key=" + key + " already exists, please try again with a new key";		
		} else {
			store.put(key, value);
			putResponse = "Server response: put key=" + key;
		}
		
		return putResponse;
	}
	
	// Get function
	public String get(String key) throws RemoteException {
		String getResponse = null;
		
		if (store.containsKey(key)) {
			String value = store.get(key);
			getResponse = "Server response: get key=" + key + ", get value=" + value;
		} else {
			getResponse = "Server response: key=" + key + " doesn't exist";
		}
		
		return getResponse;
	}
	
	// Delete function
	public String del(String key) throws RemoteException {
		String delResponse = null;
		
		if (store.containsKey(key)) {
			store.remove(key);
			delResponse = "Server response: delete key=" + key;
		} else {
			delResponse = "Server response: key=" + key + " doesn't exist";
		}
		
		return delResponse;
	}
	
	// Store function
	public String store() throws RemoteException {
		String storeResponse = null;
		
		if (store.isEmpty()) {
			storeResponse = "Server response: store is empty";
		} else {
			Set<String> keys = store.keySet();  
		    
			StringBuilder sb = new StringBuilder("Server response: ");
			for (String key : keys) {
				sb.append("{key=" + key + ", value=" + store.get(key) + "} ");
			}
			
			if (sb.toString().getBytes().length > 65000) {
				byte[] storeByte = sb.toString().getBytes();
				byte[] storeByteTrimmed = new byte[65000];
						
				for (int i=0; i<65000; i++) {		
					storeByteTrimmed[i] = storeByte[i];
				}
				
				storeResponse = storeByteTrimmed.toString();
			} else {
				storeResponse = sb.toString();
			}
		}
		
		return storeResponse;
	}
	
	// Operation abstraction function 
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
	
	// Exit function for RMI
	public String exit() throws RemoteException { 
		String exitResponse;

		try {
			Naming.unbind(rmiAddr);
			UnicastRemoteObject.unexportObject(registry, true);
			
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			e.printStackTrace();
		}
		
		exitResponse = "Server response: See you again!";
		return exitResponse;
	}

	@Override
	public String threading(String operationInfo) throws RemoteException {
		StoreServiceThread serverThread = new StoreServiceThread(operationInfo);
        FutureTask<String> futureTask = new FutureTask<String>(serverThread);
        Thread thread = new Thread(futureTask);
        thread.start();

        String rmiResponse = null;
		try {
			rmiResponse = futureTask.get();
		
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		return rmiResponse;
		
	} 
	
}
