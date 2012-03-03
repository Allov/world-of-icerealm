package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.logging.Logger;
import java.util.zip.CheckedOutputStream;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class CollectObjective extends CountObjective implements Runnable, QuestListener {

	private final int materialId;
	private final boolean keep;
	private long alarm;

	public CollectObjective(Player player, WorldZone zone, String name, int amount, boolean keep, int materialId) {
		super(player, zone, name, amount);
		this.keep = keep;
		this.materialId = materialId;
	}
	
	private static final Object lock = new Object();
	
	public void checkForOwnedAmount() {
		int ownedAmount = checkForItemInPlayerInventory();
		
		if (isCompleted() && ownedAmount < getAmount()) {
			setCompleted(false);
		}
		
		if (ownedAmount - getCurrent() != 0) {
			synchronized(lock) {
				advance(ownedAmount - getCurrent());
			}
		}
	}

	public void questCompleted(Quest quest) {
		if (!this.keep) {
			MaterialData material = new MaterialData(materialId);
			this.getPlayer().getInventory().removeItem(new ItemStack(material.getItemType(), getAmount()));
		}
		
		quest.removeListener(this);
	}

	private int checkForItemInPlayerInventory() {
		Inventory inventory = getPlayer().getInventory();
		
		MaterialData material = new MaterialData(materialId);
		
		int amount = 0;
		
		for (ItemStack itemStack : inventory.all(material.getItemType()).values()) {
			amount += itemStack.getAmount();
		}
		
		return amount;
	}
	
	@Override
	protected void listenerAdded() {
		super.listenerAdded();
		
		checkForOwnedAmount();
	}

	@Override
	public String getType() {
		return "collect";
	}

	@Override
	public void questProgressed(Quest quest, Objective objective) {
	}

	@Override
	public void questReseted(Quest quest) {
		
	}

	@Override
	public void run() {
		checkForOwnedAmount();
	}
}
