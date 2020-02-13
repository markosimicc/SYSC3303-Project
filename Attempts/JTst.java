import static org.junit.jupiter.api.Assertions.*;
import java.time.*;


import org.junit.jupiter.api.Test;

class JTst {
	Thread myFloor;
	Thread myScheduler;
	Thread myElevator;
	Scheduler sch = new Scheduler();
	int num = 1;	
	public LocalTime time;
	
	@Test
	//Test floor thread  creation
	public void Test1() {
		myFloor = new Thread(new FloorSubsystem(sch), "Floor");
		String str = new String("Floor");
		assertEquals(str, this.myFloor.getName(),"Thread name is 'Floor'");
		assertNotEquals("Nothing", this.myFloor.getName());
	}
	@Test
	//Test scheduler thread creation
	public void Test2() {
		myScheduler = new Thread(sch, "Scheduler");
		assertEquals("Scheduler", this.myScheduler.getName(), "Thread name is 'Scheduler'");
		assertNotEquals("Nothing", this.myScheduler.getName());
	}
	@Test
	//Test elevator thread creation
	public void Test3() {
		myElevator = new Thread(new ElevatorSubsystem(num, sch), "Elevator");
		assertEquals("Elevator", this.myElevator.getName(), "Thread name is 'Elevator'");
		assertNotEquals("Nothing", this.myElevator.getName());
	}
	
	@Test
	//Test scheduler put method
	public void Test4() {
		Struct temp = new Struct(1, 3, num, time.now());
		sch.put(temp);
		assertEquals(temp, sch.str, "NICE!");
		assertNotEquals(null, sch.str);
	}
	
	@Test
	//Test scheduler get method
	public void Test5() {
		Struct temp = new Struct(1, 3, num, time.now());
		sch.put(temp);
		Struct temp2 = sch.get(true);
		assertEquals(null, sch.str, "NICE!");
		assertNotEquals(temp, sch.str);
		assertEquals(temp, temp2, "NICE!");	
		assertNotEquals(null, temp);
	}
	
	@Test
	//Test Struct class constructor
	public void Test6() {
		LocalTime timeTest = time.now();
		Struct test = new Struct(1,2,3, timeTest);
		assertEquals(1, test.direction, "NICE!");
		assertNotEquals(10, test.direction);
		assertEquals(2, test.floor, "NICE!");
		assertNotEquals(20, test.floor);
		assertEquals(3, test.elevator, "NICE!");
		assertNotEquals(30, test.elevator);
		assertEquals(timeTest, test.time, "NICE!");
		assertNotEquals(time.NOON, test.time);  //Assuming test is not run at NOON :)
		
	}
	
}
