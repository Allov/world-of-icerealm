package ca.qc.icerealm.bukkit.plugins.perks;

public class RequirePerkException extends Exception {

	public RequirePerkException(String perkId, String[] requiredPerks) {
		super("You cannot purchase perk '" + perkId + "' until you either purchase one of these perk: " + getPerks(requiredPerks));
	}

	private static String getPerks(String[] requiredPerks) {
		String message = "";
		
		int i = 0;
		for(String perkId : requiredPerks) {
			i++;
			message += perkId + (i < requiredPerks.length ? ", " : "");			
		}
		
		return message;
	}
	
}
