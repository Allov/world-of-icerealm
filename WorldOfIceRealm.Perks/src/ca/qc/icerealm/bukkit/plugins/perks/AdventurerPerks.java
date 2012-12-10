package ca.qc.icerealm.bukkit.plugins.perks;

public class AdventurerPerks {
	public static final String MercenaryId = "mer";
	public static final String ExplorerId = "exp";
	public static final String MeatShieldId = "ms";
	public static final String LightningReflexesId = "lr";
	public static final String BerserkerId = "ber";
	public static final String PoisonedArrowId = "pa";
	public static final String LifeLeechId = "ll";
	public static final String FindWeaknessId = "fw";
	public static final String LastManStandingId = "last";
	
	public static final Perk Mercenary = new Perk(MercenaryId, "Mercenary", "Killed creatures will drop 20% more experience.", 5, null, ExplorerId);
	public static final Perk Explorer = new Perk(ExplorerId, "Explorer", "Actions have a 50% chance to consume no energy at all.", 5, null, MercenaryId);
	public static final Perk MeatShield = new Perk(MeatShieldId, "Meat Shield", "Your thick skin give you innate protection. All damage received is reduced by 1.", 10, new String[] { ExplorerId, MercenaryId }, LightningReflexesId);
	public static final Perk LightningReflexes = new Perk(LightningReflexesId, "Lightning Reflexes", "You have 20% chance to completely dodge damage.", 10, new String[] { ExplorerId, MercenaryId }, MeatShieldId);
	public static final Perk Berserker = new Perk(BerserkerId, "Berserker", "Critical hits provide you with a strength buff for 5 seconds.", 15, new String[] { MeatShieldId, LightningReflexesId }, PoisonedArrowId);
	public static final Perk PoisonedArrow = new Perk(PoisonedArrowId, "Poisoned Arrow", "Apply a poison to your arrow that slows ennemy for 5 seconds.", 15, new String[] { MeatShieldId, LightningReflexesId }, BerserkerId);
	public static final Perk LifeLeech = new Perk(LifeLeechId, "Life Leech", "Every successful melee hit give you health regeneration for 1 seconds.", 15, new String[] { BerserkerId, PoisonedArrowId }, FindWeaknessId);
	public static final Perk FindWeakness = new Perk(FindWeaknessId, "Find Weakness", "Damage you do against an ennemy weakens his attacks for 5 seconds.", 15, new String[] { BerserkerId, PoisonedArrowId }, LifeLeechId);
	public static final Perk LastManStanding = new Perk(LastManStandingId, "Last Man Standing", "Reduce incoming damage by 60% for 10 seconds when health is below 50%. Cannot trigger more than once every minute.", 30, new String[] { FindWeaknessId, LifeLeechId }, null);
	
	public static final Perk[] Perks = new Perk[] { Mercenary, Explorer, MeatShield, LightningReflexes, Berserker, PoisonedArrow, LifeLeech, FindWeakness, LastManStanding };
	
	
	 
}
