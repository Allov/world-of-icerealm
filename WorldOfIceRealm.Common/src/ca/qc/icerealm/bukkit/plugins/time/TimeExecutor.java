package ca.qc.icerealm.bukkit.plugins.time;

public class TimeExecutor implements Runnable {

	TimeObserver _observer;
	
	public TimeExecutor(TimeObserver ob) {
		_observer = ob;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		_observer.timeHasCome(System.currentTimeMillis());
	}

}
