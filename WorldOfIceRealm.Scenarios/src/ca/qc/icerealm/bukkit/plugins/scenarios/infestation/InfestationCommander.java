package ca.qc.icerealm.bukkit.plugins.scenarios.infestation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class InfestationCommander implements CommandExecutor {

	private Infestation _infestation;
	private InfestationConfiguration _config;
	private String _name;
	
	public InfestationCommander(Infestation infestation, String name) {
		_infestation = infestation;
		_config = infestation.getConfig();
		_name = name;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		
		if (sender.isOp()) {
			if (arg3.length == 0) {
				displayActiveConfig(sender);
			}
			
			if (arg3.length == 1 && arg3[0].contains("reset")) {
				_infestation.resetInfestation(_config);
				sender.sendMessage(ChatColor.GRAY + "Resetting the infestation");
				displayActiveConfig(sender);
				
			}
			
			if (arg3.length == 1 && arg3[0].contains("status")) {
				sender.sendMessage(ChatColor.GRAY + "Infestion active: " + ChatColor.YELLOW + _infestation.isActive());
			}
			
			if (arg3.length == 1 && arg3[0].contains("help")) {
				sender.sendMessage(ChatColor.DARK_GREEN + "/" + _name + " status - " + ChatColor.YELLOW + "Display isActive()");
				sender.sendMessage(ChatColor.DARK_GREEN + "/" + _name + " start|stop|reset - " + ChatColor.YELLOW + "Start/Stop/Reset the infesation");
				sender.sendMessage(ChatColor.DARK_GREEN + "/" + _name + " qty [int] - " + ChatColor.YELLOW + "Number of Spawners");
				sender.sendMessage(ChatColor.DARK_GREEN + "/" + _name + " perspawn [int] - " + ChatColor.YELLOW + "Number of monsters per spawner");
				sender.sendMessage(ChatColor.DARK_GREEN + "/" + _name + " cooldown [long] - " + ChatColor.YELLOW + "CoolDown for spawners");
				sender.sendMessage(ChatColor.DARK_GREEN + "/" + _name + " radius [double] - " + ChatColor.YELLOW + "Activation Radius per spawner");
				sender.sendMessage(ChatColor.DARK_GREEN + "/" + _name + " prob [int] - " + ChatColor.YELLOW + "Prob. for monsters to spawn");
				sender.sendMessage(ChatColor.DARK_GREEN + "/" + _name + " interval [long] - " + ChatColor.YELLOW + "Interval between monster spawn");
				sender.sendMessage(ChatColor.DARK_GREEN + "/" + _name + " regen [long] - " + ChatColor.YELLOW + "Delay before respawn blocks");
				sender.sendMessage(ChatColor.DARK_GREEN + "/" + _name + " activeregen [bool] - " + ChatColor.YELLOW + "Enable/disable block regen");
				sender.sendMessage(ChatColor.DARK_GREEN + "/" + _name + " burn [bool] - " + ChatColor.YELLOW + "Enable/disable burn during day");
				sender.sendMessage(ChatColor.DARK_GREEN + "/" + _name + " monsters [string] - " + ChatColor.YELLOW + "Possible monsters");
				sender.sendMessage(ChatColor.DARK_GREEN + "ex: " + ChatColor.YELLOW + "spider,skeleton,creeper");
				
			}
			if (arg3.length == 1 && arg3[0].contains("stop")) {
				_infestation.stopInfestation();
				sender.sendMessage(ChatColor.GRAY + "Infestation Stopped");
			}
			if (arg3.length == 1 && arg3[0].contains("start")) {
				_infestation.startInfestation();
				sender.sendMessage(ChatColor.GRAY + "Infestation Started");
				
			}

			if (arg3.length == 1 && arg3[0].contains("qty")) {
				sender.sendMessage(ChatColor.GRAY + "Spawner quantity: " + ChatColor.YELLOW + _infestation.getSpawnerQuantity());
			}
			if (arg3.length == 2 && arg3[0].contains("qty")) {
				try {
					int qty = Integer.parseInt(arg3[1]);
					_infestation.setSpawnerQuantity(qty);
					sender.sendMessage(ChatColor.GRAY + "Spawner quantity: " + ChatColor.YELLOW + _infestation.getSpawnerQuantity());
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Value was not valid");
				}			
			}
			
			if (arg3.length == 1 && arg3[0].contains("burn")) {
				sender.sendMessage(ChatColor.GRAY + "Burn during daylight: " + ChatColor.YELLOW + _config.BurnDuringDaylight);
			}
			if (arg3.length == 2 && arg3[0].contains("burn")) {
				try {
					boolean convertBool = Boolean.parseBoolean(arg3[1]);
					_config.BurnDuringDaylight = convertBool;
					sender.sendMessage(ChatColor.GRAY + "Burn during daylight: " + ChatColor.YELLOW + _config.BurnDuringDaylight);
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Value was not valid");
				}
			}
			
			if (arg3.length == 1 && arg3[0].contains("perspawn")) {
				sender.sendMessage(ChatColor.GRAY + "Monster per spawner: " + ChatColor.YELLOW + _config.MaxMonstersPerSpawn);
			}
			if (arg3.length == 2 && arg3[0].contains("perspawn")) {
				try {
					int qty = Integer.parseInt(arg3[1]);
					_config.MaxMonstersPerSpawn = qty;
					sender.sendMessage(ChatColor.GRAY + "Monster per spawner: " + ChatColor.YELLOW + _config.MaxMonstersPerSpawn);
					
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Value was not valid");
				}			
			}
			
			if (arg3.length == 1 && arg3[0].contains("cooldown")) {
				sender.sendMessage(ChatColor.GRAY + "Spawner CoolDown: " + ChatColor.YELLOW + _config.DelayBeforeRespawn);
			}
			if (arg3.length == 2 && arg3[0].contains("cooldown")) {
				try {
					long qty = Long.parseLong(arg3[1]);
					_config.DelayBeforeRespawn = qty;
					sender.sendMessage(ChatColor.GRAY + "Spawner CoolDown: " + ChatColor.YELLOW + _config.DelayBeforeRespawn);
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Value was not valid");
				}			
			}
			
			if (arg3.length == 1 && arg3[0].contains("radius")) {
				sender.sendMessage(ChatColor.GRAY + "Spawner radius: " + ChatColor.YELLOW + _config.SpawnerRadiusActivation);
			}
			if (arg3.length == 2 && arg3[0].contains("radius")) {
				try {
					long qty = Long.parseLong(arg3[1]);
					_config.SpawnerRadiusActivation = qty;
					sender.sendMessage(ChatColor.GRAY + "Spawner radius: " + ChatColor.YELLOW + _config.SpawnerRadiusActivation);
					
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Value was not valid");
				}			
			}
			
			if (arg3.length == 1 && arg3[0].contains("prob")) {
				sender.sendMessage(ChatColor.GRAY + "Spawner Prob: " + ChatColor.YELLOW + "1/" + _config.ProbabilityToSpawn);
			}
			if (arg3.length == 2 && arg3[0].contains("prob")) {
				try {
					int qty = Integer.parseInt(arg3[1]);
					_config.ProbabilityToSpawn = qty;
					sender.sendMessage(ChatColor.GRAY + "Spawner prob: " + ChatColor.YELLOW + _config.ProbabilityToSpawn);
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Value was not valid");
				}			
			}
			
			if (arg3.length == 1 && arg3[0].contains("monsters")) {
				sender.sendMessage(ChatColor.GRAY + "Spawner monsters: " + ChatColor.YELLOW + _config.SpawnerMonsters);
			}
			if (arg3.length == 2 && arg3[0].contains("monsters")) {
				try {
					_config.SpawnerMonsters = arg3[1];
					sender.sendMessage(ChatColor.GRAY + "Spawner monsters: " + ChatColor.YELLOW + _config.SpawnerMonsters);
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Value was not valid");
				}			
			}
			
			if (arg3.length == 1 && arg3[0].contains("regen")) {
				sender.sendMessage(ChatColor.GRAY + "Block Regen Delay: " + ChatColor.YELLOW + _config.DelayBeforeRegeneration);
			}
			if (arg3.length == 2 && arg3[0].contains("regen")) {
				try {
					long qty = Long.parseLong(arg3[1]);
					_config.DelayBeforeRegeneration = qty;
					sender.sendMessage(ChatColor.GRAY + "Block Regen Delay: " + ChatColor.YELLOW + _config.DelayBeforeRegeneration);
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Value was not valid");
				}			
			}
			
			if (arg3.length == 1 && arg3[0].contains("activeregen")) {
				sender.sendMessage(ChatColor.GRAY + "Block Regen: " + ChatColor.YELLOW + _config.RegenerateExplodedBlocks);
			}
			if (arg3.length == 2 && arg3[0].contains("activeregen")) {
				try {
					boolean convertBool = Boolean.parseBoolean(arg3[1]);
					_config.RegenerateExplodedBlocks = convertBool;
					sender.sendMessage(ChatColor.GRAY + "Block Regen: " + ChatColor.YELLOW + _config.RegenerateExplodedBlocks);
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Value was not valid");
				}
			}
			
			if (arg3.length == 1 && arg3[0].contains("interval")) {
				sender.sendMessage(ChatColor.GRAY + "Interval between monsters: " + ChatColor.YELLOW + _config.IntervalBetweenSpawn);
			}
			if (arg3.length == 2 && arg3[0].contains("interval")) {
				try {
					long qty = Long.parseLong(arg3[1]);
					_config.IntervalBetweenSpawn = qty;
					sender.sendMessage(ChatColor.GRAY + "Interval between monsters: " + ChatColor.YELLOW + _config.IntervalBetweenSpawn);
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Value was not valid");
				}			
			}
			
			return true;
		}
		else {
			sender.sendMessage("You're not an OP! This will be reported!");
		}
		
		return false;
		
	}
	
	private void displayActiveConfig(CommandSender sender) {
		sender.sendMessage(ChatColor.GRAY + "Infestion active: " + ChatColor.YELLOW + _infestation.isActive());
		sender.sendMessage(ChatColor.GRAY + "Spawner quantity: " + ChatColor.YELLOW + _infestation.getSpawnerQuantity());
		sender.sendMessage(ChatColor.GRAY + "Monster per spawner: " + ChatColor.YELLOW + _config.MaxMonstersPerSpawn);
		sender.sendMessage(ChatColor.GRAY + "Spawner Max monsters: " + ChatColor.YELLOW + _config.MaxMonstersPerSpawn);
		sender.sendMessage(ChatColor.GRAY + "Spawner CoolDown: " + ChatColor.YELLOW + _config.DelayBeforeRespawn);
		sender.sendMessage(ChatColor.GRAY + "Spawner radius: " + ChatColor.YELLOW + _config.SpawnerRadiusActivation);
		sender.sendMessage(ChatColor.GRAY + "Spawner Prob: " + ChatColor.YELLOW + "1/" + _config.ProbabilityToSpawn);
		sender.sendMessage(ChatColor.GRAY + "Spawner monsters: " + ChatColor.YELLOW + _config.SpawnerMonsters);
		sender.sendMessage(ChatColor.GRAY + "Block Regen Delay: " + ChatColor.YELLOW + _config.RegenerateExplodedBlocks);
		sender.sendMessage(ChatColor.GRAY + "Block Regen: " + ChatColor.YELLOW + _config.DelayBeforeRegeneration);
		sender.sendMessage(ChatColor.GRAY + "Interval between monsters: " + ChatColor.YELLOW + _config.IntervalBetweenSpawn);
		sender.sendMessage(ChatColor.GRAY + "Burn during daylight: " + ChatColor.YELLOW + _config.BurnDuringDaylight);
	}

}
