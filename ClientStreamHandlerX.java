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
	
	
	public synchronized void listen() throws Exception{
		try{
			int clients = oIn.readInt();
			System.out.println("Recieved integer: "+clients);
			
			DataLocker[] dataLockers = new DataLocker[clients];
			Summer[] summers = new Summer[clients];
			
			for(int i = 0; i < clients; i++){
				DataLocker dl = new DataLocker();
				Summer summer = new Summer(dl);
				dataLockers[i] = dl;
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
							//close
							break;
						case 1:
							answer = sumAll(dataLockers);
							break;
						case 2:
							answer = getMax(dataLockers);
							break;
						case 3:
							answer = countAll(dataLockers);
							break;
						default:
							answer = -1;
					}
					System.out.println("Sending value to Y: "+answer);
					oOut.writeInt(answer);
					oOut.flush();
				}
			}
			// luodaan client määrä summauspalvelimia
			
			// jäädään kuuntelemaan Y:n pyyntöjä

			
		} catch(SocketTimeoutException ste){
			close();
		} catch(IOException ioe){
			close();
		}
		
		close();
	}

	public int sumAll(DataLocker[] dls){
		int sum = 0;
		for(DataLocker dl : dls){
			sum += dl.getSum();
		}
		return sum;
	}
	
	public int getMax(DataLocker[] dls){
		int max = dls[0].getSum();
		for(int i = 1; i < dls.length; i++){
			if(dls[i].getSum() > max){
				max = dls[i].getSum();
			}
		}
		return max;
	}
	
	public int countAll(DataLocker[] dls){
		int count = 0;
		for(DataLocker dl : dls){
			count += dl.getCount();
		}
		return count;
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