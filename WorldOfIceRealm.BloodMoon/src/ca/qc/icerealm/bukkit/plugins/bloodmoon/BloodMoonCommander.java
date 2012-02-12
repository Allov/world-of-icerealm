package ca.qc.icerealm.bukkit.plugins.bloodmoon;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class BloodMoonCommander implements CommandExecutor {

	private BloodMoon _moon;
	
	public BloodMoonCommander(BloodMoon moon) {
		_moon = moon;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		
		if (arg0.isOp()) {
			
			if (arg3.length > 0 && arg3[0].contains("stop")) {				
				if (_moon.isActive()) {
					_moon.stopBloodMoon();
				}
			}
			if (arg3.length > 0 && arg3[0].contains("start")) {				
				if (!_moon.isActive()) {
					_moon.startBloodMoon();
				}
			}
			if (arg3.length > 0 && arg3[0].contains("reset")) {				
				_moon.initializeTimer();
			}
			if (arg3.length > 0 && arg3[0].contains("status")) {				
				arg0.sendMessage(ChatColor.DARK_GREEN + "Blood Moon Status: " + ChatColor.YELLOW + _moon.isActive());
			}
			
			if (arg3.length == 1 && arg3[0].contains("delay")) {
				arg0.sendMessage(ChatColor.DARK_GREEN + "Blood Moon Delay: " + ChatColor.YELLOW + _moon.getDelay() + "ms");
			}
			if (arg3.length == 2 && arg3[0].contains("delay")) {
				try {
					long d = Long.parseLong(arg3[1]);
					_moon.setDelay(d);
					arg0.sendMessage(ChatColor.DARK_GREEN + "Blood Moon Delay changed to: " + ChatColor.YELLOW + _moon.getDelay() + " ms");
				}
				catch (Exception ex) {
					arg0.sendMessage(ChatColor.GRAY + "Value was not valid");
				}
				
				
			}
			if (arg3.length == 1 && arg3[0].contains("prob")) {
				arg0.sendMessage(ChatColor.DARK_GREEN + "Blood Moon Prob: " + ChatColor.YELLOW + "1/" + ChatColor.GOLD + _moon.getProbability());
			}
			if (arg3.length == 2 && arg3[0].contains("prob")) {
				try {
					int d = Integer.parseInt(arg3[1]);
					_moon.setProbability(d);
					arg0.sendMessage(ChatColor.DARK_GREEN + "Blood Moon Prob: " + ChatColor.YELLOW + "1/" + ChatColor.GOLD + _moon.getProbability());
				}
				catch (Exception ex) {
					arg0.sendMessage(ChatColor.GRAY + "Value was not valid");
				}
			}
			if (arg3.length == 1 && arg3[0].contains("hardreset")) {
				arg0.getServer().broadcastMessage(ChatColor.YELLOW + "Blood Moon Plugin is hardreseting");
				_moon.onDisable();
				_moon.onEnable();
			}

		}
		else 
		{
			arg0.sendMessage("Only OP can do that");
		}
		
		
		return false;
	}

}
