
public class ElevatorSubsystem implements Runnable{
	private int elevatorNum;
	private Scheduler s;
	private s a;
	
	public ElevatorSubsystem(int elevatorNum, Scheduler s) {
		this.elevatorNum = elevatorNum;
		this.s = s;
	}
	
	public void run() {
		a = s.get(false);
		System.out.println("Elevator Recieved: "+  a.time + " " + a.floor + " " + a.direction + " " + a.elevator);
		s.put(a);
		
		
	}

}
