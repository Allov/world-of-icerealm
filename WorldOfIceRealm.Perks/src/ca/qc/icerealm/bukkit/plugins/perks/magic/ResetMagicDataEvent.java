package ca.qc.icerealm.bukkit.plugins.perks.magic;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import ca.qc.icerealm.bukkit.plugins.perks.magic.data.MagicDataService;

public class ResetMagicDataEvent implements Listener 
{
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent evt) 
	{
		Player player = evt.getEntity();
		MagicDataService.getInstance().resetMagicData(player);
	}
}