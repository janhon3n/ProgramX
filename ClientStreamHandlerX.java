import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ClientStreamHandlerX {

	private final int timeout = 5000;

	private Socket s;
	private InputStream iS;
	private OutputStream oS;
	private ObjectOutputStream oOut;
	private ObjectInputStream oIn;
	
	private SharedData sharedData;
	private Summer[] summers;
	

	public ClientStreamHandlerX(Socket s) throws Exception {
		try{
			s.setSoTimeout(timeout);
			this.s = s;
			iS = s.getInputStream();
			oS = s.getOutputStream();
			oOut = new ObjectOutputStream(oS);
			oIn = new ObjectInputStream(iS);
		} catch(SocketException se){
			close();
		} catch(IOException ioe){
			close();
		}
	}
	
	
	public void listen() throws Exception {
		try{
			int clients = oIn.readInt();
			System.out.println("Recieved integer: "+clients);
			
			sharedData = new SharedData(clients);
			summers = new Summer[clients];
			
			for(int i = 0; i < clients; i++){
				Summer summer = new Summer(sharedData, i);
				summer.setPriority(Thread.currentThread().getPriority() +1);
				summers[i] = summer;
				summer.start();

				int port = summer.getPort();
				oOut.writeInt(port);
				oOut.flush();
				System.out.println("Created summer #"+i+" at port "+port);
			}
			
			while(true){
				if(oIn.available() > 0){
					int data = oIn.readInt();
					System.out.println("Data from Y: "+data);
					
					int answer = 0;
					switch(data){
						case 0:
							//TODO close
							break;
						case 1:
							answer = sharedData.sumAll();
							break;
						case 2:
							answer = sharedData.getMax();
							break;
						case 3:
							answer = sharedData.countAll();
							break;
						default:
							answer = -1;
					}
					System.out.println("Sending value to Y: " + answer);
					oOut.writeInt(answer);
					oOut.flush();
				}
			}
		} catch(IOException ioe){
			ioe.printStackTrace(System.out);
		}
	}

	public void close() throws IOException{
		oOut.writeInt(-1);
		oOut.flush();
		oOut.close();
		oIn.close();
		oS.close();
		iS.close();
		s.close();
	}
}