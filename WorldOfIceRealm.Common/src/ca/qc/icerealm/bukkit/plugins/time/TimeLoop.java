package ca.qc.icerealm.bukkit.plugins.time;

public class TimeLoop implements Runnable {

	private boolean _stop = true;
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!_stop) {
			
			
			
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
