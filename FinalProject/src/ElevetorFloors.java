import java.util.*;

public class ElevetorFloors {
	public ArrayList<Integer> floors = new ArrayList<>();
	
	public ElevetorFloors() {
		floors.add(0,1);
		floors.add(1,1);
		floors.add(2,1);
		floors.add(3,1);
	}
	
	public void put(int index,int num)
    {
		synchronized(floors) {
			floors.set(index, num);
		}
        //notifyAll();
    }
	public int get(int floor) {
		synchronized(floors) {
			int current = floors.get(floor);
			//notifyAll();
			return current;
		}
	}	
}
