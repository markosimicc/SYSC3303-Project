import java.time.*;

class Struct {
	public final int direction;
	public final int floor;
	public final int elevator;
	public final LocalTime time;
	
	public Struct(int direction, int floor, int elevator, LocalTime time) {
		this.direction = direction;
		this.floor = floor;
		this.elevator = elevator;
		this.time = time;
	}
}
