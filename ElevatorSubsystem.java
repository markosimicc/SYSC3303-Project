import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
	private int currFloor = 01;
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
	 * 
	 * 
	 * 
	 * @param elevatorNum
	 * 
	 * 
	 * 
	 */
	
	
	public ElevatorSubsystem(int elevatorNum, int portNum) {
		this.elevatorNum = elevatorNum;  //Set elevator number
		this.port = portNum;  //Set elevator port number
		
		//Create sockets
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(port);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}


	public void receiveAndEcho() {
		
		while (true) {  //If there is another task to complete
			byte data[] = new byte[2];
			data[0] = (byte) currFloor;
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

			byte dataR[] = new byte[7];
			receivePacket = new DatagramPacket(dataR, dataR.length);
			//Receive packet from scheduler containing the next floor to go to
			try {
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//Set the current floor value to the one sent by the scheduler
			byte startFloor[] = new byte[2];
			byte endFloor[] = new byte[2];
			System.arraycopy(receivePacket.getData(), 0, startFloor, 0, startFloor.length);
			System.arraycopy(receivePacket.getData(), 5, endFloor, 0, endFloor.length);
			int nextFloor = 0;
			for (int i=0; i<2; i++) {
				nextFloor = (nextFloor << 8 ) - Byte.MIN_VALUE + (int) startFloor[i];
			}
			if(nextFloor != 0) {
				currFloor = nextFloor;
			}
			int addFloor = 0;
			for (int i=0; i<2; i++) {
			      addFloor = ( addFloor << 8 ) - Byte.MIN_VALUE + (int) endFloor[i];
			}
			if(!floors.contains(addFloor) && (addFloor != 0)){
				floors.add(addFloor);
			}
			
			//Create packet to send to scheduler to see if there are more tasks
			String checkStr = "?";
			byte[] checkBytes = checkStr.getBytes();
			sendPacket = new DatagramPacket(checkBytes, checkBytes.length, receivePacket.getAddress(), port);
			//Send task request packet
			try {
				sendSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//Create packet to receive info saying if there is another task to do
			receivePacket = new DatagramPacket(dataR, dataR.length);
			try {
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte nextTask[] = receivePacket.getData();
			hasTask = nextTask[0];  //Update if there is another task to do or not (1 or 0)
			
			if(!(hasTask == 1)  && (!floors.isEmpty())) {
				int goTo = floors.get(0);
				for(int i=0; i<floors.size()-1; i++) {
					if(goTo>floors.get(i+1)) {
						goTo = floors.get(i+1);
					}
				}
				currFloor = goTo;
			}
		}
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
		receiveAndEcho();
	}
	
	public static void main(String[] args) throws Exception {
		Thread elevator1, elevator2, elevator3, elevator4;

		elevator1 = new Thread(new ElevatorSubsystem(1, 69),"Elevator1");
		elevator2 = new Thread(new ElevatorSubsystem(2, 70),"Elevator1");
		elevator3 = new Thread(new ElevatorSubsystem(3, 71),"Elevator1");
		elevator4 = new Thread(new ElevatorSubsystem(4, 72),"Elevator1");

		elevator1.start();
		elevator2.start();
		elevator3.start();
		elevator4.start();
		
	}
/*
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
	*/
}