package ca.qc.icerealm.bukkit.plugins.simplekits;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SimpleKitsCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandName, String[] params) {
		
		if (sender instanceof Player && commandName.equalsIgnoreCase("kit") && params.length > 0) {
			SimpleKitsProvider.getInstance().kit(params[0], (Player)sender);
		}
		
		return true;
	}

}
