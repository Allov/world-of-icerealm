package ca.qc.icerealm.bukkit.plugins.time;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class TimeServer implements TimeSubject {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private static TimeServer _instance = null;
	private List<TimeObserver> _observers;
	private TimeLoop _loop;
	private Thread _thread;
	
	
	protected TimeServer() {
		// part le thread!
		_observers = new ArrayList<TimeObserver>();
		_loop = new TimeLoop();
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
			_observers.add(obs);
		}
		
		if (_observers.size() > 0) {
			_loop.setTimeServer(this);
			
			if (_thread == null) {
				_thread = new Thread(_loop);
				_thread.start();
			}

		}
		
	}
	
	public List<TimeObserver> getDueListener(long timeStamp)  {
		List<TimeObserver> list = new ArrayList<TimeObserver>();
		for (TimeObserver t : _observers) {
			if (t.getAlarm() < System.currentTimeMillis()) {
				list.add(t);
			}
		}

		return list;
	}
	
	public void removeListener(List<TimeObserver> observer) {
		_observers.removeAll(observer);
	}

}
