package ca.qc.icerealm.bukkit.plugins.fishingtournament;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class FishingTournamentPlugin extends JavaPlugin {

	private FishingTournament fishingTournament;
	private ScheduledFuture<?> fishingTournamentThread;

	@Override
	public void onDisable() {
		if (fishingTournamentThread != null) {
			fishingTournamentThread.cancel(false);
		}
	}

	@Override
	public void onEnable() {
		fishingTournament = new FishingTournament(this);
		fishingTournamentThread = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(fishingTournament, 1, 1, TimeUnit.SECONDS);
	}
}
