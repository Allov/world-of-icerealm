package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CompassCommandExecutor implements CommandExecutor
{
	private static final String AdvancedCompassParamHelp = "help";
	private static final String AdvancedCompassParamPointPlayer = "point";
	private static final String AdvancedCompassCommandName = "c";
	
	public CompassCommandExecutor() 
	{

	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandName, String[] params) 
	{
		if (sender instanceof Player) {
			Player player = (Player)sender;
			
			if (commandName.equalsIgnoreCase(AdvancedCompassCommandName)) 
			{
				if ((params == null || params.length == 0))
				{
					displayHelp(player);
				}
				else if (params[0].equalsIgnoreCase(AdvancedCompassParamPointPlayer))
				{
					CompassPlayersInfo compassPlayersInfo = CompassPlayersInfo.getInstance();
					
					// TODO Test if player exists...
					compassPlayersInfo.setCurrentPlayerModePlayerName(params[1]);
					player.sendMessage(ChatColor.LIGHT_PURPLE + ">> TEST");
				}
				else if (params[0].equalsIgnoreCase(AdvancedCompassParamHelp))
				{
					displayHelp(player);
				}
			}
			
			return true;
		} 
		else 
		{
			return false;
		}
	}

	private void displayHelp(Player player) 
	{
		player.sendMessage(ChatColor.LIGHT_PURPLE + ">> AdvancedCompass help");
		player.sendMessage(ChatColor.LIGHT_PURPLE + "[ " + ChatColor.YELLOW + "point [playerName], help" + ChatColor.LIGHT_PURPLE + " ]");		
		player.sendMessage("  > " + ChatColor.YELLOW + "/c point [playerName]" + ChatColor.WHITE + ": "+ ChatColor.DARK_GREEN + " Compass now points at this player");		
		player.sendMessage("  > " + ChatColor.YELLOW + "/c help " + ChatColor.WHITE + ": "+ ChatColor.DARK_GREEN + "this blob.");		
	}
}
