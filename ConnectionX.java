

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.lang.IllegalArgumentException;
import java.lang.Exception;

public class ConnectionX {	//class that handles the initial connection
	
	static final int connectionTimes = 5;  	// times the program tries to connect to the server
	static final int timeout = 5000; 		// time amount that the program waits for a response from the server
	
	private String address;	//domain for server Y
	private int port;	//port for server Y (3126)
	
	public ConnectionX(String address, int port){	//initializes the address and port given at startup
		try{
			this.setAddress(address);
			this.setPort(port);
		} catch(IllegalArgumentException iea){
			System.out.println(iea.getMessage());
			System.exit(2);
		}
	}
	
	public Socket connect() throws Exception {	//attempts to establish a UDP connection
		InetAddress targetAddr = InetAddress.getByName(address);	//loads address

		ServerSocket serverSocket = new ServerSocket(0);	//create new server socket
		serverSocket.setSoTimeout(timeout);	//with timeout 5 sec
		int tcpPort = serverSocket.getLocalPort();	//finds TCP port for our computer
		byte[] data = Integer.toString(tcpPort).getBytes();	//loads address as bytes for datagram packet
		
		DatagramSocket datagramSocket = new DatagramSocket();	//create socket to send UDP packet
		DatagramPacket datagramPacket = new DatagramPacket(data, data.length, targetAddr, port);	//creates package to send to the server
		
		for(int i = connectionTimes - 1; i >= 0; i--){	//attempts to connect to server 5 times
			datagramSocket.send(datagramPacket);	//we send the package to server Y
			System.out.println("UDP packet sent to address "+targetAddr.getHostAddress()+":"+port+" with data: "+tcpPort);
			try{
				Socket xSocket = serverSocket.accept();	//waits for Y to take contact, accepts if Y does
				datagramSocket.close();	// close the udp package sender
				System.out.println("Connection succeful! XD");
				return xSocket;	//return socket to ClientX
				
			} catch(SocketException se){
				System.err.println("Server not found, retrying "+ i +" more times...");
			} catch(SocketTimeoutException ste){
				System.err.println("Server not found, retrying "+ i +" more times...");
			}
		}
		serverSocket.close(); //if Y does't respond in 5 attempts, we give up
		datagramSocket.close();	//closing program..
		throw new Exception("Connection unsuccessful :(");
	}
	
	//Setters and getters
	public void setAddress(String address) throws IllegalArgumentException {
		if(address == null || address.equals("")){
			throw new IllegalArgumentException("Invalid address");
		}
		this.address = address;
	}
	public void setPort(int port) throws IllegalArgumentException {
		if(port < 1024 || port > 65535){
			throw new IllegalArgumentException("Invalid port number");
		}
		this.port = port;
	}
	public String getAddress(){
		return address;
	}
	public int getPort(){
		return port;
	}
}