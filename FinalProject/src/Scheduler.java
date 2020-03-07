import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/*
 * Scheduler receives information from FloorSubsystem and passes it to ElevatorSubsystem and vice versa.
 * @author Marko Simic and Michael Slokar (states)
 *
 */

public class Scheduler implements Runnable {
	public ArrayList<Integer> curr = new ArrayList<>();
	public boolean reply = true;
	private RequestInfo info;
	public SchedulerTransistions transition;
	public State st = State.WAITING;
	public RequestInfo str;
	int currentChosenElevator;
	DatagramSocket mainSocket;
	DatagramPacket sendPacket, receivePacket;
	ElevetorFloors floors;
	Buffer buf;
	int num1;
	int num2;
	int num3;
	int num4;
	public Scheduler(Buffer b,ElevetorFloors floors) {
		this.floors = floors;
		buf =b;
		System.out.println("Scheduler Initial State : " + getState(st));
	}

	@Override

	public void run() {
		while(true) {
			info = buf.get();
			num1 = floors.get(0);
			
			num2 = floors.get(1);
			num3 = floors.get(2);
			num4 = floors.get(3);
			curr.add(0,num1);
			curr.add(1, num2);
			curr.add(2,num3);
			curr.add(3,num4);
			System.out.println("Handling Request : " +  info.elevator + info.direction + info.floor);
			int a = 500;
			for (int i:curr) {
				int distance = info.elevator - curr.get(i);
				if( Math.abs(distance) < Math.abs(a)) {
					a = distance;
					currentChosenElevator = i;
				}
				if(info.direction.equals("Up")) {
					
				}
			}
			sendInfo(currentChosenElevator);	
			
		}
	}
	public void sendInfo(int elev) {
		String s = info.elevator + " " + info.direction + " " + info.floor;
		byte[] data = s.getBytes();
		try {
			//Constructs the sendpacket with the byte array created
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(),69+elev );
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		try {
			mainSocket.send(sendPacket);
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Takes in the information passed as an argument and notifies the system
	 * @param info
	 */
/*	public synchronized void sendInfo(RequestInfo info) {
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
		System.out.println("Scheduler is : " + getState(st));
		notifyAll();
	}

	/**
	 * Checks if it passing infomation or not, then returns the information to pass
	 * it to the other subsystems
	 * @param isReply
	 * @return
	 */

	/*public synchronized RequestInfo recieveInfo(boolean isReply) {
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
		System.out.println("Scheduler is : " + getState(st));
		return data;
	}*/
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
																				// return value “SCHEDULING”Else -
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