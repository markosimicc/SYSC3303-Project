/*
 * Scheduler receives information from FloorSubsystem and passes it to ElevatorSubsystem and vice versa.
 * @author Marko Simic and Michael Slokar (states)
 *
 */

public class Scheduler implements Runnable {

	public boolean reply = true;

	public SchedulerTransistions transition;

	public State st = State.WAITING;

	public RequestInfo str;

	public Scheduler() {

		System.out.println("Scheduler Initial State : " + getState(st));

	}

	@Override

	public void run() {

	}

	/**
	 * Takes in the information passed as an argument and notifies the system
	 * @param info
	 */

	public synchronized void sendInfo(RequestInfo info) {

		while (!(str == null)) {

			try {

				wait();

			}

			catch (InterruptedException e) {

				return;

			}

		}

		if (Thread.currentThread().getName().contains("Floor")) {

			reply = false;

		}

		else {

			reply = true;

		}

		str = info;

		System.out.println("Scheduler recieved message  from : " + Thread.currentThread().getName() + " " + info.time
				+ " " + info.floor + " " + info.direction + " " + info.elevator);

		st = st.next(SchedulerTransistions.RECEIVE);

		System.out.println("Now in state : " + getState(st));

		notifyAll();

	}

	/**
	 * Checks if it passing infomation or not, then returns the information to pass
	 * it to the other subsystems
	 * @param isReply
	 * @return
	 */

	public synchronized RequestInfo recieveInfo(boolean isReply) {

		while (str == null || !(isReply == reply)) {

			try {

				wait();

			}

			catch (InterruptedException e) {

				return null;

			}

		}

		RequestInfo data = str;

		str = null;

		notifyAll();

		transition = SchedulerTransistions.SEND;

		st = st.next(transition);

		System.out.println("Now in State : " + getState(st));

		return data;

	}

	/**
	 * 
	 * 
	 * 
	 * @author Michael Slokar
	 *
	 * 
	 * 
	 *         sets up the states of the scheduler and switches between states
	 * 
	 * 
	 * 
	 */

	public static enum State {

		WAITING

		{

			@Override
			public State next(SchedulerTransistions transition)

		{

				return (transition == SchedulerTransistions.RECEIVE) ? SCHEDULING : ILLEGAL; // if transition == RECEIVE then
																					// return value �SCHEDULING�Else -
																					// illegal

			}

		},

		SCHEDULING

		{

			@Override
			public State next(SchedulerTransistions transition) {

				return (transition == SchedulerTransistions.SEND) ? WAITING : ILLEGAL;

			}

		},

		ILLEGAL

		{

			@Override
			public State next(SchedulerTransistions transition) {

				return ILLEGAL;

			}

		};

		public State next(SchedulerTransistions transition) {

			return null;

		}

	}

	// returns the current state the scheduler is in

	public State getState(State st) {

		return st;

	}

}