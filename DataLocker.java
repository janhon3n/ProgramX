public class DataLocker {
	int count;
	int sum;
	
	public DataLocker(){
		count = 0;
		sum = 0;
	}
	public void add(int value){
		count++;
		sum += value;
	}
	
	public int getCount(){
		return count;
	}
	public int getSum(){
		return sum;
	}
}