package ca.qc.icerealm.bukkit.plugins.quests;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class CollectObjective extends CountObjective implements Listener, TimeObserver {

	private final int materialId;
	private final boolean keep;
	private long alarm;

	public CollectObjective(Player player, WorldZone zone, String name, int amount, boolean keep, int materialId) {
		super(player, zone, name, amount);
		this.keep = keep;
		this.materialId = materialId;
	}
	
	public void checkForOwnedAmount() {
		int ownedAmount = checkForItemInPlayerInventory();
		if (ownedAmount > 0) {
			advance(ownedAmount);
		}
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
			MaterialData material = new MaterialData(materialId);
			this.getPlayer().getInventory().removeItem(new ItemStack(material.getItemType(), getAmount()));
		}
	}
	
	@Override
	protected void objectiveCompleted() {
		if (checkForItemInPlayerInventory() == getAmount()) {
			super.objectiveCompleted();			
		} else {
			// TODO: Find a better solution than a timer;
			TimeServer.getInstance().addListener(this, 100);
		}			
	}

	private int checkForItemInPlayerInventory() {
		Inventory inventory = getPlayer().getInventory();
		
		MaterialData material = new MaterialData(materialId);
		
		int amount = 0;
		int slot = inventory.first(material.getItemType());
		
		if (slot > -1) {
			amount = inventory.getItem(slot).getAmount();
		}
		
		return amount;
	}
	
	@Override
	protected void listenerAdded() {
		super.listenerAdded();
		
		int ownedAmount = checkForItemInPlayerInventory();
		if (ownedAmount > 0) {
			advance(ownedAmount);
		}
	}

	@Override
	public void timeHasCome(long time) {
		super.objectiveCompleted();
	}

	@Override
	public void setAlaram(long time) {
		this.alarm = time;
	}

	@Override
	public long getAlarm() {
		return this.alarm;
	}
}
