package ca.qc.icerealm.bukkit.plugins.time;

import java.util.HashMap;

public class TimeLoop implements Runnable {

	private boolean _stop = true;
	private TimeServer _timeServer;
	
	
	public TimeLoop(TimeServer s) {
		_timeServer = s;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!_stop) {
			
			long currentTime = System.currentTimeMillis();
			
						
			
			try {
				Thread.sleep(100);
			}
			catch (Exception ex) {}
		}
		
	}
	
	public void stop() {
		_stop = true;
	}

}
