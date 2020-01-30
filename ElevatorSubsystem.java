import java.time.LocalTime;
public class ElevatorSubsystem implements Runnable{
	
	private int elevatorNum;
	private Scheduler s;
	private LocalTime time;
	
	public ElevatorSubsystem(int eleNum, Scheduler shedule) {
		this.elevatorNum = eleNum;
		this.s = shedule;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Elevator " + this.elevatorNum + " is ready to move.");
		Struct temp = s.get();
		System.out.println("Revieced: Direction =" + temp.direction + " , Floor =" + temp.floor + " ,Elevator number =" + temp.elevator + " , Time =" + temp.time);		
		Struct temp2 = new Struct(temp.direction, temp.floor, this.elevatorNum, time.now());
		s.put(temp2);
		System.out.println("Sent: Direction =" + temp2.direction + " , Floor =" + temp2.floor + " ,Elevator number =" + temp2.elevator + " , Time =" + temp2.time);		
		
	}

	
	
}
