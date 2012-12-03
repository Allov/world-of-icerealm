package ca.qc.icerealm.bukkit.plugins.perks;

public class CannotPurchaseNemesisPerkException extends Exception {
	public CannotPurchaseNemesisPerkException(String perkId, String nemesisPerkId) {
		super("Perk '" + perkId + "' cannot be purchased because you already have perk '" + nemesisPerkId + "'");
	}
}
