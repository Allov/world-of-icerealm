package ca.qc.icerealm.bukkit.plugins.fishingtournament;

import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class FishingTournamentPlugin extends JavaPlugin {

	private FishingTournament fishingTournament;

	@Override
	public void onDisable() {
		TimeServer.getInstance().removeListener(fishingTournament);
	}

	@Override
	public void onEnable() {
		fishingTournament = new FishingTournament(this);
		fishingTournament.start();
	}
}
