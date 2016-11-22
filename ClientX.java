import java.lang.Exception;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;

public class ClientX {
	
	static final int timeout = 5;
	static Socket s;
		
	public static void main(String[] args){
		if(args.length != 2){
			System.out.println("Arguments: address, port");
			System.exit(3);
		}
		
		try {
			ConnectionX conX = new ConnectionX(args[0], Integer.parseInt(args[1]));
			s = conX.connect();
			ClientStreamHandlerX cshx = new ClientStreamHandlerX(s);
			cshx.listen();
			cshx.close();
			
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}