package ca.qc.icerealm.bukkit.plugins.time;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TimeLoop implements Runnable {
	private final Logger logger = Logger.getLogger(("Minecraft"));
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
				_timeServer.removeListener(ob);
				this.logger.info("TimeLoop - time has come AND removing listener");
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
