package ca.qc.icerealm.bukkit.plugins.perks;

public class NotEnoughLevelExpcetion extends Exception {
	public NotEnoughLevelExpcetion(String perkId, int cost) {
		super("Perk '" + perkId + "' requires " + cost + " to be purchased."); 
	}
}
