package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CompassCommandExecutor implements CommandExecutor
{
	private static final String AdvancedCompassParamHelp = "help";
	private static final String AdvancedCompassParamPointPlayer = "player";
	private static final String AdvancedCompassParamCurrentLocation = "current_location";
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
					PlayerCompassData compassData = compassPlayersInfo.getPlayerCompassData(player.getName());
					
					boolean playerIsOnline = false;
					
					for (int i = 0; i < player.getServer().getOnlinePlayers().length; i++)
					{
						if (player.getServer().getOnlinePlayers()[i].getName().equalsIgnoreCase(params[1]))
						{
							playerIsOnline = true;
							break;
						}
					}
					
					if (playerIsOnline)
					{
						compassData.setCurrentPlayerModePlayerName(params[1]);	
						compassData.setCurrentCompassMode(CompassMode.Player);
						compassPlayersInfo.setPlayerCompassData(player.getName(), compassData);
						
						player.sendMessage(ChatColor.LIGHT_PURPLE + ">> Current pointing location is now set to player " + params[1]);
						
						CompassToggler toggler = new CompassToggler(player);
						toggler.setPlayerMode();
					}
					else
					{
						player.sendMessage(ChatColor.RED + ">> Error, " +  params[1] + " is not online ");
					}
				}
				else if (params[0].equalsIgnoreCase(AdvancedCompassParamCurrentLocation))
				{
					CompassPlayersInfo compassPlayersInfo = CompassPlayersInfo.getInstance();
					PlayerCompassData compassData = compassPlayersInfo.getPlayerCompassData(player.getName());
					
					compassData.setCurrentFixedModeLocation(player.getLocation());
					compassData.setCurrentCompassMode(CompassMode.Fixed);
					compassPlayersInfo.setPlayerCompassData(player.getName(), compassData);
					
					player.sendMessage(ChatColor.LIGHT_PURPLE + ">> Current fixed location set");
					CompassToggler toggler = new CompassToggler(player);
					toggler.setFixedMode();
				}
				else if (params[0].equalsIgnoreCase(AdvancedCompassParamHelp))
				{
					displayHelp(player);
				}
				else
				{
					player.sendMessage(ChatColor.RED + ">> Wrong command. Use /c help");
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
		player.sendMessage(ChatColor.LIGHT_PURPLE + "[ " + ChatColor.YELLOW + "player [playerName], current_location, help" + ChatColor.LIGHT_PURPLE + " ]");		
		player.sendMessage("  > " + ChatColor.YELLOW + "/c player [playerName]" + ChatColor.WHITE + ": "+ ChatColor.DARK_GREEN + " Compass pointing mode will point at this player");	
		player.sendMessage("  > " + ChatColor.YELLOW + "/c current_location" + ChatColor.WHITE + ": "+ ChatColor.DARK_GREEN + " Compass fixed mode will be fixed at player's location");	
		player.sendMessage("  > " + ChatColor.YELLOW + "/c help " + ChatColor.WHITE + ": "+ ChatColor.DARK_GREEN + "this blob.");		
	}
}
