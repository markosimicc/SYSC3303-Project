import java.io.*;
import java.util.Scanner;
import java.time.*;

public class FloorSubsystem implements Runnable {
	private String[] tokens;
	private Scheduler scheduler;
	
	public FloorSubsystem(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public void run() {
		File textFile = new File("C:\\Users\\Marko\\Documents\\InputFile.txt");
		int direction = 0;
		int elevator;
		int floor;
		LocalTime time;
		
		Scanner scanner;
		try {
			scanner = new Scanner(textFile);
			
			while(scanner.hasNext()) {
				tokens = scanner.nextLine().split(" ");
			}
			
			time = LocalTime.parse(tokens[0]);
			floor = Integer.parseInt(tokens[1]);
			if(tokens[2].equals("Up")) {
				direction = 1;
				
			}
			else{
				direction = 0;
			}
			elevator = Integer.parseInt(tokens[3]);
			
			s struct = new s(direction, floor, elevator, time);
			System.out.println("Floor requested at : " + " " + struct.time + " " + struct.floor + " " +struct.direction + " " + struct.elevator);
			scheduler.put(struct);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s str = scheduler.get(true);
		System.out.println("Elevator has arrived at floor : " + str.time + " "+ str.floor  + " "+str.direction + " "+ str.elevator);
	}
}
