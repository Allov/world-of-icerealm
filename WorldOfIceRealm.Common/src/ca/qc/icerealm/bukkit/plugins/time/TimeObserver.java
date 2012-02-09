package ca.qc.icerealm.bukkit.plugins.time;

public interface TimeObserver {
	
	void timeHasCome(long time);
	void setAlaram(long time);
	void getAlarm();
	
}
