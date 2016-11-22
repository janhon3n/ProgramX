public class Coordinator {
	
	LinkedList<Queuable> queue = new LinkedList<Queuable>();
	
	public void addToQueue(Queuable q){
		this.queue.addLast(q);
	}
}