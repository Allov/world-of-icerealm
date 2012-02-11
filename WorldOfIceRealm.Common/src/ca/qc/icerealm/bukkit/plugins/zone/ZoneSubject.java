package ca.qc.icerealm.bukkit.plugins.zone;

public interface ZoneSubject {
	void addListener(ZoneObserver obs);
	void removeListener(ZoneObserver obs);
	void stopListening();
	void startListening();
}
