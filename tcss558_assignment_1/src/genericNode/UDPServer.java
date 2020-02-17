package genericNode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UDPServer extends Server{

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
				
				// Execute the operation
				String response = operate(operationInfo);
				
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
			System.exit(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
