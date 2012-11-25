package ca.qc.icerealm.bukkit.plugins.scenarios.ambush;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.LocationUtil;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.Infestation;
import ca.qc.icerealm.bukkit.plugins.scenarios.infestation.InfestationConfiguration;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;


public class Ambush implements Runnable, CommandExecutor {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private Server _server = null;
	private int _interval = 0;
	private int _prob = 0;
	private JavaPlugin _plugin;
	private int _radius = 32;
	private boolean _active = false;
	
	public Ambush(JavaPlugin plugin, int interval, int prob, int radius) {
		_server = plugin.getServer();
		_interval = interval;
		_prob = prob;
		_plugin = plugin;
		_radius = radius;
		
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this, 0, interval, TimeUnit.MILLISECONDS);
		_active = true;
	}

	@Override
	public void run() {
		
		if (_active) {
			Player[] players = _server.getOnlinePlayers();
			for (Player p : players) {
				
				// ajouté un calcul de probablité selon l'inventaire du joueurs
				boolean createAmbush = RandomUtil.getDrawResult(_prob);
				if (createAmbush) {
					
					logger.info("creating an ambush!");
					
					World world = _server.getWorld("world");
					WorldZone zone = new WorldZone(p.getLocation(), _radius);
								
					InfestationConfiguration config = new InfestationConfiguration();
					config.BurnDuringDaylight = false;
					config.HealthModifier = ScenarioService.getInstance().calculateHealthModifierWithFrontier(p.getLocation(), world.getSpawnLocation());
					config.InfestedZone = zone.toString();
					config.ProbabilityToSpawn = 1;
					config.DelayBeforeRespawn = 100000;
					config.UseInfestedZoneAsRadius = true;
					config.IntervalBetweenSpawn = 500;
					config.MaxMonstersPerSpawn = 5;
					config.SpawnerQuantity = 2;
					config.Server = _server;
					config.ResetWhenPlayerLeave = true;
					config.RegenerateExplodedBlocks = false;
					config.DelayBeforeRegeneration = 0;
					config.SpawnerMonsters = "zombie,spider,skeleton,enderman,pigzombie";
					config.RareDropMultiplier = 0.0;
					config.EnterZoneMessage = ChatColor.GREEN + "You have been ambushed! Escape or kill the monsters!";
					config.LeaveZoneMessage = ChatColor.YELLOW + "You escaped an ambush!";
					config.KeepInMemory = false;
					
					Infestation infest = new Infestation(_plugin, config, ZoneServer.getInstance());
					infest.playerEntered(p);
					_server.broadcastMessage(ChatColor.YELLOW + p.getDisplayName() + ChatColor.GOLD + " has been ambushed! Help him!");
					break;
					
				}
			}	
		}
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		
		if (sender.isOp()) {
			
			if (arg3.length == 0) {
				sender.sendMessage(ChatColor.GRAY + "Ambush active: " + ChatColor.YELLOW + _active);
			}
			
			if (arg3.length == 1 && arg3[0].contains("prob")) {
				sender.sendMessage(ChatColor.GRAY + "Ambush prob: " + ChatColor.YELLOW + _prob);
			}
			if (arg3.length == 2 && arg3[0].contains("prob")) {
				int prob = Integer.parseInt(arg3[1]);
				if (prob > 0.0) {
					_prob = prob;
					sender.sendMessage(ChatColor.GRAY + "Ambush prob: " + ChatColor.YELLOW + _prob);
				}
				else {
					sender.sendMessage(ChatColor.GRAY + "Ambush prob: " + ChatColor.RED + _prob + " value not valid");
				}
			}
			
			if (arg3.length == 1 && arg3[0].contains("status")) {
				sender.sendMessage(ChatColor.GRAY + "Ambush active: " + ChatColor.YELLOW + _active);
			}
			if (arg3.length == 2 && arg3[0].contains("status")) {
				boolean active = Boolean.parseBoolean(arg3[1]);
				_active = active;
				sender.sendMessage(ChatColor.GRAY + "Ambush active: " + ChatColor.YELLOW + _active);
			}
			
		}
		
		return false;
	}
}
