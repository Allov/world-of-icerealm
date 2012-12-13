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
	
	/**
	 * Permet d'obtenir un Event selon son nom.
	 * @param name Le nom de l'event voulu
	 * @return Retourne la premiere instance trouvée, il est possible que plusieurs instance ai
	 * le meme nom. Retourne null si aucun event est trouvé.
	 */
	public Event getEvent(String name) {
		for (Event e : _events) {
			if (e.getName().equalsIgnoreCase(name)) {
				return e;
			}
		}
		return null;
	}
	
	/**
	 * Permet d'obtenir tous les event qui ont le nom passée en parametre.
	 * @param name Le nom des events voulues
	 * @return Renvoie une liste des events 
	 */
	public List<Event> getEvents(String name) {
		List<Event> list = new ArrayList<Event>();
		for (Event e : _events) {
			if (e.getName().equalsIgnoreCase(name)) {
				list.add(e);
			}
		}
		
		return list;
	}
	
}
