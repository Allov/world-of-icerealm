package ca.qc.icerealm.bukkit.plugins.fishingtournament;

import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;

public class TimeChecker implements TimeObserver {

	private final FishingTournament fishingTournament;
	private long alarm;

	public TimeChecker(FishingTournament fishingTournament) {
		this.fishingTournament = fishingTournament;
	}
	
	@Override
	public void timeHasCome(long time) {
		fishingTournament.progress();
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
