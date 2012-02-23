package ca.qc.icerealm.bukkit.plugins.SlashRoll;

import java.util.List;
import java.util.logging.Logger;

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
		if(sender instanceof Player){
			if (commandName.equalsIgnoreCase(RollCommandName)) {
				roll((Player)sender);
			}
			return true;
		}
		return false;
	}

	private void roll(Player sender) {
		List<Entity> list = sender.getNearbyEntities(20, 20, 20);
		int rollValue = Min + (int)(Math.random() * ((Max - Min) + 1));
		logger.warning(sender.getName() + " rolled " + rollValue);
		sender.sendMessage(sender.getName() + " rolled " + rollValue);
		for (Entity t : list)
		{
			if(t instanceof Player)
			{
				Player recipient = (Player) t;
				recipient.sendMessage(sender.getName() + " rolled " + rollValue);
				
			}
		}
	}

}