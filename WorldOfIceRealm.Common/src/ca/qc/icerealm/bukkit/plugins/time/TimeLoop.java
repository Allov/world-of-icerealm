package ca.qc.icerealm.bukkit.plugins.time;

import java.util.List;
import java.util.concurrent.Executors;
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

		try {
			List<TimeObserver> observers = _timeServer.getDueListener(System.currentTimeMillis());
			
			for (TimeObserver ob : observers) {
				_timeServer.removeListener(ob);
				Executors.newSingleThreadExecutor().execute(new TimeExecutor(ob));
				//ob.timeHasCome(System.currentTimeMillis());
			}
			
			try {
				Thread.sleep(50);
			}
			catch (Exception ex) {
				this.logger.info("Exception in TimeLoop: " + ex.toString());
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			this.logger.throwing("TimeLoop", "run()", ex);
		}
	}
}
