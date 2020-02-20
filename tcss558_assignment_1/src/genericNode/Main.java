package genericNode;

public class Main {
	
	// Show usage if any parameter is null
	public static void showUsage() {
		System.out.println("Usage: \n");
		System.out.println("Client: ");
		System.out.println("	uc/tc <address> <port> put <key> <msg> | UDP/TCP CLIENT: Put an object into store");
		System.out.println("	uc/tc <address> <port> get <key> | UDP/TCP CLIENT: Get an object from store by key");
		System.out.println("	uc/tc <address> <port> del <key> | UDP/TCP CLIENT: Delete an object from store by key");
		System.out.println("	uc/tc <address> <port> store | UDP/TCP CLIENT: Display object store");
		System.out.println("	uc/tc <address> <port> exit | UDP/TCP CLIENT: Shutdown server");
		System.out.println("	rmic <address> <port> put <key> <msg> | RMI CLIENT: Put an object into store");
		System.out.println("	rmic <address> <port> get <key> | RMI CLIENT: Get an object from store by key");
		System.out.println("	rmic <address> <port> del <key> | RMI CLIENT: Delete an object from store by key");
		System.out.println("	rmic <address> <port> store | RMI CLIENT: Display object store");
		System.out.println("	rmic <address> <port> exit | RMI CLIENT: Shutdown server\n");

		System.out.println("TCP/UDP Server: ");
		System.out.println("	us/ts <port> | UDP/TCP/TCP-and-UDP SERVER: run server on <port>.");
		System.out.println("	tus <tcpport> <udpport> | TCP-and-UDP SERVER: run servers on <tcpport> and <udpport> sharing same key-value store.");
		System.out.println("	alls <tcpport> <udpport> <rmiport> | TCP, UDP, and RMI SERVER: run servers on <tcpport> and <udpport> <rmiport> sharing same key-value store.\n");
		
		System.out.println("RMI Server: ");
		System.out.println("	rmis <port> | RMI SERVER: run server on <port>");
		
		System.exit(0);
	}
	
	// Main function
	public static void main(String[] args) {
		if (args.length < 2) {
			showUsage();
		}
		
		// Determine node type
		if (args[0].equals("ts")) {
			if (args.length != 2) {
				showUsage();
			}
			
			int portNum = Integer.parseInt(args[1]);
			TCPServer tcpServer = new TCPServer(portNum);
			
			tcpServer.listenAndExecute();
			
		} else if (args[0].equals("us")) {
			if (args.length != 2) {
				showUsage();
			}
			
			int portNum = Integer.parseInt(args[1]);
			UDPServer udpServer = new UDPServer(portNum);
			
			udpServer.listenAndExecute();
			
		} else if (args[0].equals("rmis")) {
			if (args.length != 2) {
				showUsage();
			}
			
			int portNum = Integer.parseInt(args[1]);
			RMIServer rmiServer = new RMIServer(portNum);
			
			rmiServer.listenAndExecute();
			
		} else if (args[0].equals("tus")) {
			if (args.length != 3) {
				showUsage();
			}
			
			int tcpPortNum = Integer.parseInt(args[1]);
			int udpPortNum = Integer.parseInt(args[2]);
			
			new Thread(() -> {
				TCPServer tcpServer = new TCPServer(tcpPortNum);
				tcpServer.listenAndExecute();
			}).start();

			new Thread(() -> {
				UDPServer udpServer = new UDPServer(udpPortNum);
				udpServer.listenAndExecute();
			}).start();
			
		} else if (args[0].equals("alls")) {
			if (args.length != 4) {
				showUsage();
			}
			
			int tcpPortNum = Integer.parseInt(args[1]);
			int udpPortNum = Integer.parseInt(args[2]);
			int rmiPortNum = Integer.parseInt(args[3]);
			
			new Thread(() -> {
				TCPServer tcpServer = new TCPServer(tcpPortNum);
				tcpServer.listenAndExecute();
			}).start();
			
			new Thread(()->{
				UDPServer udpServer = new UDPServer(udpPortNum);
				udpServer.listenAndExecute();
			}).start();
			
			new Thread(() -> {
				RMIServer rmiServer = new RMIServer(rmiPortNum);
				rmiServer.listenAndExecute();
			}).start();		
			
		} else {
			if (args[3].equals("put") && args.length!=6) {
				showUsage();
			}
			
			if (args[3].equals("get") && args.length!=5) {
				showUsage();
			}
			
			if (args[3].equals("del") && args.length<3 && 6<args.length) {
				showUsage();
			}
			
			if (args[3].equals("store") && args.length!=4) {
				showUsage();
			}
			
			if (args[3].equals("exit") && args.length!=4) {
				showUsage();
			}
			
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
