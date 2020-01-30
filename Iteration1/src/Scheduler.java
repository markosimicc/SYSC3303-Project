
public class Scheduler implements Runnable {
	private boolean empty = true;
	private boolean reply;
	private Struct str;
	private String getDirection(int Direction) {
		if(Direction == 1) {
			return "UP";
		}
		else {
			return "DOWN";
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	public synchronized void put(Struct s) {
		while(!empty) {
			try {
				wait();
			}
			catch(InterruptedException e) {
				return;
			}
		}
		str = s;
		System.out.println("Scheduler recieved message :" + Struct.time + " " + Struct.floor + getDirection(Struct.direction)+ " "+ Struct.elevator);
		empty = true;
		notifyAll();
	}
	public synchronized Struct get() {
		while(empty && str != null) {
			try {
				wait();
			}
			catch(InterruptedException e) {
				return null;
			}
		}
		
		empty = true;
		notifyAll();
		return str;
		
	}
	

}
