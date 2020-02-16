package genericNode;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class RMIServer extends Server {

	private String rmiAddr;
	
	public RMIServer(int portNum) {
		super(portNum);
		this.rmiAddr = "rmi://localhost:" + portNum + "/tcss558";
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
				// Check isShutDown status every 0.02 second
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				isShutDown = StoreService.getIsShutDown();
			}
			
			// Otherwise exits
			Naming.unbind(rmiAddr);
			UnicastRemoteObject.unexportObject(registry, true);
			
		} catch (IOException | NotBoundException e) {
			e.printStackTrace();
		}
	}
}
