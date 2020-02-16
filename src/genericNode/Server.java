package genericNode;

import java.util.Hashtable;
import java.util.Set;


public abstract class Server {
	
	// Central database
	private static Hashtable<String, Integer> store = new Hashtable<String, Integer>();
	
	// Port number
	protected int portNum = -1;
	
	// Server constructor
	public Server(int portNum) {
		this.portNum = portNum;
	}
	
	// Put function
	public String put(String key, Integer value) {
		String putResponse;
		
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
		String getResponse;
		
		if (store.containsKey(key)) {
			Integer value = store.get(key);
			getResponse = "Server response:\nget key = " + key + ", get value = " + value;
		} else {
			getResponse = "Server response:\nkey = " + key + " doesn't exist";
		}
		
		return getResponse;
	}
	
	// Delete function
	public String del(String key) {
		String delResponse;
		
		if (store.containsKey(key)) {
			store.remove(key);
			delResponse = "Server response:\ndelete key = " + key;
		} else {
			delResponse = "Server response:\nkey = " + key + " doesn't exist";
		}
		
		return delResponse;
	}
	
	// Store function
	public String store() {
		String storeResponse;
		
		if (store.isEmpty()) {
			storeResponse = "Server response:\nstore is empty";
		} else {
			Set<String> keys = store.keySet();  
		    
			storeResponse = "Server response:";
			for (String key : keys) {
				storeResponse = "\nkey = " + key + ", value = " + store.get(key);
			}
		}
		
		return storeResponse;
	}
	
	// Exit function
	public String exit() {
		String exitResponse;
		
		portNum = -1;
		exitResponse = "Server response:\nSee you again!";
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
        	Integer value = Integer.parseInt(operationInfoSplit[2]);
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
