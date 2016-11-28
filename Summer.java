import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.lang.Thread;

/**
* Class that recieves integer data from Y and sums them in SharedData
*/
public class Summer extends Thread{

	private ServerSocket serverSocket;
	private final int index;
	private SharedData sharedData;
	private boolean shutdown = false;

	public Summer(SharedData sd, int index) throws IOException{
		this.sharedData = sd;
		this.index = index;
		serverSocket = new ServerSocket(0); // create the serversocket at free port
	}
	
	// create socket and listen for data
	public void run(){
		try{
			Socket s = serverSocket.accept(); //wait for Y to connect
			System.out.println("Connection established");
			InputStream iS = s.getInputStream(); // get input streams
			ObjectInputStream oIn = new ObjectInputStream(iS);
			
			while(true){
				if(shutdown){ // if shutdown flag is set, close streams and sockets and stop running
					oIn.close();
					iS.close();
					s.close();
					return;
				}
				
				// if data available, read and store it
				if(oIn.available() > 0){
						
					int value = oIn.readInt(); // read the integer
					System.out.println("Summer " + index + " Recieved data: "+value);
					if(value == 0){ // if Y sent 0, close streams and socket
						oIn.close();
						iS.close();
						s.close();
						return;
					}else{
						// if Y sent something else, store it in sharedData
						sharedData.addToLocker(index, value);
					}
				}
				
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	//returns port of summer
	public int getPort(){
		return serverSocket.getLocalPort();
	}
	
	//shutdown, sets the shutdown flag to be true
	public void shutdown(){
		this.shutdown = true;
	}
}
