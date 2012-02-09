package ca.qc.icerealm.bukkit.plugins.time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeServer implements TimeSubject {

	private static TimeServer _instance = null;
	private List<TimeObserver> _observers;
	
	
	protected TimeServer() {
		// part le thread!
		_observers = new ArrayList<TimeObserver>();
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
			long alarm = when + System.currentTimeMillis();
			obs.setAlaram(alarm);
			
		
		}
		
	}
	
	public List<TimeObserver> getDueListener(long timeStamp)  {
		List<TimeObserver> list = new ArrayList<TimeObserver>();
		
		
		
		return list;
	}
	
	public void removeListener(List<TimeObserver> observer) {
		_observers.removeAll(observer);
	}

}
