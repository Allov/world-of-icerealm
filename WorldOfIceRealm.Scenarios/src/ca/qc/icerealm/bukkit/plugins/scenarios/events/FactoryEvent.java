package ca.qc.icerealm.bukkit.plugins.scenarios.events;

public class FactoryEvent {

	private String[] _eventList = new String[] { "treasurehunt", "killingspree", "barbarian", "arena", "infestation", "boss" };
	
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
		else if (name.equalsIgnoreCase("boss")) {
			e = new Boss();
		}

		return e;
	}
	
	public String[] getEventsName() {
		return _eventList;
	}
}
