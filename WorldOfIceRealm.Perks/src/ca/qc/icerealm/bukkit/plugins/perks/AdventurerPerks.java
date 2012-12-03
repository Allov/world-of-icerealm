package ca.qc.icerealm.bukkit.plugins.perks;

public class AdventurerPerks {
	public static final String MercenaryId = "mer";
	public static final String ExplorerId = "exp";
	public static final String MeatShieldId = "ms";
	public static final String LightningReflexesId = "lr";

	public static final Perk Mercenary = new Perk(MercenaryId, "Mercenary", "Killed creatures will drop 20% more experience.", 5, null, ExplorerId);
	public static final Perk Explorer = new Perk(ExplorerId, "Explorer", "Actions have a 50% chance to consume no energy at all.", 5, null, MercenaryId);
	public static final Perk MeatShield = new Perk(MeatShieldId, "Meat Shield", "Your thick skin give you innate protection. All damage received is reduced by 1.", 10, new String[] { ExplorerId, MercenaryId }, LightningReflexesId);
	public static final Perk LightningReflexes = new Perk(LightningReflexesId, "Lightning Reflexes", "You have 20% chance to completely dodge damage.", 10, new String[] { ExplorerId, MercenaryId }, MeatShieldId);
	
	public static final Perk[] Perks = new Perk[] { Mercenary, Explorer, MeatShield, LightningReflexes };
	 
}
