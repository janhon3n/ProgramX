
import java.util.concurrent.atomic.AtomicInteger;

/**
Contains the sum and amount of numbers for each individual summer
*/
public class DataLocker {
	
	AtomicInteger ATcount ;	//atomic counter
	AtomicInteger ATsum ;	//atomic sum, atomic values handle calculation within program (faster than regular integer)
	int count;	//regular count
	int sum;	//regular sum, these are sent back to Y
	
	
	//constructor, initializes integers with value zero
	public DataLocker(){
		ATcount = new AtomicInteger(0);
		ATsum = new AtomicInteger(0);
		count = 0;
		sum = 0;
	}
	
	//adjusts the count and sum based on given number
	public void add(int value){
		count = ATcount.incrementAndGet();
		sum = ATsum.addAndGet(value);
	}
	
	//getters
	public int getCount(){
		return count;
	}
	public int getSum(){
		return sum;
	}

}
