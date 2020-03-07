import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * 
 * @author Ross Matthew
 * a list of the transitions of the Schedulers states
 */
public class ElevatorSubsystem implements Runnable {
	private int elevatorNum; // Declaration of class fields
	private int currFloor;
	private ArrayList<Integer> floors = new ArrayList<>();
	
	
	public int hasTask = 1;
	private Scheduler scheduler;
	private RequestInfo info;
	public ElevState stat = ElevState.WAITING;
	public ElevatorTransistions trans;
	public boolean current = false;
	
	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket, receiveSocket;
	private int port;
	private byte[] responseMessage = new byte[100];

	/**
	 * 
	 * Constructor for the Elevator Subsystem class
	 * @param elevatorNum
	 */
	
	public ElevatorSubsystem(int elevatorNum, int portNum, int receivePort) {
		this.elevatorNum = elevatorNum;  //Set elevator number
		this.port = portNum;  //Set elevator port number
		this.currFloor = 1;
		
		//Create sockets
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(receivePort);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}


	public void receiveAndEcho() {
		
		while (true) {  //If there is another task to complete
			String temp = Integer.toString(currFloor);
			byte[] data = temp.getBytes();
			//Create packet with current floor to send to scheduler
			try {
				sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			//Send packet with current floor to scheduler
			try {
				sendSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

			byte dataR[] = new byte[10];
			receivePacket = new DatagramPacket(dataR, dataR.length);
			//Receive packet from scheduler containing the next floor to go to
			try {
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//Set the current floor value to the one sent by the scheduler			
			String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
			String[] elem = received.split(" ");
			int requestFloor = Integer.parseInt(elem[0]);
			int lastFloor = Integer.parseInt(elem[2]);
			
			System.out.println(Thread.currentThread().getName() + ":");
			System.out.println("Received a request from floor " + requestFloor + " to go to floor " + lastFloor + ".");
			stat = stat.next(ElevatorTransistions.Door_closing);
			System.out.println(Thread.currentThread().getName() + " is in state: " + stat);
			System.out.println("Going from floor " + currFloor + " to floor " + requestFloor + ".");
			stat = stat.next(ElevatorTransistions.Start_Motor);
			System.out.println(Thread.currentThread().getName() + " is in state: " + stat);
			currFloor = requestFloor;
			stat = stat.next(ElevatorTransistions.Reach_Floor);
			System.out.println(Thread.currentThread().getName() + " is in state: " + stat);
			floors.add(lastFloor);
			stat = stat.next(ElevatorTransistions.Door_Opening);
			System.out.println(Thread.currentThread().getName() + " is in state: " + stat);
			System.out.println("Picked up passenger at floor " + currFloor + ".");
			stat = stat.next(ElevatorTransistions.Door_closing);
			System.out.println(Thread.currentThread().getName() + " is in state: " + stat);
			System.out.println("Going from floor " + currFloor + " to floor " + floors.get(0) + ".");
			stat = stat.next(ElevatorTransistions.Start_Motor);
			System.out.println(Thread.currentThread().getName() + " is in state: " + stat);
			currFloor = floors.get(0);
			stat = stat.next(ElevatorTransistions.Reach_Floor);
			System.out.println(Thread.currentThread().getName() + " is in state: " + stat);
			floors.remove(0);
			System.out.println("Arrived at floor " + currFloor + ".");
			stat = stat.next(ElevatorTransistions.Door_Opening);
			System.out.println(Thread.currentThread().getName() + " is in state: " + stat + "\n");
			
		}
	}

	/**
	 * 
	 * run() receives the information from the scheduler, prints out a message and
	 * sends the information back using an instance of scheduler
	 * 
	 */
	public enum ElevState {
		WAITING {
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
				return (transition == ElevatorTransistions.Door_Opening) ? WAITING : ILLEGAL;
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
		receiveAndEcho();
	}
	
	public static void main(String[] args) throws Exception {
		Thread elevator1, elevator2, elevator3, elevator4;

		elevator1 = new Thread(new ElevatorSubsystem(1, 69, 2000),"Elevator1");
		elevator2 = new Thread(new ElevatorSubsystem(2, 70, 2001),"Elevator2");
		elevator3 = new Thread(new ElevatorSubsystem(3, 71, 2002),"Elevator3");
		elevator4 = new Thread(new ElevatorSubsystem(4, 72, 2003),"Elevator4");

		elevator1.start();
		elevator2.start();
		elevator3.start();
		elevator4.start();
		
	}
}