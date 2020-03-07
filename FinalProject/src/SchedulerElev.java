import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class SchedulerElev implements Runnable{
	public int ElevNum;
	DatagramSocket ElevSocket;
	DatagramPacket sendPacket, receivePacket;
	private int elevNumber;
	ElevetorFloors floors;
	public SchedulerElev(int port,int elevNumber,ElevetorFloors floors) {
		this.elevNumber = elevNumber;
		this.floors = floors;
		try {
	    	  //Constructs a datagram socket used to send and receive from any port	
	          ElevSocket = new DatagramSocket(port);
	      } catch (SocketException se) {   
	         se.printStackTrace();
	         System.exit(1);
	      }
	}
	public void run() {
		while (true) {
			byte[] data = new byte[10];
			receivePacket = new DatagramPacket(data,data.length);
			try {
				//Waits to receive the packet
				ElevSocket.receive(receivePacket);
			} catch(IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			String received = new String( receivePacket.getData(), 0, receivePacket.getLength());
			int floornum = Integer.parseInt(received);
			floors.put(elevNumber, floornum);
			
			System.out.print(Thread.currentThread().getName() + " received floor number " + floornum + "\n");
		}
	}
	
}
