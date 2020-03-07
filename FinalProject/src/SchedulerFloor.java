import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalTime;

public class SchedulerFloor implements Runnable {
	DatagramSocket floorSocket,sendReceieveSocket;
	DatagramPacket sendPacket, receivePacket;
	private String direction;
	private int elevator;
	private int floor;
	private LocalTime time;
	Buffer buf;
	public RequestInfo info;
	public SchedulerFloor(Buffer b) {
		buf = b;
		try {
	    	  //Constructs a datagram socket used to send and receive from any port	
	          floorSocket = new DatagramSocket(23);
	          sendReceieveSocket = new DatagramSocket();
	      } catch (SocketException se) {   
	         se.printStackTrace();
	         System.exit(1);
	      }
	}
	public void run() {
		while(true) {
			byte[] data = new byte[20];
			receivePacket = new DatagramPacket(data,data.length);
			try {
				//Waits to receive the packet
				System.out.println("Waiting...");
				floorSocket.receive(receivePacket);
			} catch(IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			int port = receivePacket.getPort();
			String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
			System.out.println("Got message : " + received + "\n");
			String[] tokens = received.split(" ");
			
			time = LocalTime.parse(tokens[0]);
			floor = Integer.parseInt(tokens[1]);
			direction = tokens[2];
			elevator = Integer.parseInt(tokens[3]);
			info = new RequestInfo(direction,floor,elevator,time);
			buf.put(info);
			String s = "rECEIED";
			byte[] msg = s.getBytes();
			try {
				//Constructs the sendpacket with the byte array created
				sendPacket = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(),port );
			} catch (UnknownHostException e) {
				e.printStackTrace();
				System.exit(1);
			}
			try {
				sendReceieveSocket.send(sendPacket);
	        } catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

}
