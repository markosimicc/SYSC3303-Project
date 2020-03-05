import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 
 * @author Archit Bhatia(states)
 *
 * 
 * 
 *         a list of the transitions of the Schedulers states
 * 
 */
public class ElevatorSubsystem implements Runnable {
	private int elevatorNum; // Declaration of class fields
	private int currFloor = 1;
	private Scheduler scheduler;
	private RequestInfo info;
	// private static Logger LOGGER = Logger.getLogger(Scheduler.class.getName());
	public ElevState stat = ElevState.WAITING;
	public ElevatorTransistions trans;

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket, receiveSocket;
	private int port = 69;
	private byte[] responseMessage = new byte[100];

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

		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(port);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	public int getFloor() {

		return currFloor;
	}

	public void setFloor(int x) {

		currFloor = x;
	}

	public void receiveAndEcho() {

		//while (scheduler.getRequest()) {

			byte data[] = new byte[1];
			data[0] = (byte) currFloor;
			try {
				sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			try {
				sendSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

			byte dataR[] = new byte[1];
			receivePacket = new DatagramPacket(dataR, dataR.length);

			try {
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

			byte temp[] = receivePacket.getData();
			currFloor = temp[0];
		   //  }
	}

	/**
	 * 
	 * run() receives the information from the scheduler, prints out a message and
	 * 
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
		while (true) {
			info = scheduler.recieveInfo(false);
			System.out.println(
					"Elevator Recieved: " + info.time + " " + info.floor + " " + info.direction + " " + info.elevator);
			stat = stat.next(ElevatorTransistions.Door_closing);
			System.out.println("Elevator is in state: " + stat);
			System.out.println("Elevator " + this.elevatorNum + " is ready to move.");
			stat = stat.next(ElevatorTransistions.Start_Motor);
			System.out.println("Elevator is in state: " + stat);
			scheduler.sendInfo(info);
			stat = stat.next(ElevatorTransistions.Reach_Floor);
			System.out.println("Elevator is in state: " + stat);
			stat = stat.next(ElevatorTransistions.Door_Opening);
		}
	}
}