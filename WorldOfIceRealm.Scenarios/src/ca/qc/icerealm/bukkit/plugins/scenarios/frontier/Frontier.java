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
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultiplierData;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultipliers;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;

public class Frontier implements Listener, CommandExecutor {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private ScenarioService _scenarioService = null;
	private World _world = null;
	private double _divider;
	private boolean _activated = false;
	private double _damage;
	
	public Frontier(World w, double divider, double damage) {
		_scenarioService = ScenarioService.getInstance();
		_world = w;
		_activated = true;
		_divider = divider;
		_damage = damage;
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
		
		/*
		if (distance > _divider) {
			modifier = (double)distance / _divider;
		}
		*/
		if (_divider > 0) {
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
				//logger.info("frontier spawn modfiier: " + modifier + "health is: " + creature.getMaxHealth() + " maxhealth is: " + maxHealth);
				_scenarioService.addExistingEntity(creature.getEntityId(), maxHealth, true, modifier / _damage);
				
				/*
				if (LocationUtil.getDistanceBetween(event.getLocation(), _world.getSpawnLocation()) <= _divider) {
					RareDropsMultiplierData.getInstance().addEntityRareDropsMultiplier(creature.getEntityId(), new RareDropsMultipliers(1.0, 1.0, 0.00));
				}
				else {
					RareDropsMultiplierData.getInstance().addEntityRareDropsMultiplier(creature.getEntityId(), new RareDropsMultipliers(modifier, modifier, modifier));
				}*/
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {

		if (sender.isOp()) {
			
			if (arg3.length == 0) {
				sender.sendMessage(ChatColor.GRAY + "Frontier active: " + ChatColor.YELLOW + _activated);
			}
			
			if (arg3.length == 1 && (arg3[0].contains("help") || arg3[0].contains("?"))) {
				sender.sendMessage(ChatColor.DARK_GREEN + "/fr divider [double] - " + ChatColor.YELLOW + "Divider to determine the strengh (smaller value means stronger)");
				sender.sendMessage(ChatColor.DARK_GREEN + "/fr status [bool] - " + ChatColor.YELLOW + "Activate/Deactivate frontier feature");
				sender.sendMessage(ChatColor.DARK_GREEN + "/fr damage [double] - " + ChatColor.YELLOW + "Divide the health modifier for damage (1 = health modifier value)");
			}
			
			if (arg3.length == 1 && arg3[0].contains("divider")) {
				sender.sendMessage(ChatColor.GRAY + "Frontier divider: " + ChatColor.YELLOW + _divider);
			}
			if (arg3.length == 2 && arg3[0].contains("divider")) {
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
			
			if (arg3.length == 1 && arg3[0].contains("status")) {
				sender.sendMessage(ChatColor.GRAY + "Frontier active: " + ChatColor.YELLOW + _activated);
			}
			if (arg3.length == 2 && arg3[0].contains("status")) {
				boolean activation = Boolean.parseBoolean(arg3[1]);
				_activated = activation;
				sender.sendMessage(ChatColor.GRAY + "Frontier active: " + ChatColor.YELLOW + _activated);
			}
			
			if (arg3.length == 1 && arg3[0].contains("damage")) {
				sender.sendMessage(ChatColor.GRAY + "Frontier damage: " + ChatColor.YELLOW + _damage);
			}
			if (arg3.length == 2 && arg3[0].contains("damage")) {
				double divider = Double.parseDouble(arg3[1]);
				if (divider > 0.0) {
					_damage = divider;
					sender.sendMessage(ChatColor.GRAY + "Frontier damage: " + ChatColor.YELLOW + _damage);
				}
				else {
					sender.sendMessage(ChatColor.GRAY + "Frontier damage: " + ChatColor.RED + divider + " value not valid (must be > 0.0");
				}
				sender.sendMessage(ChatColor.GRAY + "Frontier damage: " + ChatColor.YELLOW + _damage);
			}
		}
		else {
			sender.sendMessage("Only OP can do that");
		}
		return false;
	}

	
	
	
	
	
}
