package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

public class FactoryEvent {

	public static enum EventType { KILLING_SPREE, TREASURE_HUNT }
	
	public FactoryEvent() {
		
	}
	
	public Event getEvent(String name) {
		
		Event e = null;
		
		if (name.equalsIgnoreCase("treasurehunt")) {
			e = new TreasureHunt();
		}
		else if (name.equalsIgnoreCase("killingspree")) {
			e = new KillingSpree();
		}
		
		return e;
	}
	
}
