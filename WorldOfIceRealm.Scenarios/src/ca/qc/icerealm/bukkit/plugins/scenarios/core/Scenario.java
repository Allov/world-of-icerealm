package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;


public abstract class Scenario {
	
	private WorldZone _zone;
	private List<Player> _players;
	private String _name;
	private Server _server;
	private JavaPlugin _plugin;
	private World _world;
	
	
	public abstract boolean isTriggered();
	public abstract void triggerScenario();
	public abstract boolean isComplete();
	public abstract void abortScenario();
	public abstract boolean abortWhenLeaving();
	public abstract boolean canBeTriggered();
	
	public World getWorld() {
		return _world;
	}
	
	public void setWorld(World w) {
		_world = w;
	}
	
	public JavaPlugin getPlugin() {
		return _plugin;
	}
	
	public void setPlugin(JavaPlugin p) {
		_plugin = p;
	}
	
	public Server getServer() {
		return _server;
	}
	
	public void setServer(Server s) {
		_server = s;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public WorldZone getZone()
	{
		return _zone;
	}
	
	public void setZone(WorldZone zone) {
		_zone = zone;
	}
	
	public void addPlayer(Player p) {
		if (_players == null) {
			_players = new ArrayList<Player>();
		}
		_players.add(p);
	}
	
	public void removePlayer(Player p) {
		_players.remove(p);
	}
	
	public List<Player> getPlayers() {
		if (_players == null) {
			_players = new ArrayList<Player>();
		}
		return _players;
	}
	

	
}
