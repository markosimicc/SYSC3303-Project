import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class SchedulerElev implements Runnable{
	public int ElevNum;
	DatagramSocket ElevSocket;
	DatagramPacket sendPacket, receivePacket;
	public SchedulerElev(int port) {
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
			ElevNum = data[0];
			System.out.println(ElevNum);
		}
	}
	public static void main(String[] args) throws Exception {
		Thread SchElev,SchElev1,SchElev2,SchElev3;
		Buffer shared = new Buffer();
		SchElev = new Thread(new SchedulerElev(69), "Floor");
		SchElev1 = new Thread(new SchedulerElev(70), "Floor");
		SchElev2 = new Thread(new SchedulerElev(71), "Floor");
		SchElev3 = new Thread(new SchedulerElev(72), "Floor");
		SchElev.start();
		SchElev1.start();
		SchElev2.start();
		SchElev3.start();
	}
}
