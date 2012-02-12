package ca.qc.icerealm.bukkit.plugins.time;

import java.util.ArrayList;
import java.util.List;

public class TimeLoop implements Runnable {

	private boolean _stop = false;
	private TimeServer _timeServer;
	private List<TimeObserver> _removeObs = new ArrayList<TimeObserver>();

	public void setTimeServer(TimeServer s) {
		_timeServer = s;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!_stop) {
			
			List<TimeObserver> observers = _timeServer.getDueListener(System.currentTimeMillis());
			
			for (TimeObserver ob : observers) {
				ob.timeHasCome(System.currentTimeMillis());			
			}
			
			_timeServer.removeListener(observers);
			
			try {
				Thread.sleep(50);
			}
			catch (Exception ex) {
				_stop = true;
			}
		}
		
	}
}
