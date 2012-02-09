package ca.qc.icerealm.bukkit.plugins.time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeServer implements TimeSubject {

	private static TimeServer _instance = null;
	private HashMap<TimeObserver, Long> _observers;
	
	
	protected TimeServer() {
		// part le thread!
		_observers = new HashMap<TimeObserver, Long>();
	}
	
	public static TimeServer getInstance() {
		if (_instance == null) {
			_instance = new TimeServer();
		}
		return _instance;
	}
	
	
	@Override
	public void addListener(TimeObserver obs, long when) {
		if (_observers != null) {
			_observers.put(obs, when);
		}
		
	}

}
