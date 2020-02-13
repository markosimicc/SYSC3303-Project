import java.time.LocalTime;

/**
 * 
 */

/**
 * @author Mahmoud
 *
 */
public class s {
	//0 for down, 1 for up
	public int direction;
	public int floor;
	public int elevator;
	public LocalTime time;
	
	public s(int direction, int floor, int elevator, LocalTime time) {
		this.direction = direction;
		this.floor = floor;
		this.elevator = elevator;
		this.time = time;
	}
}