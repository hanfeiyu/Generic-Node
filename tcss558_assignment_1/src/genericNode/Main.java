package genericNode;

public class Main {
	
	public static void main(String[] args) {
		// Show usage if any parameter is null
		if (args.length < 2) {
			System.out.println("Usage: \n");
			System.out.println("Client: ");
			System.out.println("	uc/tc <address> <port> put <key> <msg> | UDP/TCP CLIENT: Put an object into store");
			System.out.println("	uc/tc <address> <port> get <key> | UDP/TCP CLIENT: Get an object from store by key");
			System.out.println("	uc/tc <address> <port> del <key> | UDP/TCP CLIENT: Delete an object from store by key");
			System.out.println("	uc/tc <address> <port> store | UDP/TCP CLIENT: Display object store");
			System.out.println("	uc/tc <address> <port> exit | UDP/TCP CLIENT: Shutdown server");
			System.out.println("	rmic <address> put <key> <msg> | RMI CLIENT: Put an object into store");
			System.out.println("	rmic <address> get <key> | RMI CLIENT: Get an object from store by key");
			System.out.println("	rmic <address> del <key> | RMI CLIENT: Delete an object from store by key");
			System.out.println("	rmic <address> store | RMI CLIENT: Display object store");
			System.out.println("	rmic <address> exit | RMI CLIENT: Shutdown server\n");

			System.out.println("Server: ");
			System.out.println("	us/ts <port> UDP/TCP/TCP-and-UDP SERVER: run server on <port>.");
			System.out.println("	tus <tcpport> <udpport> | TCP-and-UDP SERVER: run servers on <tcpport> and <udpport> sharing same key-value store.");
			System.out.println("	alls <tcpport> <udpport> TCP, UDP, and RMI SERVER: run servers on <tcpport> and <udpport> sharing same key-value store.\n");
			
			System.out.println("RMI Server: ");
			System.out.println("	uc/tc <address> <port> put <key> <msg> | UDP/TCP CLIENT: Put an object into store");
			System.out.println("	uc/tc <address> <port> put <key> <msg> | UDP/TCP CLIENT: Put an object into store");
			
			return;
		}
		
		// Determine node type
		if (args[0].equals("ts")) {
			int portNum = Integer.parseInt(args[1]);
			TCPServer tcpServer = new TCPServer(portNum);
			
			tcpServer.listenAndExecute();
		} else if (args[0].equals("us")) {
			int portNum = Integer.parseInt(args[1]);
			UDPServer udpServer = new UDPServer(portNum);
			
			udpServer.listenAndExecute();
		} else if (args[0].equals("rmis")) {
			int portNum = Integer.parseInt(args[1]);
			RMIServer rmiServer = new RMIServer(portNum);
			
			rmiServer.listenAndExecute();
		} else {
			String ipAddr = args[1];
			int portNum = Integer.parseInt(args[2]);
			StringBuilder sb = new StringBuilder();
			for (int i=3; i<args.length-1; i++) {
				sb.append(args[i] + " ");
			}
			sb.append(args[args.length-1]);
			String operationInfo = sb.toString();
			
			if (args[0].equals("tc")) {
				TCPClient tcpClient = new TCPClient(ipAddr, portNum, operationInfo);
				
				tcpClient.requestAndListen();
			} else if (args[0].equals("uc")) {
				UDPClient udpClient = new UDPClient(ipAddr, portNum, operationInfo);
				
				udpClient.requestAndListen();
			} else if (args[0].equals("rmic")) {
				RMIClient rmiClient = new RMIClient(ipAddr, portNum, operationInfo);
				
				rmiClient.requestAndListen();
			}
		}
	}
}
