import java.util.*;

public class ElevetorFloors {
	public ArrayList<Integer> floors = new ArrayList<>();
	public void put(int index,int num)
    {
		synchronized(floors) {
			floors.add(index, num);
		}
        //notifyAll();
    }
	public int get(int floor) {
		synchronized(floors) {
			int current = floors.remove(floor);
			notifyAll();
			return current;
		}
	}	
}
