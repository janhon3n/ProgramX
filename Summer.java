import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.lang.Thread;

public class Summer extends Thread {

	private DataLocker dl;
	private ServerSocket serverSocket;

	public Summer(DataLocker dl) throws IOException{
		this.dl = dl;
		serverSocket = new ServerSocket(0);
	}
	
	public void run(){
		try{
			Socket s = serverSocket.accept();
			System.out.println("Connection established");
			InputStream iS = s.getInputStream();
			ObjectInputStream oIn = new ObjectInputStream(iS);
		
			while(true){
				if(oIn.available() > 0){
					int value = oIn.readInt();
					System.out.println("Recieved data: "+value);
					if(value == 0){
						oIn.close();
						iS.close();
						s.close();
						return;
					}
					dl.add(value);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}


	public int getPort(){
		return serverSocket.getLocalPort();
	}
}