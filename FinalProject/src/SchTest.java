
public class SchTest {
	public static void main(String[] args) throws Exception {
		Thread Schfloor;
		Buffer shared = new Buffer();
		Schfloor = new Thread(new SchedulerFloor(shared), "Floor Scheduler");
		Thread Sch;
		

		Thread SchElev,SchElev1,SchElev2,SchElev3;
		ElevetorFloors sharedFloors = new ElevetorFloors();
		Sch = new Thread(new Scheduler(shared, sharedFloors), "Scheduler");
		SchElev = new Thread(new SchedulerElev(69,0,sharedFloors), "Elev1");
		SchElev1 = new Thread(new SchedulerElev(70,1,sharedFloors), "Elev2");
		SchElev2 = new Thread(new SchedulerElev(71,2,sharedFloors), "Elev3");
		SchElev3 = new Thread(new SchedulerElev(72,3,sharedFloors), "Elev4");
		SchElev.start();
		SchElev1.start();
		SchElev2.start();
		SchElev3.start();
		Schfloor.start();
		Sch.start();
	}
}
