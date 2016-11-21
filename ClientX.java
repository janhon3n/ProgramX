import java.lang.Exception;
import java.net.Socket;

public class ClientX {
	
	public static void main(String[] args){
		if(args.length != 2){
			System.out.println("Arguments: address, port");
			System.exit(3);
		}
		
		try {
			ConnectionX conX = new ConnectionX(args[0], Integer.parseInt(args[1]));
			Socket s = conX.connect();
		} catch(Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
			}
	}
}