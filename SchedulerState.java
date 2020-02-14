/**
 * Scheduler receives information from FloorSubsystem and passes it to ElevatorSubsystem and vice versa.
 * @author Marko Simic
 *
 */
public class Scheduler implements Runnable {
	public boolean reply = true;
	public RequestInfo str;
	@Override
	public void run() {
		// Nothing to be done in run()
		
	}
	
	/**
	 * Takes in the information passed as an argument and notifies the system
	 * @param info
	 */
	public synchronized void sendInfo(RequestInfo info) {
		while(!(str == null)) {
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
		str = info;
		System.out.println("Scheduler recieved message  from : " + Thread.currentThread().getName() + " " + info.time + " " + info.floor + " " + info.direction + " "+ info.elevator);
		notifyAll();
	}
	
	/**
	 * Checks if it passing information or not, then returns the information to pass it to the other subsystems
	 * @param isReply
	 * @return
	 */
	public synchronized RequestInfo recieveInfo(boolean isReply) {
		
		while(str == null || !(isReply == reply)) {
			try {
				wait();
			}
			catch(InterruptedException e) {
				return null;
			}
		}
		RequestInfo data = str;
		str = null;
		notifyAll();
		return data;
	}
	
	 public static enum State// enum is a special class that represents a group of constants, methods that use those constants
	 	WAITING
	 	{
		 @Override public State next(Transition transition);//this will override abstract method state next declared later;// Transition another enumdeclared later
		 {
			 return (transition == Recieve) ? Process : ILLEGAL; //? – if transition == create then return value “RUNNING”Else - illegal
		 }
	 	},
	 	PROCESS
	 	{
	 		@Override public State next(Transition transition)
	 		{
	 			return (transition == Send) ? waiting : ILLEGAL;
	 		}
	 	},
	 	RECIEVE
	 	{
	 		@Override public State next(Transition transition)
	 		{
	 			return ILLEGAL;
	 		}
	 	},
	 	SEND
	 	{
	 		@Override public State next(Transition transition)
	 		{
	 			return ILLEGAL;
	 		} 
	 	};
	 	ILLEGAL
	 	{
	 		@Override public State next(Transition transition)
	 		{
	 			return ILLEGAL;
	 		} 
	 	};
	 	public abstract State next(Transition transition)
	 	//abstract method that will be overidden based on state
	 	{
	 		return null;
	 	}
	}
	public static enum Transition //
	{
		RECIEVE,
		SEND
	}
	/*public static void main(String[] args)// starts running when you execute java statemachine.java
	{
		State finish = run(State.Waiting, Recieve, Send);// hard coded arguments (e.g. new ect.) defined next
		// finish is an instance of enumerated type State
	}
	public static State run(State start, Transition...transitions) {//? Second argument is an array called “transitions” of enum type Transtions
		// State is defined earlier in second line
	 	
		State state = start;
		for (Transition transition : transitions)//for loop iterating over list of arguments (see above)
		{
			state = state.next(transition);

		}
	} */
	
}
	
