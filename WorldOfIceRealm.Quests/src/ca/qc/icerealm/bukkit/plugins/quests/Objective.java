package ca.qc.icerealm.bukkit.plugins.quests;

import org.bukkit.entity.Entity;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.Zone;

public abstract class Objective {
	private Zone zone;
	private int entityId;	
	private int amount;
	private int current;
	private boolean requirementsFulfilled;
	private final Quest quest;
	
	public Objective(Quest quest, Zone zone, int amount, int entityId) {
		this.quest = quest;
		this.zone = zone;
		this.amount = amount;
		this.entityId = entityId;		
	}
	
	public boolean advance(Entity entity) {
		if (!requirementsFulfilled) {
			current++;
			
			getQuest().objectiveProgressed(this, entity);
			
			if (current == amount) {
				requirementsFulfilled = true;
				getQuest().objectiveFulfilled(this, entity);
			}
		}
		
		return requirementsFulfilled;
	}
	
	public boolean isDone() {
		return requirementsFulfilled;
	}
	
	public int getEntityId() {
		return entityId;
	}
	
	public Zone getZone() {
		return zone;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public int getCurrent() {
		return current;
	}

	public Quest getQuest() {
		return quest;
	}
}
