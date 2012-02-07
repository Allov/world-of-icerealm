package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;

public abstract class Objective {
	private WorldZone zone;
	private int entityId;	
	private int amount;
	private int current;
	private boolean requirementsFulfilled;
	private List<ObjectiveListener> listeners;
	private Player player;
	
	public Objective(Player player, WorldZone zone, int amount, int entityId) {
		this.player = player;
		this.zone = zone;
		this.amount = amount;
		this.entityId = entityId;
		
		this.listeners = new CopyOnWriteArrayList<ObjectiveListener>();
	}
	
	public boolean advance(Entity entity) {
		if (!requirementsFulfilled) {
			current++;
			
			objectiveProgressed(entity);
			
			if (current == amount) {
				requirementsFulfilled = true;
				objectiveDone();
			}
		}
		
		return requirementsFulfilled;
	}
	
	public void register(ObjectiveListener listener) {
		this.listeners.add(listener);
	}
	
	public void unregister(ObjectiveListener listener) {
		this.listeners.remove(listener);
	}
	
	private void objectiveProgressed(Entity entity) {
		for (ObjectiveListener listener : this.listeners) {
			listener.objectiveProgressed(this, entity);
		}
	}	

	private void objectiveDone() {
		for (ObjectiveListener listener : this.listeners) {
			listener.objectiveDone(this);
		}
	}

	public boolean isDone() {
		return requirementsFulfilled;
	}
	
	public int getEntityId() {
		return entityId;
	}
	
	public WorldZone getZone() {
		return zone;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public int getCurrent() {
		return current;
	}

	public Player getPlayer() {
		return player;
	}
}
