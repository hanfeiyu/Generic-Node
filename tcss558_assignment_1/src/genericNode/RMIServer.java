package genericNode;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


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
			// Set up Store service
			InterfaceStoreService storeService = new StoreService();
			
			// Set up registry
			Registry registry = LocateRegistry.createRegistry(portNum);
			
			// Bind and publish service to registry
			Naming.rebind(rmiAddr, storeService);
			
			// Remain connected if client doesn't want to exit
			boolean isShutDown = false;
			while (!isShutDown) {
				// Check isShutDown status every 0.01 second
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				isShutDown = StoreService.getIsShutDown();
			}
			
			// Otherwise exits
			Naming.unbind(rmiAddr);
			UnicastRemoteObject.unexportObject(registry, true);
			System.exit(0);
			
		} catch (IOException | NotBoundException e) {
			e.printStackTrace();
		}
	}
}
