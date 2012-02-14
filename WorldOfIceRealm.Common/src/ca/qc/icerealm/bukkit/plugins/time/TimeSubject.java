package ca.qc.icerealm.bukkit.plugins.time;

import java.util.List;

public interface TimeSubject {
	void addListener(TimeObserver obs, long when);
	void removeListener(TimeObserver obs);
	List<TimeObserver> getDueListener(long timeStamp);
}
