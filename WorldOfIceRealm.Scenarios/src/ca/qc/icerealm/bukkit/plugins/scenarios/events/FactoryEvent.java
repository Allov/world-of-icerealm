package ca.qc.icerealm.bukkit.plugins.scenarios.events;

public class FactoryEvent {

	public static enum EventType { KILLING_SPREE, TREASURE_HUNT, BARBARIAN_CAMP, ARENA }
	
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
		else if (name.equalsIgnoreCase("barbarian")) {
			e = new BarbarianRaid();
		}
		else if (name.equalsIgnoreCase("arena")) {
			e = new Arena();
		}
		else if (name.equalsIgnoreCase("infestation")) {
			e = new InfestationAdapter();
		}
		
		return e;
	}
	
}
