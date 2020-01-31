/**
 * FloorSubsystem reads information in from a text file, puts the information into an instance of RequestInfo class 
 * and passes that instance to the scheduler using an instance of scheduler
 * @author Mahmoud Al Sous
 *
 */
import java.io.*;
import java.util.Scanner;
import java.time.*;

public class FloorSubsystem implements Runnable {
	private String[] tokens;
	private Scheduler scheduler;
	private String direction;
	private int elevator;
	private int floor;
	private LocalTime time;
	
	/**
	 * Constructor for FloorSubsystem
	 * @param scheduler
	 */
	public FloorSubsystem(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	/**
	 * Locates the file, accesses it and uses a scanner to read the file line by line.
	 * The scanner splits the line of input using spaces as separators and puts each of the inputs separated into a string array
	 * @return type: RequestInfo
	 */
	public RequestInfo readInput() {
	File textFile = new File("C:\\Users\\mahmoudalsous\\Downloads\\SYSC3303-Project-master\\SYSC3303-Project-master\\InputFile.txt");
		Scanner scanner;
		try {
			scanner = new Scanner(textFile);
			
			while(scanner.hasNext()) {
				tokens = scanner.nextLine().split(" ");
			}
		time = LocalTime.parse(tokens[0]);
		floor = Integer.parseInt(tokens[1]);
		direction = tokens[2];
		elevator = Integer.parseInt(tokens[3]);
		
		RequestInfo input = new RequestInfo(direction, floor, elevator, time);
		scanner.close();
		return input;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * run() calls the readInput() method to return the info read into an instance variable and displays the information as a message
	 * as well as passes the information returned to the scheduler
	 */
	@Override
	public void run() {
		RequestInfo input;
		input = readInput();
		System.out.println("Floor requested at: " + " " + input.time + " " + input.floor + " " +input.direction + " " + input.elevator);
		scheduler.sendInfo(input);

		RequestInfo response = scheduler.recieveInfo(true);
		System.out.println("Elevator has arrived at floor : " + response.time + " "+ response.floor  + " "+response.direction + " "+ response.elevator);
	}
}

