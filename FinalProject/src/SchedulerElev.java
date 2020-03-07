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
			byte[] data = new byte[2];
			receivePacket = new DatagramPacket(data,data.length);
			try {
				//Waits to receive the packet
				System.out.println("Waiting...");
				ElevSocket.receive(receivePacket);
			} catch(IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			String received = receivePacket.getData().toString();
			int floornum = Integer.parseInt(received);
			floors.put(elevNumber, floornum);
			
			System.out.println(ElevNum);
		}
	}
	public static void main(String[] args) throws Exception {
		Thread SchElev,SchElev1,SchElev2,SchElev3;
		ElevetorFloors shared = new ElevetorFloors();
		SchElev = new Thread(new SchedulerElev(69,0,shared), "Elev1");
		SchElev1 = new Thread(new SchedulerElev(70,1,shared), "Elev2");
		SchElev2 = new Thread(new SchedulerElev(71,1,shared), "Elev3");
		SchElev3 = new Thread(new SchedulerElev(72,2,shared), "Elev4");
		SchElev.start();
		SchElev1.start();
		SchElev2.start();
		SchElev3.start();
	}
}
