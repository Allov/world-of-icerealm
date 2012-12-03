package ca.qc.icerealm.bukkit.plugins.perks;

public class PerkNotFoundException extends Exception {
	public PerkNotFoundException(String perkId) {
		super("Perk " + perkId + " doesn't exist.");
	}
}
