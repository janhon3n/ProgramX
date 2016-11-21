import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.lang.IllegalArgumentException;
import java.lang.Exception;

public class ConnectionX {
	
	static final int connectionTimes = 5;  	// times the program tryes to connect to the server
	static final int timeout = 5000; 		// time amount that the program waits for a response from the server
	
	private String address;
	private int port;
	
	public ConnectionX(String address, int port){
		try{
			this.setAddress(address);
			this.setPort(port);
		} catch(IllegalArgumentException iea){
			System.out.println(iea.getMessage());
			System.exit(2);
		}
	}
	
	public Socket connect() throws Exception {
		InetAddress targetAddr = InetAddress.getByName(address);

		ServerSocket serverSocket = new ServerSocket(0);
		serverSocket.setSoTimeout(timeout);
		int tcpPort = serverSocket.getLocalPort();
		byte[] data = Integer.toString(tcpPort).getBytes();
		
		DatagramSocket datagramSocket = new DatagramSocket();
		DatagramPacket datagramPacket = new DatagramPacket(data, data.length, targetAddr, port);
		
		for(int i = connectionTimes - 1; i >= 0; i--){
			datagramSocket.send(datagramPacket);
			System.out.println("UDP packet sent to address "+targetAddr.getHostAddress()+":"+port+" with data: "+tcpPort);
			try{
				Socket xSocket = serverSocket.accept();
				System.out.println("Connection succeful! XD");
				return xSocket;
				
			} catch(SocketException se){
				System.err.println("Server not found, retrying "+ i +" more times...");
			} catch(SocketTimeoutException ste){
				System.err.println("Server not found, retrying "+ i +" more times...");
			}
		}
		throw new Exception("Connection unsuccessful :(");
	}
	
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