package util.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Clients should extend this class to:
 * 1. Add new methods to implement the required listener interface 
 * 2. Delegate these listener methods to fireEvent
 * 3. Add getEventX(int) convenience methods to retrieve and cast event data 
 */
public class EventRecorder {
	
	private class Event {
		public final long time;
		public final Object[] data;
		
		public Event(Object[] data) {
			time = System.currentTimeMillis();
			this.data = data;
		}
	}
	
	public List<Event> events = new ArrayList<Event>(100);
	
	public void fireEvent(Object... data) {
		events.add(new Event(data));
	}
	
	public int count() {
		return events.size();
	}
	
	public long timeSpan() {
		return events.get(events.size() - 1).time - events.get(0).time;
	}
	
	public long timeBetweenEvents(int firstEvent, int secondEvent) {
		return events.get(secondEvent).time - events.get(firstEvent).time;
	}
	
	public Object[] getEventData(int event) {
		return events.get(event).data;
	}
}
