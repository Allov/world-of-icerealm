package ca.qc.icerealm.bukkit.plugins.time;

import java.util.List;
import java.util.logging.Logger;

public class TimeLoop implements Runnable {
	private final Logger logger = Logger.getLogger(("Minecraft"));
	private boolean _stop = false;
	private TimeSubject _timeServer;

	public void setTimeServer(TimeSubject s) {
		_timeServer = s;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!_stop) {
			
			List<TimeObserver> observers = _timeServer.getDueListener(System.currentTimeMillis());
			
			for (TimeObserver ob : observers) {
				_timeServer.removeListener(ob);
				ob.timeHasCome(System.currentTimeMillis());
			}
			
			try {
				Thread.sleep(50);
			}
			catch (Exception ex) {
				this.logger.info("Exception in TimeLoop: " + ex.toString());
			}
		}
		
	}
}
