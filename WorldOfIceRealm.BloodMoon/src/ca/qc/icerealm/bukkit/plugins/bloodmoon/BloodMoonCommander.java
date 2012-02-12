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
				arg0.sendMessage(ChatColor.DARK_GREEN + "Blood Moon Status: " + ChatColor.GRAY + _moon.isActive());
			}
			
		}
		else 
		{
			arg0.sendMessage("Only OP can do that");
		}
		
		
		return false;
	}

}
