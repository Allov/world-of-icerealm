package ca.qc.icerealm.bukkit.plugins.perks.magic;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import ca.qc.icerealm.bukkit.plugins.perks.magic.data.MagicDataService;
import ca.qc.icerealm.bukkit.plugins.perks.magic.data.PlayerMagicData;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class FoodLevelRegenerationObserver implements TimeObserver 
{
	public final Logger logger = Logger.getLogger(("Minecraft"));

	private long alarm;
	private boolean active = true;
	Server _server;

	public FoodLevelRegenerationObserver(Server server)
	{
		this._server = server;
	}
	
	@Override
	public void timeHasCome(long time) 
	{
		if (active)
		{
			// Loop into all online players and test if it should regenerate
			for (int i = 0; i < _server.getOnlinePlayers().length; i++)
			{
				MagicDataService magicDataService = MagicDataService.getInstance();
				Player p = _server.getOnlinePlayers()[i];
				PlayerMagicData data = magicDataService.getMagicData(p.getName());
				
				// If it should regenerate any time in the future
				if(data.getLastTimeRegenerated() != 0 && data.getFoodLevelsToRegen() > 0)
				{
					if (System.currentTimeMillis() - data.getLastTimeRegenerated() > getRegenerationInterval(p))
					{
						//p.sendMessage(ChatColor.RED + ">> regenerated");
						data.addFoodLevelsToRegen(-1);
						
						if (p.getFoodLevel() != 20)
						{
							p.setFoodLevel(p.getFoodLevel() + 1);
							data.setLastTimeRegenerated(System.currentTimeMillis());
						}
						else
						{
							data.setLastTimeRegenerated(0);
							data.setFoodLevelsToRegen(0);
						}
					}
				}
			}		
		}
		
		TimeServer.getInstance().addListener(this, 500);
	}
	
	@Override
	public void setAlaram(long time) 
	{
		this.alarm = time;
	}

	@Override
	public long getAlarm() 
	{
		return this.alarm;
	}
	
	private long getRegenerationInterval(Player p)
	{
		long regenerationInterval = 12000; // Base is 10 seconds
		PlayerInventory inventory = p.getInventory();
		ItemStack[] armorContents = inventory.getArmorContents();

		for(ItemStack armor : armorContents) 
		{
			if (armor.getType().equals(Material.GOLD_BOOTS)) 
			{
				regenerationInterval -= 2000;
			} 
			else if (armor.getType().equals(Material.GOLD_CHESTPLATE)) 
			{
				regenerationInterval -= 2000;
			} 
			else if (armor.getType().equals(Material.GOLD_HELMET)) 
			{
				regenerationInterval -= 2000;
			}
			else if (armor.getType().equals(Material.GOLD_LEGGINGS)) 
			{
				regenerationInterval -= 2000;
			}
		}
		
		// Should be 4 seconds if all 4 gold pieces
		return regenerationInterval;
	}
}
