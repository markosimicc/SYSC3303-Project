import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

/*
 * Scheduler receives information from FloorSubsystem and passes it to ElevatorSubsystem and vice versa.
 * @author Marko Simic and Michael Slokar (states)
 *
 */

public class Scheduler implements Runnable {
	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendReceiveSocket, floorSocket, replySocket,elevatorSocket;
	public boolean reply = true;
	public Queue<String> upQueue = new LinkedList<>();
	public Queue<String> downQueue = new LinkedList<>();
	public InetAddress floorIp;
	public int floorport;
	public SchedulerTransistions transition;
	private int numElevators = 4;
	public ArrayList<Integer> elevfloors = new ArrayList<>();
	public State st = State.WAITING;

	public RequestInfo str;

	public Scheduler() {
		try {
			floorSocket = new DatagramSocket(23);
			sendReceiveSocket = new DatagramSocket();
			replySocket = new DatagramSocket();
			elevatorSocket = new DatagramSocket(5050);
		}catch (SocketException se) {   // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
		System.out.println("Scheduler Initial State : " + getState(st));

	}

	@Override

	public void run() {
		byte data[] = new byte[2];
		receivePacket = new DatagramPacket(data, data.length);
		try {
			floorSocket.receive(receivePacket);
			} 
		catch (IOException e) {
			//System.out.print("IO Exception: likely:");
			//System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
			}
		System.out.println("Scheduler is gonna receive " + data.toString() + "number of requests");
		floorIp = receivePacket.getAddress();
		floorport = receivePacket.getPort();
		String s = "Ready for Requests";
		byte[] send = s.getBytes();
		sendPacket = new DatagramPacket(send,send.length,floorIp,floorport);
		try {
			replySocket.send(sendPacket);
			}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
			}
		for(int i = 0; i< data[0];i++) {
			receiveRequest();
		}
		for(int i = 0; i <numElevators ; i++) {
			int elevFloor = getElevNumber(23+i);
			elevfloors.add(elevFloor);
		}
		schedule();
		
		

	}
	public void schedule() {
		
	}
	public int getElevNumber(int port) {
		String elev = "What floor";
		byte[] elevreq = elev.getBytes();
		sendPacket = new DatagramPacket(elevreq,elevreq.length,floorIp,port);
		try {
			sendReceiveSocket.send(sendPacket);
			}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
			}
		byte[] reply = new byte[4];
		receivePacket = new DatagramPacket(reply,reply.length);
		try {
			sendReceiveSocket.receive(sendPacket);
			}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
			}
		int a = reply[0];
		return a;

	}
	public void receiveRequest() {
		byte data[] = new byte[50];
		receivePacket = new DatagramPacket(data, data.length);
		try {
			floorSocket.receive(receivePacket);
			} 
		catch (IOException e) {
			//System.out.print("IO Exception: likely:");
			//System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
			}
		String s = "Next";
		byte[] send = s.getBytes();
		sendPacket = new DatagramPacket(send,send.length,floorIp,floorport);
		try {
			replySocket.send(sendPacket);
			}
		catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
			}
		System.out.println("Scheduler received : " + data.toString());
		parse(data);
		
	}
	/**
	 * Takes in the information passed as an argument and notifies the system
	 * @param info
	 */
	public void parse(byte[] msg) {
		String req= "";
		
		
	}
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

		System.out.println("Scheduler is : " + getState(st));

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

		System.out.println("Scheduler is : " + getState(st));

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