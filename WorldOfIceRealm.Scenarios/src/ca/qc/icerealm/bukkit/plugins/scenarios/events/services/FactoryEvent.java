package ca.qc.icerealm.bukkit.plugins.scenarios.events.services;

import ca.qc.icerealm.bukkit.plugins.scenarios.events.Arena;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.BarbarianRaid;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.Boss;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.Event;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.InfestationAdapter;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.KillingSpree;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.Mayhem;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.RescueSurvivors;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.TreasureHunt;

public class FactoryEvent {

	private String[] _eventList = new String[] { "treasurehunt", "killingspree", "barbarian", "arena", "infestation", "boss", "rescuesurvivor", "mayhem" };
	
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
		else if (name.equalsIgnoreCase("rescuesurvivor")) {
			e = new RescueSurvivors();
		}
		else if (name.equalsIgnoreCase("mayhem")) {
			e = new Mayhem();
		}

		return e;
	}
	
	public String[] getEventsName() {
		return _eventList;
	}
}
