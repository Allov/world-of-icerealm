package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;

public abstract class Objective {
	private WorldZone zone;
	private boolean completed;
	private Player player;
	private String name;

	protected List<ObjectiveListener> listeners;
	
	public Objective(Player player, WorldZone zone, String name) {
		this.player = player;
		this.zone = zone;
		this.setName(name);
		
		this.listeners = new CopyOnWriteArrayList<ObjectiveListener>();
	}
	
	public void register(ObjectiveListener listener) {
		this.listeners.add(listener);
		listenerAdded();
	}
	
	public void unregister(ObjectiveListener listener) {
		this.listeners.remove(listener);
	}
	
	protected void objectiveProgressed() {
		for (ObjectiveListener listener : this.listeners) {
			listener.objectiveProgressed(this);
		}
	}	

	protected void objectiveCompleted() {
		for (ObjectiveListener listener : this.listeners) {
			listener.objectiveCompleted(this);
		}
	}
	
	public WorldZone getZone() {
		return zone;
	}
	
	public Player getPlayer() {
		return player;
	}

	public boolean isCompleted() {
		return completed;
	}

	protected void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void reset() {
		this.setCompleted(false);
	}
	
	protected void listenerAdded() {
		
	}

	public abstract String getType();
}
