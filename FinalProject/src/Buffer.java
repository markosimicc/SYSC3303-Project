import java.util.*;

public class Buffer {
	public Queue<RequestInfo> requests = new LinkedList<>();
	public void put(RequestInfo req)
    {
        requests.add(req);
        //notifyAll();
    }
	public RequestInfo get() {
		synchronized(requests) {
			try {
				 while (requests.isEmpty()) {
					 requests.wait();
				 }
			}catch (InterruptedException e) {
				 return null;
				 }
			}
			RequestInfo r = requests.remove();
			notifyAll();
			return r;
		}
}
