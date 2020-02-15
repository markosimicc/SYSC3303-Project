/**

 * @author Archit Bhatia(states)

 *

 * a list of the transitions of the Schedulers states

 */
public class ElevatorSubsystem implements Runnable {
	private int elevatorNum; // Declaration of class fields
	private Scheduler scheduler;
	private RequestInfo info;
	//private static Logger LOGGER = Logger.getLogger(Scheduler.class.getName());
	public ElevState stat = ElevState.Start;
	public ElevatorTransistions trans;
	/**
	 * 
	 * Constructor for the Elevator Subsystem class
	 * 
	 * 
	 * 
	 * @param elevatorNum
	 * 
	 * 
	 * 
	 */

	public ElevatorSubsystem(int elevatorNum, Scheduler s) {
		this.elevatorNum = elevatorNum;
		this.scheduler = s;
	}

	/**
	 * 
	 * run() receives the information from the scheduler, prints out a message and
	 * 
	 * sends the information back using an instance of scheduler
	 * 
	 */

	public enum ElevState {
		Start {
			@Override
			public ElevState next(ElevatorTransistions transition) {
				return (transition == ElevatorTransistions.Door_closing) ? Door_Closed : ILLEGAL;
			}
		},

		Door_Closed {
			@Override
			public ElevState next(ElevatorTransistions transition) {
				
				return (transition == ElevatorTransistions.Start_Motor) ? Moving : ILLEGAL;
			}
		},

		Moving {
			@Override
			public ElevState next(ElevatorTransistions transition) {
				return (transition == ElevatorTransistions.Reach_Floor) ? Stopped : ILLEGAL;
			}
		},

		Stopped {
			@Override
			public ElevState next(ElevatorTransistions transition) {
				return (transition == ElevatorTransistions.Door_Opening) ? Start : ILLEGAL;
			}
		},

		ILLEGAL {
			@Override
			public ElevState next(ElevatorTransistions transition) {
				return ILLEGAL;
			}
		};
		public ElevState next(ElevatorTransistions transition) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
	public ElevState getState(ElevState st) {
		return st;
	}
	public void run() {
		while(true) {
			info = scheduler.recieveInfo(false);
			System.out.println("Elevator Recieved: " + info.time + " " + info.floor + " " + info.direction + " " + info.elevator);
			stat = stat.next(ElevatorTransistions.Door_closing);
			System.out.println(stat);
			System.out.println("Elevator " + this.elevatorNum + " is ready to move.");
			stat = stat.next(ElevatorTransistions.Start_Motor);
			System.out.println(stat);
			scheduler.sendInfo(info);
			stat = stat.next(ElevatorTransistions.Reach_Floor);
			System.out.println(stat);
			stat = stat.next(ElevatorTransistions.Door_Opening);
			
			
		}
	}
}