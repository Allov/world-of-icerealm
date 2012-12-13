package ca.qc.icerealm.bukkit.plugins.scenarios.events;

import java.util.ArrayList;
import java.util.List;

public class EventService {

	private static EventService _instance = null;
	private List<Event> _events;
	private List<EventListener> _listener;
	
	protected EventService() {
		_events = new ArrayList<Event>();
		_listener = new ArrayList<EventListener>();
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
		for (EventListener ev : _listener) {
			ev.eventActivated(e);
		}
	}
	
	public void clearEvents() {
		_events.clear();
		for (EventListener ev : _listener) {
			ev.eventCleared();
		}
	}
	
}
