package ca.qc.icerealm.bukkit.plugins.scenarios.barbarian;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
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
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioEventsListener;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class BarbarianRaid implements Listener {

	private WorldZone _zone;
	private Server _server;
	private ScenarioEvent _firstHouse;
	
	private List<ScenarioEvent> _scenarios;
	private List<Player> _players;
	private boolean _isActive;
	private List<Monster> _monsters;
	private JavaPlugin _plugin;
	private World _world;
	private ZoneSubject _zoneServer;
	
	private Disclaimer _disclaimer;
		
	public BarbarianRaid(JavaPlugin plugin, ZoneSubject subject) {
		_plugin = plugin;
		_world = _plugin.getServer().getWorld("world");
		_zoneServer = subject;
		
		/*
		WorldZone activation = new WorldZone(_server.getWorld("world"), a);
		_firstHouse = new SubScenario(s, _server.getWorld("world"), _zone, activation, 4, CreatureType.ZOMBIE);
		*/
		initializeDisclaimer();
		initializeBridgeAmbush();
		initializeSpawningZone();
		
	}
	
	public void releaseBarabarianRaid() {
		_zoneServer.removeListener(_disclaimer);
	}
	
	private void initializeDisclaimer() {
		WorldZone disclaimerZone = new WorldZone(_world, "-157,805,-151,810,60,70");
		List<String> disclaimer = new ArrayList<String>();
		disclaimer.add(ChatColor.LIGHT_PURPLE + "[Great warriors do not fear in the face of the enemy");
		disclaimer.add(ChatColor.LIGHT_PURPLE + "Great victories are achieved throught blood and men]");
		disclaimer.add(ChatColor.YELLOW + "Do not go ahead if you are not properly equipped!!");
		disclaimer.add(ChatColor.DARK_GREEN + "[" + ChatColor.DARK_GREEN + "New Obj" + ChatColor.DARK_GREEN  +"] " + ChatColor.WHITE + "Follow the path ahead and fight your way to the top.");
		_disclaimer = new Disclaimer(_plugin.getServer(), disclaimerZone, disclaimer);
		_zoneServer.addListener(_disclaimer);
	}
	
	private void initializeBridgeAmbush() {
		WorldZone disclaimerZone = new WorldZone(_world, "-157,805,-151,810,60,70");
		
		
	}
	
	private void initializeSpawningZone() {
		
	}
	
	public BarbarianRaid(Server s, String[] zones, String[] activation) {
		_server = s;
		_scenarios = new ArrayList<ScenarioEvent>();
		for (int i = 0; i < zones.length; i++) {
			WorldZone z = new WorldZone(s.getWorld("world"), zones[i]);
			WorldZone a = new WorldZone(s.getWorld("world"), activation[i]);
			ScenarioEvent sc = new ScenarioEvent(s, s.getWorld("world"), z, a, 4, CreatureType.ZOMBIE);
			_scenarios.add(sc);
		}
	}
	
	


}
