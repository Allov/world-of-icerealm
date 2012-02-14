package ca.qc.icerealm.bukkit.plugins.time;

import java.util.ArrayList;
import java.util.Date;
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
		_observers = new ArrayList<TimeObserver>();
		// creating the loop thread and starting it
		_loop = new TimeLoop();
		_loop.setTimeServer(this);
		_thread = new Thread(_loop);
		_thread.start();
	}
	
	public static TimeServer getInstance() {
		if (_instance == null) {
			_instance = new TimeServer();
		}
		return _instance;
	}

	@Override
	public void addListener(TimeObserver obs, long when) {	
		if (_observers != null && obs != null) {
			obs.setAlaram(when + System.currentTimeMillis());
			_observers.add(obs);
			displayInfo("addListener: " + obs.getAlarm() + "ms will be in " + (obs.getAlarm() - System.currentTimeMillis()) + "ms");
		}
	}
	
	@Override
	public void addListener(TimeObserver obs, Date when) {
		long dateToMillis = when.getTime() - System.currentTimeMillis();
		if (dateToMillis > 0) {
			this.addListener(obs, dateToMillis);	
		}
	}
		
	@Override
	public void removeListener(TimeObserver obs) {
		if (obs != null && _observers.contains(obs)) {
			_observers.remove(obs);
			displayInfo("removeListener: " + (obs.getAlarm() - System.currentTimeMillis()) + "ms to be called");
		}
	}

	@Override
	public List<TimeObserver> getDueListener(long timeStamp)  {
		List<TimeObserver> list = new ArrayList<TimeObserver>();
		for (TimeObserver t : _observers) {
			if (t.getAlarm() < System.currentTimeMillis()) {
				list.add(t);
			}
		}
		if (list.size() > 0) {
			displayInfo("dueListener: " + list.size() + "/" + _observers.size() + " listeners");
		}
		return list;
	}
	
	private void displayInfo(String info) {
		this.logger.info("[TimeServer] " + info);
	}

	

	
}
