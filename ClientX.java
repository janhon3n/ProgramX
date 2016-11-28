
import java.lang.Exception;
import java.net.Socket;

/**
* ClientX contains main method and runs the whole program
*/
public class ClientX {
	
	static Socket s;
		
	public static void main(String[] args){
		// checking for the correct amount of arguments
		if(args.length != 2){
			System.out.println("Arguments: address, port");
			System.exit(3);
		}
		
		try {
			// attempts to connect and creates the client thread if successful
			ConnectionX conX = new ConnectionX(args[0], Integer.parseInt(args[1]));
			s = conX.connect();
			ClientStreamHandlerX cshx = new ClientStreamHandlerX(s);
			cshx.listen();
			
		} catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}