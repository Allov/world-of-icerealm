package ca.qc.icerealm.bukkit.plugins.scenarios.barbarian;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioEventsListener;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class BarbarianRaid implements Listener {

	private WorldZone _zone;
	private Server _server;
	private SubScenario _firstHouse;
	
	private List<SubScenario> _scenarios;
	private List<Player> _players;
	private boolean _isActive;
	private List<Monster> _monsters;
	
	public BarbarianRaid(Server s, String zone, String a) {
		_server = s;
		_zone = new WorldZone(_server.getWorld("world"), zone);
		
		/*
		WorldZone activation = new WorldZone(_server.getWorld("world"), a);
		_firstHouse = new SubScenario(s, _server.getWorld("world"), _zone, activation, 4, CreatureType.ZOMBIE);
		*/
		
	}
	
	public BarbarianRaid(Server s, String[] zones, String[] activation) {
		_server = s;
		_scenarios = new ArrayList<SubScenario>();
		for (int i = 0; i < zones.length; i++) {
			WorldZone z = new WorldZone(s.getWorld("world"), zones[i]);
			WorldZone a = new WorldZone(s.getWorld("world"), activation[i]);
			SubScenario sc = new SubScenario(s, s.getWorld("world"), z, a, 4, CreatureType.ZOMBIE);
			_scenarios.add(sc);
		}
	}
	
	


}
