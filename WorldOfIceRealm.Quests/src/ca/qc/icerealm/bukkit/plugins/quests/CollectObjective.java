package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.logging.Logger;

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
	private final boolean keep;

	public CollectObjective(Player player, WorldZone zone, String name, int amount, boolean keep, int materialId) {
		super(player, zone, name, amount);
		this.keep = keep;
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
	
	@Override
	protected void questCompleted() {
		super.questCompleted();
		
		Logger.getLogger("Minecraft").info("Keep: " + this.keep);
		
		if (!this.keep) {
			this.getPlayer().getInventory().removeItem(new ItemStack(materialId, getAmount()));
		}
	}
}
