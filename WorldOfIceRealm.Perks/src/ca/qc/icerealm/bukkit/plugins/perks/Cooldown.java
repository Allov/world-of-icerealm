package ca.qc.icerealm.bukkit.plugins.perks;

import java.util.Calendar;
import java.util.logging.Logger;

public class Cooldown {
	private final long duration;
	private long startTime;

	public Cooldown(long duration) {
		this.duration = duration;
	}
	
	public void start() {
		if (!isOnCooldown()) {
			startTime = Calendar.getInstance().getTimeInMillis();
		}
	}
	
	public boolean isOnCooldown() {
		long currentTime = Calendar.getInstance().getTimeInMillis();
		return currentTime - startTime <= duration;
	}
}
