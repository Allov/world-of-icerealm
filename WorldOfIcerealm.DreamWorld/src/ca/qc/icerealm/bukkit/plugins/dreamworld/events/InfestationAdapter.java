package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ca.qc.icerealm.bukkit.plugins.dreamworld.PinPoint;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.scenarios.frontier.Frontier;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.Infestation;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.InfestationConfiguration;
import ca.qc.icerealm.bukkit.plugins.scenarios.zone.ScenarioZoneServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneSubject;

public class InfestationAdapter extends BaseEvent {

	private Logger _logger = Logger.getLogger("Minecraft");
	private Infestation _infestation;
	private InfestationConfiguration _configInfestation;
	private ZoneSubject _zoneServer;
	
	@Override
	public void setWelcomeMessage(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEndMessage(String s) {
		// TODO Auto-generated method stub
	}

	@Override
	public void activateEvent() {
		
		JavaPlugin plugin = (JavaPlugin) _server.getPluginManager().getPlugin("WoI.DreamWorld");
		_zoneServer = this.getZoneSubjectInstance();
		
		// on batit la config de base
		_configInfestation = new InfestationConfiguration();
		_configInfestation.BurnDuringDaylight = false;
		_configInfestation.DelayBeforeRegeneration = 60000; // 1 minute
		_configInfestation.DelayBeforeRespawn = 60000; // 1 minute
		_configInfestation.IntervalBetweenSpawn = 1500; // 1 sec et demi
		_configInfestation.ProbabilityToSpawn = 1;
		_configInfestation.RegenerateExplodedBlocks = true;
		_configInfestation.UseInfestedZoneAsRadius = false;
		_configInfestation.ResetWhenPlayerLeave = true;
		_configInfestation.Server = _server;
		_configInfestation.EnterZoneMessage = ChatColor.YELLOW + "You are entering into " + ChatColor.DARK_RED + "an infested zone!";
		_configInfestation.LeaveZoneMessage =  ChatColor.YELLOW + "You are leaving " + ChatColor.RED + "an infested zone!";
		_configInfestation.HealthModifier = Frontier.getInstance().calculateHealthModifier(_source, _source.getWorld().getSpawnLocation());
		
		// on formatte la zone!
		String[] configData = _config.split(",");
		if (configData.length > 3) {
			try {
				
				for (int i = 0; i < _zones.size(); i++) {
					List<PinPoint> zone = _zones.get(i);
					
					// on cherche une zone nommé activation
					if (zone.size() == 2 && zone.get(0).Name.equalsIgnoreCase("activation")) {
						
						String activationZoneString = (_source.getX() + zone.get(0).X) + "," + (_source.getZ() + zone.get(0).Z) + "," + 
													  (_source.getX() + zone.get(1).X) + "," + (_source.getZ() + zone.get(1).Z) + "," +
													  (_source.getY() + zone.get(0).Y) + "," + (_source.getY() + zone.get(1).Y);
						
						_configInfestation.InfestedZone = activationZoneString;
						_logger.info(activationZoneString);
					}
				}
				
				_configInfestation.MaxMonstersPerSpawn = Integer.parseInt(configData[0]); // config
				_configInfestation.SpawnerRadiusActivation = Integer.parseInt(configData[1]); // config
				_configInfestation.SpawnerQuantity = Integer.parseInt(configData[2]); // config
				_configInfestation.SpawnerMonsters = configData[3].replace("+", ","); // config

				if (plugin != null && _zoneServer != null) {
					_infestation = new Infestation(plugin, _configInfestation, _zoneServer);
					_zoneServer.addListener(_infestation);
					_server.getPluginManager().registerEvents(_infestation, plugin);
					_logger.info("zone listener done!");
				}
			}
			catch (Exception ex) {
				_logger.info("could not create an infestation zone!");
			}
		}
		
		
	}

	@Override
	public void releaseEvent() {
		// TODO Auto-generated method stub
		_zoneServer.removeListener(_infestation);
	}

	@Override
	public String getName() {
		return "infestation";
	}

}
