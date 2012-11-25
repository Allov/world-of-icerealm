package ca.qc.icerealm.bukkit.plugins.scenarios.frontier;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import ca.qc.icerealm.bukkit.plugins.common.LocationUtil;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;

public class Frontier implements Listener, CommandExecutor {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private ScenarioService _scenarioService = null;
	private World _world = null;
	private double _divider;
	private boolean _activated = false;
	
	public Frontier(World w, double divider) {
		_scenarioService = ScenarioService.getInstance();
		_world = w;
		_activated = true;
		_divider = divider;
	}
	
	public void setActivated(boolean activate) {
		_activated = activate;
	}
	
	public void setDivider(double d) {
		_divider = d;
	}
	
	public void setWorld(World w) {
		_world = w;
	}
	
	public double calculateHealthModifier(Location loc, Location spawn) {
		double distance = LocationUtil.getDistanceBetween(loc, spawn);
		double modifier = 0.0;
		
		if (distance > _divider) {
			modifier = (double)distance / _divider;
		}
		
		return modifier;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterSpawn(CreatureSpawnEvent event) {
		
		if (_activated && event.getEntity() instanceof Monster) {
			double modifier = calculateHealthModifier(event.getLocation(), _world.getSpawnLocation());

			if (modifier > 0 && !_scenarioService.monsterAlreadyPresent(event.getEntity().getEntityId())) {
				LivingEntity creature = event.getEntity();
				int maxHealth = creature.getMaxHealth() + (int)(modifier * creature.getMaxHealth());
				_scenarioService.addExistingEntity(creature.getEntityId(), maxHealth);
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {

		if (sender.isOp()) {
			if (arg3.length == 0) {
				sender.sendMessage(ChatColor.GRAY + "Frontier active: " + ChatColor.YELLOW + _activated);
			}
			
			if (arg3.length == 1 && arg3[0] == "divider") {
				sender.sendMessage(ChatColor.GRAY + "Frontier divider: " + ChatColor.YELLOW + _divider);
			}
			if (arg3.length == 2 && arg3[0] == "divider") {
				double divider = Double.parseDouble(arg3[1]);
				if (divider > 0.0) {
					_divider = divider;
					sender.sendMessage(ChatColor.GRAY + "Frontier divider: " + ChatColor.YELLOW + _divider);
				}
				else {
					sender.sendMessage(ChatColor.GRAY + "Frontier divider: " + ChatColor.RED + divider + " value not valid (must be > 0.0");
				}
				sender.sendMessage(ChatColor.GRAY + "Frontier active: " + ChatColor.YELLOW + _activated);
			}
			
			if (arg3.length == 1 && arg3[0] == "status") {
				sender.sendMessage(ChatColor.GRAY + "Frontier active: " + ChatColor.YELLOW + _activated);
			}
			if (arg3.length == 2 && arg3[0] == "status") {
				boolean activation = Boolean.parseBoolean(arg3[1]);
				_activated = activation;
				sender.sendMessage(ChatColor.GRAY + "Frontier active: " + ChatColor.YELLOW + _activated);
			}
		}
		else {
			sender.sendMessage("Only OP can do that");
		}
		return false;
	}

	
	
	
	
	
}
