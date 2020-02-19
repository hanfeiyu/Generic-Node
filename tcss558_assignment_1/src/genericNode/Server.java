package genericNode;

import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.Callable;


public abstract class Server {
	
	// Server thread
	public class ServerThread implements Callable<String> {
		
		private String operationInfo = null;
		
		public ServerThread(String operationInfo) {
			this.operationInfo = operationInfo;
		}
		
		@Override
		public String call() throws Exception {
			return operate(operationInfo);
		}
	}
	
	
	// Central database
	private static Hashtable<String, String> store = new Hashtable<String, String>();
	
	// Port number
	protected int portNum = -1;
	
	// Server constructor
	public Server(int portNum) {
		this.portNum = portNum;
	}
	
	// Expose store
	public static Hashtable<String, String> getStore() {
		return store;
	}
	
	// Put function
	public String put(String key, String value) {
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
	public String get(String key) {
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
	public String del(String key) {
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
	public String store() {
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
	
	// Exit function
	public String exit() {
		String exitResponse = null;
		
		portNum = -1;
		exitResponse = "Server response: See you again!";
		return exitResponse;
	}
	
	// Operation abstraction function 
	public String operate(String operationInfo) {
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
	
	// Listen to the client and execute the request
	public abstract void listenAndExecute();

}
