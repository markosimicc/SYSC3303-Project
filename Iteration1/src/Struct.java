
import java.time.*;

public class Struct {
	public int direction;
	public int floor;
	public int elevator;
	public LocalTime time;
	
	public Struct(int direction, int floor, int elevator, LocalTime time) {
		this.direction = direction;
		this.floor = floor;
		this.elevator = elevator;
		this.time = time;
	}
}