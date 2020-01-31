public class Scheduler implements Runnable {
	private boolean empty = true;
	public boolean reply = true;
	private s str;
	private String getDirection(int Direction) {
		if(Direction == 1) {
			return "Up";
		}
		else {
			return "Down";
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	public synchronized void put(s s) {
		while(!empty) {
			try {
				wait();
			}
			catch(InterruptedException e) {
				return;
			}
		}
		if(Thread.currentThread().getName().contains("Floor")) {
			reply = false;
		}
		else { 
			reply = true;
		}
		str = s;
		System.out.println("Scheduler recieved message :" + s.time + " " + s.floor + " " + getDirection(s.direction)+ " "+ s.elevator);
		empty = false;
		notifyAll();
	}
	public synchronized s get(boolean rep) {
		
		while(empty || str == null || !(rep == reply)) {
			try {
				wait();
			}
			catch(InterruptedException e) {
				return null;
			}
		}
		empty = true;
		s data = str;
		str = null;
		notifyAll();
		return data;
	}
	

}