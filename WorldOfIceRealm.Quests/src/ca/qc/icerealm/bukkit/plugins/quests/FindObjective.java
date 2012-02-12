package ca.qc.icerealm.bukkit.plugins.quests;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;

public class FindObjective extends CountObjective implements Listener {

	private final int materialId;

	public FindObjective(Player player, WorldZone zone, String name, int amount, int materialId) {
		super(player, zone, name, amount);
		this.materialId = materialId;		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockDestory(BlockBreakEvent event) {
		if (getZone() != null) {
			if (!getZone().isInside(event.getPlayer().getLocation())) {
				return;
			}
		}
		
		if (event.getBlock().getTypeId() == materialId && event.getPlayer() == getPlayer()) {
			advance();
		}
	}
}
