package ca.qc.icerealm.bukkit.plugins.time;

public interface TimeSubject {
	void addListener(TimeObserver obs, long when);
}
