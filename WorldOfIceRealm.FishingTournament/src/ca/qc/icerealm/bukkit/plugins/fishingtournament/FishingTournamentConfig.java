package ca.qc.icerealm.bukkit.plugins.fishingtournament;

import org.bukkit.configuration.file.FileConfiguration;

import ca.qc.icerealm.bukkit.plugins.common.ConfigWrapper;

public class FishingTournamentConfig {
	private final int starts;
	private final int span;
	private final int chances;
	private final int reward;
	private final int halfTime;
	private final int nearEnd;
	private final int end;
	private final int playerNumberNeeded;
	

	public FishingTournamentConfig(int starts, int span, int chances, int reward, int playerNumberNeeded) {
		this.starts = starts;
		this.span = span;
		this.chances = chances;
		this.reward = reward;
		this.playerNumberNeeded = playerNumberNeeded;
		
		this.halfTime = span / 2;
		this.nearEnd = span - 1;
		this.end = starts + span;
	}

	public static FishingTournamentConfig fromConfigurationFile(FileConfiguration configuration) {
		ConfigWrapper wrapper = new ConfigWrapper(configuration);
		
		return new FishingTournamentConfig(
				wrapper.getInt("starts", 0),
				wrapper.getInt("span", 6),
				wrapper.getInt("chances", 8),
				wrapper.getInt("reward", 300),
				wrapper.getInt("playerNumberNeeded", 2));
	}

	public int getStarts() {
		return starts;
	}

	public int getSpan() {
		return span;
	}

	public int getChances() {
		return chances;
	}

	public int getReward() {
		return reward;
	}

	public int getHalfTime() {
		return halfTime;
	}

	public int getNearEnd() {
		return nearEnd;
	}

	public int getEnd() {
		return end;
	}

	public int getPlayerNumberNeeded() {
		return playerNumberNeeded;
	}
}
