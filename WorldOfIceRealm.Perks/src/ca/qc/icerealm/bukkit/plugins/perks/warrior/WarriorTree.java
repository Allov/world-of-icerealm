package ca.qc.icerealm.bukkit.plugins.perks.warrior;

import ca.qc.icerealm.bukkit.plugins.perks.Perk;
import ca.qc.icerealm.bukkit.plugins.perks.PerkService;
import ca.qc.icerealm.bukkit.plugins.perks.PerkTree;

public class WarriorTree extends PerkTree {
	public WarriorTree() {
		super("Warrior");
	}

	public static final String MercenaryId = "mer";
	public static final String MeatShieldId = "ms";
	public static final String BerserkerId = "ber";
	public static final String LifeLeechId = "ll";
	public static final String LastManStandingId = "last";
	
	public static final Perk Mercenary = new Perk(	MercenaryId, 
													"Mercenary", 
													"Killed creatures will drop 20% more experience.", 
													5, 
													null, 
													null);
	
	public static final Perk MeatShield = new Perk(	MeatShieldId, 
													"Meat Shield", 
													"Your thick skin give you innate protection. All damage received is reduced by 1.", 
													10, 
													new String[] { MercenaryId }, 
													null);
	
	public static final Perk Berserker = new Perk(	BerserkerId, 
													"Berserker", 
													"Critical hits provide you with a strength buff for 5 seconds.", 
													15, 
													new String[] { MeatShieldId }, 
													null);
	
	public static final Perk LifeLeech = new Perk(	LifeLeechId, 
													"Life Leech", 
													"Every successful melee hit give you health regeneration for 1 seconds.", 
													30, 
													new String[] { BerserkerId }, 
													null);
	
	public static final Perk LastManStanding = new Perk(LastManStandingId, 
														"Last Man Standing", 
														"Reduce incoming damage by 60% for 10 seconds when health is below 50%. Cannot trigger more than once every minute.", 
														40, 
														new String[] { LifeLeechId }, 
														null);
	
	public static final Perk[] Perks = new Perk[] { Mercenary, MeatShield, Berserker, LifeLeech, LastManStanding };
	
	@Override
	public void initializePerks() {
		for(Perk perk : Perks) {
			getPerks().add(perk);
		}
	}
	 
}
