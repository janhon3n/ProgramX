public class SharedData {

	private DataLocker[] dataLockers;

	public SharedData(int size) {
		dataLockers = new DataLocker[size];
		for(int i = 0; i < dataLockers.length; i++){
			dataLockers[i] = new DataLocker();
		}
	}
	
	public void addToLocker(int index, int value) throws IllegalArgumentException{
		if(index < 0 || index >= dataLockers.length){
			throw new IllegalArgumentException("Locker index out of bounds");
		}
		dataLockers[index].add(value);
	}
	
	public int sumAll(){
		int sum = 0;
		for(DataLocker dl : dataLockers){
			sum += dl.getSum();
		}
		return sum;
	}
	
	public int getMax(){
		int max = dataLockers[0].getSum();
		for(int i = 1; i < dataLockers.length; i++){
			if(dataLockers[i].getSum() > max){
				max = dataLockers[i].getSum();
			}
		}
		return max;
	}
	
	public int countAll(){
		int count = 0;
		for(DataLocker dl : dataLockers){
			count += dl.getCount();
		}
		return count;
	}

	public DataLocker getDataLocker(int i){
		return dataLockers[i];
	}
}