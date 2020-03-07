import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class TestingScheduler {

	@Test
	void test() {
		LocalTime time = LocalTime.MIDNIGHT;
		Buffer buf = new Buffer();
		ElevetorFloors floors = new ElevetorFloors();
		Scheduler schTest = new Scheduler(buf, floors);
		RequestInfo info = new RequestInfo("Up", 1, 2, time);
		buf.put(info);
		buf.put(info);
		assertEquals(buf.get(), info);
		assertEquals(buf.get(), info);
	}
	
	@Test
	void test1() {
		ElevetorFloors floors = new ElevetorFloors();
		
		assertEquals(floors.get(0), 1);
		assertEquals(floors.get(1), 1);
		
		floors.put(0, 22);
		assertEquals(floors.get(0),22);
	}
	

}
