package genericNode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class UDPServer extends Server {
	
	public UDPServer(int portNum) {
		super(portNum);
	}

	@Override
	public void listenAndExecute() {
		try {
			while (portNum != -1) {
				// Set up socket
				DatagramSocket socket = new DatagramSocket(portNum);
				
				// Set up packet
				byte[] requestByte = new byte[1024];
				DatagramPacket requestPacket = new DatagramPacket(requestByte, requestByte.length);
				
				// Receive request
				socket.receive(requestPacket);
				String operationInfo = new String(requestByte, 0, requestPacket.getLength());
				
				// Execute the operation in parallel
				OperateThread serverThread = new OperateThread(operationInfo);
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
				InetAddress inetAddr = requestPacket.getAddress();
				int portNum = requestPacket.getPort();
				byte[] responseByte = response.getBytes();
				DatagramPacket responsePacket = new DatagramPacket(responseByte, responseByte.length, inetAddr, portNum);
				
				socket.send(responsePacket);
				
				// Close utilities
				socket.close();
			}
			
			// Exit
			//System.exit(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
