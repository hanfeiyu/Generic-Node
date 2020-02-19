package genericNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class TCPServer extends Server {

	public TCPServer(int portNum) {
		super(portNum);
	}
	
	@Override
	public void listenAndExecute() {	
		
		try {
	    	// Set up server socket
			ServerSocket serverSocket = new ServerSocket(portNum);
			
		    // Start listening
		    while (portNum != -1) {		    	
		    	// Receive request
		    	Socket socket = serverSocket.accept();
			
		    	// Read in message
				InputStreamReader is = new InputStreamReader(socket.getInputStream());
			    BufferedReader br = new BufferedReader(is);
				String operationInfo = br.readLine();
		        	        
		        // Write response
		        PrintWriter os = new PrintWriter(socket.getOutputStream());
		        
		    	// Analyze message and execute it in parallel
		        ServerThread serverThread = new ServerThread(operationInfo);
		        FutureTask<String> futureTask = new FutureTask<String>(serverThread);
		        Thread thread = new Thread(futureTask);
		        thread.start();
		        
		        String response = null;
				try {
					response = futureTask.get();
				
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				
				// Reply to client
	        	os.println(response);
	        	os.flush();
		        
		        // Close all utilities
				socket.shutdownInput();	
		    	socket.shutdownOutput();
			    is.close();
			    br.close();
			    os.close();   
			    socket.close();	    
		    }
		    
		    // Close serverSocket
		    serverSocket.close();
		    
		    // Exit
		    System.exit(0);
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
