package ca.qc.icerealm.bukkit.plugins.quests;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;

public class CollectObjective extends CountObjective implements Listener {

	private final int materialId;

	public CollectObjective(Player player, WorldZone zone, String name, int amount, int materialId) {
		super(player, zone, name, amount);
		this.materialId = materialId;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onItemCollect(PlayerPickupItemEvent event) {
		ItemStack stack = event.getItem().getItemStack();
		if (stack.getTypeId() == materialId) {
			advance(stack.getAmount());			
		}		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onItemDrop(PlayerDropItemEvent event) {
		ItemStack stack = event.getItemDrop().getItemStack();
		if (stack.getTypeId() == materialId) {
			regress(stack.getAmount());
		}
	}
}
