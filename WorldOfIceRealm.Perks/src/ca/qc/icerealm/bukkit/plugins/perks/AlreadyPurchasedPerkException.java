package ca.qc.icerealm.bukkit.plugins.perks;

public class AlreadyPurchasedPerkException extends Exception {
	public AlreadyPurchasedPerkException(String perkId) {
		super("Already purchased '" + perkId + "' perk.");
	}
}
