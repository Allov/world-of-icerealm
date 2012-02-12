package ca.qc.icerealm.bukkit.plugins.fishingtournament;

public class Catches {
	private int catches = 0;
	public int addCatch() {
		return catches++;
	}
	
	public int getCatches() {
		return catches;
	}
}
