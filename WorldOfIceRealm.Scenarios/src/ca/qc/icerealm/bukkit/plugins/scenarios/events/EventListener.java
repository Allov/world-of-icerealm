package ca.qc.icerealm.bukkit.plugins.scenarios.events;

public interface EventListener {
	public void eventCompleted(Object ... param);	
	public void eventActivated(Event e);
	public void eventCleared();
}
