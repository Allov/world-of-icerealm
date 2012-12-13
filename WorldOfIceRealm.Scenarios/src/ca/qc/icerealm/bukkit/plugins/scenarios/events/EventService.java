package ca.qc.icerealm.bukkit.plugins.scenarios.events;

import java.util.ArrayList;
import java.util.List;

public class EventService {

	private static EventService _instance = null;
	private List<Event> _events;
	
	protected EventService() {
		_events = new ArrayList<Event>();
	}
	
	public static EventService getInstance() {
		if (_instance == null) {
			_instance = new EventService();
		}
		return _instance;
	}
	
	public List<Event> getActiveEvents() {
		return _events;
	}
	
	public void addEvent(Event e) {
		_events.add(e);
	}
	
}
