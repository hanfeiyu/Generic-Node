package genericNode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class UDPClient extends Client {

	public UDPClient(String ipAddr, int portNum, String operationInfo) {
		super(ipAddr, portNum, operationInfo);
	}

	@Override
	public void requestAndListen() {
		try {
			// Send request
			InetAddress inetAddr = InetAddress.getByName(ipAddr);
			byte[] request = operationInfo.getBytes();
			DatagramPacket requestPacket = new DatagramPacket(request, request.length, inetAddr, portNum);
			DatagramSocket socket = new DatagramSocket();
			
			System.out.println("Send a request.");
			socket.send(requestPacket);
			
			// Receive response
			byte[] responseByte = new byte[1024];
			DatagramPacket responsePacket = new DatagramPacket(responseByte, responseByte.length);
			System.out.println("Start listening...");
			socket.receive(responsePacket);
			System.out.println("Receive a packet." + "\ttime=" + new Date().getTime());
			
			// Read response
			String response = new String(responseByte, 0, responsePacket.getLength());
			System.out.println(response + "\ttime=" + new Date().getTime());
			
			// Close utilities
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
