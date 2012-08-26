package ca.qc.icerealm.bukkit.plugins.SlashRoll;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


public class SlashRollCommandExecutor implements CommandExecutor {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private static final String RollCommandName = "roll";
	private static final int Min = 0;
	private static final int Max = 100;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandName, String[] params) {
		if (!commandName.equalsIgnoreCase(RollCommandName))
		{
			return false;
		}
		
		if(!(sender instanceof Player))
		{
			return false;
		}
		
		if(params == null)
		{
			return false;
		}
		
		if (IsStandardRoll(commandName,params)) {
			roll((Player)sender, Max);
			return true;
		}
		
		if(IsParametrisedRoll(commandName, params))
		{
			int paramValue = parseInt(params[0]);
			roll((Player)sender, paramValue);
			return true;
		}
	
		return false;
	}
	
	private boolean IsStandardRoll(String commandName, String[] params)
	{
		return params.length == 0;
	}
	
	private boolean IsParametrisedRoll(String commandName, String[] params)
	{
		return params.length > 0;
	}
	
	private int parseInt(String input) {
		int returnValue = -1;
		try {

			returnValue = Integer.parseInt(input);
        
        } catch (NumberFormatException ex) {
            return -1;
        }
        
        return returnValue;
	}

	private void roll(Player sender, int maxValue) {
		List<Entity> list = sender.getNearbyEntities(20, 20, 20);
		int rollValue = Min + (int)(Math.random() * ((maxValue - Min) + 1));
		
		String playerMessage = sender.getName() + " rolled " + rollValue;
		sender.sendMessage(ChatColor.GOLD + playerMessage);
				
		/*DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
		TweetFacade.UpdateStatus(dateFormat.format(date) + " - " + playerMessage);*/
		
		for (Entity t : list)
		{
			if(t instanceof Player)
			{
				Player recipient = (Player) t;
				String partyMessage = sender.getName() + " rolled " + rollValue;
				recipient.sendMessage(ChatColor.GOLD + partyMessage);
				//TweetFacade.UpdateStatus(dateFormat.format(date) + " - " + partyMessage);
				
			}
		}
	}

}