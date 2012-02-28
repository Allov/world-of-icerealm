package ca.qc.icerealm.bukkit.plugins.zone;

import java.util.List;

import org.bukkit.Server;

public interface ZoneSubject {
	void addListener(ZoneObserver obs);
	void removeListener(ZoneObserver obs);
	List<ZoneObserver> getObservers();
	void stopListening();
	void startListening();
	Server getServer();
	
}
