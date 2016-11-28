
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.lang.Thread;

/**
Class that handles the connection between X and Y
*/
public class ClientStreamHandlerX extends Thread{

	private final int timeout = 5000;

	private Socket s;
	private InputStream iS;
	private OutputStream oS;
	private ObjectOutputStream oOut;
	private ObjectInputStream oIn;
	
	private SharedData sharedData;
	private Summer[] summers;
	
	//constructor for stream handler
	public ClientStreamHandlerX(Socket s) throws Exception {
		try{
			s.setSoTimeout(timeout);	//timeout 5 sec
			this.s = s;	//uses the socket given by ClientX
			iS = s.getInputStream();
			oS = s.getOutputStream();
			oOut = new ObjectOutputStream(oS);	//get the input and output streams required for connection
			oIn = new ObjectInputStream(iS);
		} catch(SocketException se){
			close(true);
		} catch(IOException ioe){
			close(true);
		}
	}
	
	//Hey, listen! Hey!
	//Creates the summers first, then..
	//..reads and aswers the questions given by Y (WorkDistributor)
	public void listen() throws Exception {
		try{
			int clients = oIn.readInt();	//receives the number of summers Y wants us to create
			System.out.println("Recieved integer: "+clients);
			
			sharedData = new SharedData(clients);	//creates sharedData for summers and for streamHandler
			summers = new Summer[clients];	//creates the amount of summers required
			
			for(int i = 0; i < clients; i++){	//creates a summer with an index and pointer to sharedData
				Summer summer = new Summer(sharedData, i);
				summers[i] = summer;
				summer.start();	//Starts the summer

				int port = summer.getPort();	//summer's port, will be sent to Y
				oOut.writeInt(port);
				oOut.flush();	//sent to Y
				System.out.println("Created summer #"+i+" at port "+port);
			}
			
			while(true){	//continuing to listen to Y
				Thread.sleep(750); //While thread is sleeping, Y won't send any new questions (time for summers to calculate)
				if(oIn.available() > 0){	//if there's something in the input stream
					
					int data = oIn.readInt();	//we read it
					System.out.println("Recieved Question " + data);
					int answer = 0;
					
					switch(data){	//which question was it?
						case 0:	//if 0, we shut down our program
							shutDownSummers();
							close(false);
							return;
						case 1:	//if 1, we sum all the numbers we have been given
							answer = sharedData.sumAll().intValue();
							break;
						case 2:	//we find, which summer has the highest sum. Returns the index for that summer.
							answer = sharedData.getMax();
							break;
						case 3:	//calculates the amount of numbers we have received
							answer = sharedData.countAll().intValue();
							break;
						default:	//we return -1 if we receive any other question
							answer = -1;
					}
					oOut.writeInt(answer);
					oOut.flush();	//we send our answer to Y
				}
			}
		} catch(IOException ioe){
			ioe.printStackTrace(System.out);
		}
		close(true);
	}
	
	//Shuts down summers
	private void shutDownSummers(){
		for(Summer s : summers){
			s.shutdown();
		}
	}
	
	//closes all streams and sockets
	public void close(boolean sendError) throws IOException{
		if(sendError){	//if called by an error, we send -1 to Y
			oOut.writeInt(-1);
			oOut.flush();
		}
		oOut.close();
		oIn.close();
		oS.close();
		iS.close();
		s.close();
	}
}