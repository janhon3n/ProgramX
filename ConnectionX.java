import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

public class ConnectionX {
	
	static final int connectionTimes = 5;
	static final int timeout = 5000;
	
	public static void main(String[] args) throws Exception {
			if(args.length < 2){
				System.out.println("Parametrit: ip, portti");
				System.exit(0);
			}
			InetAddress targetAddr = InetAddress.getByName(args[0]);
			int targetPort = Integer.parseInt(args[1]);
			//TODO tarkista portin oikeus
			
			ServerSocket serverSocket = new ServerSocket(0);
			serverSocket.setSoTimeout(timeout);
			int port = serverSocket.getLocalPort();
			byte[] data = Integer.toString(port).getBytes();
			
			DatagramSocket datagramSocket = new DatagramSocket();
			DatagramPacket datagramPacket = new DatagramPacket(data, data.length, targetAddr, targetPort);
			
			for(int i = connectionTimes - 1; i >= 0; i--){
				datagramSocket.send(datagramPacket);
				System.out.println("UDP packet sent: "+port);
				try{
					Socket xSocket = serverSocket.accept();
					System.out.println("Connection succeful! XD");
					break;
					
				} catch(SocketException se){
					System.err.println("Server not found, retrying "+ i +" more times...");
				} catch(SocketTimeoutException ste){
					System.err.println("Server not found, retrying "+ i +" more times...");
				}
			}
		}
}