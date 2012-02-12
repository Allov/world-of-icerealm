package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;

public class CollectObjective extends CountObjective implements Listener {

	private final int materialId;
	private final boolean keep;
	private boolean questIsComplete = false;

	public CollectObjective(Player player, WorldZone zone, String name, int amount, boolean keep, int materialId) {
		super(player, zone, name, amount);
		this.keep = keep;
		this.materialId = materialId;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onItemCollect(PlayerPickupItemEvent event) {
		if (getZone() != null) {
			if (!getZone().isInside(event.getPlayer().getLocation())) {
				return;
			}
		}
		
		ItemStack stack = event.getItem().getItemStack();
		if (stack.getTypeId() == materialId && event.getPlayer() == getPlayer()) {
			advance(stack.getAmount());
			
			if (questIsComplete) {
				Logger.getLogger("Minecraft").info("Amount: " + (stack.getAmount() > getAmount() ? stack.getAmount() - getAmount() : 0));
				stack.setAmount(stack.getAmount() > getAmount() ? stack.getAmount() - getAmount() : 0);
			}
		}		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onItemDrop(PlayerDropItemEvent event) {
		ItemStack stack = event.getItemDrop().getItemStack();
		if (stack.getTypeId() == materialId && event.getPlayer() == getPlayer()) {
			regress(stack.getAmount());
		}
	}
	
	@Override
	protected void questCompleted() {
		super.questCompleted();
		
		if (!this.keep) {
			questIsComplete = true;
			Logger.getLogger("Minecraft").info("Keep: " + this.keep + " materialId: " + materialId + " amount: " + getAmount());
			MaterialData material = new MaterialData(materialId);
			this.getPlayer().getInventory().removeItem(new ItemStack(material.getItemType(), getAmount()));
		}
	}
}
