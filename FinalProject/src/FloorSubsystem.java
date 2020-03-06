/**
 * FloorSubsystem reads information in from a text file, puts the information into an instance of RequestInfo class 
 * and passes that instance to the scheduler using an instance of scheduler
 * @author Mahmoud Al Sous
 *
 */
import java.io.*;
import java.util.Scanner;
import java.net.*;

public class FloorSubsystem implements Runnable {
	private Scheduler scheduler;
	private byte[] buf;
	private String line;
	private int count;
	DatagramSocket sendReceiveSocket;
	DatagramPacket sendPacket, receivePacket;
	
	/**
	 * Constructor for FloorSubsystem
	 * @param scheduler
	 */
	public FloorSubsystem(Scheduler scheduler) {
		this.scheduler = scheduler;
		try {
	    	  //Constructs a datagram socket used to send and receive from any port	
	          sendReceiveSocket = new DatagramSocket();
	      } catch (SocketException se) {   
	         se.printStackTrace();
	         System.exit(1);
	      }
	}
	
	/**
	 * Locates the file, accesses it and uses a scanner to read the file line by line.
	 * The scanner splits the line of input using spaces as separators and puts each of the inputs separated into a string array
	 * @return type: RequestInfo
	 */
	public void readInput() {
		File textFile = new File("InputFile.txt");
		Scanner scanner;
		try {
			scanner = new Scanner(textFile);
			
			while(scanner.hasNextLine()) {
				count++;
			}
			
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				System.out.println("Floor requested at: " + line);
				
				try {
					//Constructs the sendpacket with the byte array created
					sendPacket = new DatagramPacket(line.getBytes(), line.getBytes().length, InetAddress.getLocalHost(), 23);
					sendReceiveSocket.send(sendPacket);
		        } catch (UnknownHostException e) {
		        	e.printStackTrace();
		            System.exit(1);
		        } catch (IOException e) {
					e.printStackTrace();
				}
				
				receivePacket = new DatagramPacket(buf, buf.length);
				
				try {
			     	  //Waits to receive the packet
			     	  System.out.println("Waiting...");
			           sendReceiveSocket.receive(receivePacket);
			       } catch(IOException e) {
			          e.printStackTrace();
			          System.exit(1);
			       }
				
				String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
				System.out.println("Elevator has arrived at floor : " + received + "\n");
			}
			scanner.close();
			//return requests;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * run() calls the readInput() method to return the info read into an instance variable and displays the information as a message
	 * as well as passes the information returned to the scheduler
	 */
	@Override
	public void run() {
		scheduler.sendInfo(count);
		readInput();
	}
}


