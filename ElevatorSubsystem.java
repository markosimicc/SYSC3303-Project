package scheduler_state;

import static scheduler_state.Transition.Awaiting_Next_Destination;
import static scheduler_state.Transition.Door_Opening;
import static scheduler_state.Transition.Down_Request;
import static scheduler_state.Transition.Reach_Floor;
import static scheduler_state.Transition.Start_Motor;
import static scheduler_state.Transition.Up_Request;

import java.util.logging.Level;
import java.util.logging.Logger;

import scheduler_state.Scheduler_state.State;

public class ElevatorSubsystem implements Runnable {

	private int elevatorNum;		//Declaration of class fields
	private Scheduler scheduler;
	private RequestInfo info;
	
	private static Logger LOGGER = Logger.getLogger(Scheduler.class.getName());
	public static String direction = " ";
	
	/**
	 * Constructor for the Elevator Subsystem class
	 * @param elevatorNum
	 * @param s
	 */
	public ElevatorSubsystem(int elevatorNum, Scheduler s) {
		this.elevatorNum = elevatorNum;
		this.scheduler = s;
	}
	
	/**
	 * run() receives the information from the scheduler, prints out a message 
	 * and sends the information back using an instance of scheduler
	 */
	
	public static enum State {

		Start {

			@Override
			public State next(Transition transition) {
			
				if (transition == Down_Request) {
					direction = "down";
					return Door_Closed;
				}
				else if (transition == Up_Request) {
					direction = "up";
					return Door_Closed;
				}				
				else {
					return ILLEGAL;
				}
			}

		},

		Door_Closed {

			@Override
			public State next(Transition transition) {

				if ((transition == Start_Motor)) {
					return Moving;
				} else {
					return ILLEGAL;
				}
			}
		},

		Moving {

			@Override
			public State next(Transition transition) {
				
				if (transition == Reach_Floor) {
					return Stopped;
				}
				return ILLEGAL;

			}

		},
		
		Stopped {
			@Override
			public State next(Transition transition) {
				if (transition == Door_Opening) {
					return Door_Open;
				} 
				return ILLEGAL;

			}		
		},

		Door_Open {

			@Override
			public State next(Transition transition) {
				
				if (transition == Awaiting_Next_Destination) {
					return Start;
				}
				return ILLEGAL;
			}

		},

		ILLEGAL {

			@Override
			public State next(Transition transition) {

				return ILLEGAL;

			}

		};

		public State next(Transition transition) {

			return null;

		}

	}

	public static void main(String[] args) {

		State finish = run(State.Start, Down_Request, Up_Request, Start_Motor, Reach_Floor, Door_Opening,
				Awaiting_Next_Destination);

	}

	public static State run(State start, Transition... transitions) {
		
		State state = start;
		LOGGER.log(Level.INFO, "start state: {0}", start);

		for (Transition transition : transitions) {
			state = state.next(transition);
			LOGGER.log(Level.INFO, "current state: {0}", state);
		}

		LOGGER.info("finished");

		return state;

	}
	
	public void run() {
		info = scheduler.recieveInfo(false);
		System.out.println("Elevator Recieved: "+  info.time + " " + info.floor + " " + info.direction + " " + info.elevator);
		System.out.println("Elevator " + this.elevatorNum + " is ready to move.");
		scheduler.sendInfo(info);
		
		
			
	}

}
