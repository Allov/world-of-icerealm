package ca.qc.icerealm.bukkit.plugins.simplemobs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.simplemobs.SimpleMobsFactory;
import ca.qc.icerealm.bukkit.plugins.simplemobs.data.SimpleMob;
import ca.qc.icerealm.bukkit.plugins.simplemobs.data.SimpleMobsDataProvider;

public class SimpleMobsCommandExecutor implements CommandExecutor 
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandName, String[] params) 
	{	
		if (sender instanceof Player && commandName.equalsIgnoreCase("mobs")) 
		{		
			if (params.length > 0)
			{
				SimpleMob[] mobs = null;
				
				if (!params[0].equalsIgnoreCase("r"))
				{

					SimpleMobsFactory mobsFactory = new SimpleMobsFactory();
					
					try
					{
						mobs = mobsFactory.BuildSimpleMobs(params);
						SimpleMobsDataProvider.getInstance().SaveLastSimpleMobsData(mobs);
					}
					catch(ArrayIndexOutOfBoundsException aiobe)
					{
						sender.sendMessage("One parameter is missing");
					}
					catch(NumberFormatException nfe)
					{
						sender.sendMessage("First parameter must be a number");
					}
					catch(UnknownEntityException e)
					{
						sender.sendMessage(e.getMessage());
					}
				}
				else
				{
					mobs = SimpleMobsDataProvider.getInstance().LoadLastSimpleMobsData();
					
					if (mobs == null)
					{
						sender.sendMessage("You must use the SimpleMobs at least once");
					}
				}
				
				if (mobs != null)
				{
					SimpleMobsSpawner spawner = new SimpleMobsSpawner(((Player)sender), mobs);
					spawner.spawnMobs();
				}
			}
			else
			{
				sender.sendMessage("Usage: /mobs {Amount} {EntityType}[,]{Amount} {EntityType} [,][...]");
				sender.sendMessage("Example: /mobs 3 ZOMBIE, 1 SKELETON");
				sender.sendMessage("How to repeat last command: /mobs r");
			}
		}
		
		return true;
	}

}
