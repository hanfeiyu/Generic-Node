package genericNode;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RMIServer extends Server {

	private String ipAddr;
	private String rmiAddr;
	
	public RMIServer(int portNum) {
		super(portNum);
		this.ipAddr = getIP();
		this.rmiAddr = "rmi://" + this.ipAddr + ":" + portNum + "/tcss558";
	}

	public String getIP() {
		InetAddress inetAddr = null;

		try {
			inetAddr = InetAddress.getLocalHost();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return inetAddr.getHostAddress().toString();
	}
	
	@Override
	public void listenAndExecute() {
		try {
			// Set up registry
			Registry registry = LocateRegistry.createRegistry(portNum);
			
			// Set up Store service
			InterfaceStoreService storeService = new StoreService(rmiAddr, registry);			

			// Bind and publish service to registry
			Naming.rebind(rmiAddr, storeService);
			
			// Otherwise exits
			//System.exit(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
