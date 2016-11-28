
import java.util.concurrent.atomic.AtomicInteger;

/**
* Contains the data which is used by both classes ClientStreamHandlerX and Summer
*/
public class SharedData {

	private DataLocker[] dataLockers; // 
	AtomicInteger sumofallsum = new AtomicInteger();
	AtomicInteger countallnums = new AtomicInteger();

	public SharedData(int size) {
		dataLockers = new DataLocker[size]; //create the lockers
		for(int i = 0; i < dataLockers.length; i++){
			dataLockers[i] = new DataLocker();
		}
	}
	
	// adds an integer to a specific locker
	public void addToLocker(int index, int value) throws IllegalArgumentException{
		
		//if index out of bounds throw an exception
		if(index < 0 || index >= dataLockers.length){
			throw new IllegalArgumentException("Locker index out of bounds");
		}
		
		dataLockers[index].add(value); //add the value to the locker
		sumofallsum.addAndGet(value); //add value to samofallsum
		countallnums.incrementAndGet(); //increase countallnums by 1
		System.out.println("SharedData recieved value: "+value);
		
	}
	
	
	// finds and returns the index of the locker with the highest sum
	public int getMax(){
		int j=0;
		int max=-99999999;
		for(int i = 0; i < dataLockers.length; i++){
			if((dataLockers[i].getSum()) > max){
				max=dataLockers[i].getSum();
				j=i;
			}
		}
		return j+1;
		
	}
	
	//returns sum of all numbers 
	public AtomicInteger sumAll(){
		return sumofallsum;
	}
	
	//returns amount of all numbers
	public AtomicInteger countAll(){
		return countallnums;
	}

	//returns data of summer[i]
	public DataLocker getDataLocker(int i){
		return dataLockers[i];
	}
	
}
