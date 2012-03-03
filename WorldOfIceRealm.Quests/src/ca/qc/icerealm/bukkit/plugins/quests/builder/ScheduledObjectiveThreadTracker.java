package ca.qc.icerealm.bukkit.plugins.quests.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import ca.qc.icerealm.bukkit.plugins.quests.CollectObjective;

public class ScheduledObjectiveThreadTracker {
	private static ScheduledObjectiveThreadTracker instance;
	
	public static ScheduledObjectiveThreadTracker getInstance() {
		if (instance == null) {
			instance = new ScheduledObjectiveThreadTracker();
		}
		
		return instance;
	}
	
	private Map<CollectObjective, ScheduledFuture<?>> scheduledThreads;

	public Map<CollectObjective, ScheduledFuture<?>> getScheduledThreads() {
		if (scheduledThreads == null) {
			scheduledThreads = new HashMap<CollectObjective, ScheduledFuture<?>>();
		}
		
		return scheduledThreads;
	}

	public void setScheduledThreads(Map<CollectObjective, ScheduledFuture<?>> scheduledThreads) {
		this.scheduledThreads = scheduledThreads;
	}
	
	public void stopAll() {
		for (ScheduledFuture<?> thread : getScheduledThreads().values()) {
			thread.cancel(false);
		}
	}
	
	public void stopObjectiveThread(CollectObjective objective) {
		ScheduledFuture<?> thread = getScheduledThreads().get(objective);
		
		if (thread != null) {
			thread.cancel(false);
		}
	}
}
