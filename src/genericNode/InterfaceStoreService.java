package genericNode;

import java.rmi.Remote;


public interface InterfaceStoreService extends Remote {
	
	String put(String key, Integer value);
	
	String get(String key);
	
	String del(String key);
	
	String store();
	
	String exit();
	
	String operate(String operationInfo);
}
