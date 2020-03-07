import java.util.*;

public class Buffer {
	public Queue<RequestInfo> requests = new LinkedList<>();
	public synchronized void put(RequestInfo req)
    {
		requests.add(req);
		//notifyAll();
        notifyAll();
    }
	public synchronized RequestInfo get() {
		try {
			 while (requests.isEmpty()) {
				 wait();
			 }
		}catch (InterruptedException e) {
			 return null;
			 }
		RequestInfo r = requests.remove();
		notifyAll();
		return r;
	}
}

