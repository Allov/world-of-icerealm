package ca.qc.icerealm.bukkit.plugins.perks.archer;

import ca.qc.icerealm.bukkit.plugins.perks.Perk;
import ca.qc.icerealm.bukkit.plugins.perks.PerkTree;

public class ArcherTree extends PerkTree {

	public static final String ExplorerId = "exp";
	public static final String LightningReflexesId = "lr";
	public static final String PoisonedArrowId = "pa";
	public static final String FindWeaknessId = "fw";
	public static final String WindRunId = "wr";

	public static final Perk Explorer = new Perk(	ExplorerId, 
													"Explorer", 
													"Actions have a 50% chance to consume no energy at all.", 
													5, 
													null, 
													null);

	public static final Perk PoisonedArrow = new Perk(	PoisonedArrowId, 
														"Poisoned Arrow", 
														"Apply a poison to your arrow that slows ennemy for 5 seconds.", 
														10, 
														new String[] { ExplorerId },
														null);
	
	public static final Perk LightningReflexes = new Perk(	LightningReflexesId, 
															"Lightning Reflexes", 
															"You have 20% chance to completely dodge damage.", 
															15, 
															new String[] { PoisonedArrowId }, 
															null);
	
	public static final Perk FindWeakness = new Perk(	FindWeaknessId, 
														"Find Weakness", 
														"Damage you do against an ennemy weakens his attacks for 5 seconds.", 
														30, 
														new String[] { LightningReflexesId }, 
														null);

	public static final Perk WindRun = new Perk(WindRunId, 
												"Wind Run", 
												"Upon getting hit, you gain Switfness for 10 seconds. Your reflexes are greatly enhanced boosting your dodge chance to 90%. Cannot trigger more than once every minute.", 
												40, 
												new String[] { FindWeaknessId }, 
												null);

	public static final Perk[] Perks = new Perk[] {
		Explorer,
		PoisonedArrow,
		LightningReflexes,
		FindWeakness,
		WindRun
	};
	
	@Override
	public void initializePerks() {
		for(Perk perk : Perks) {
			getPerks().add(perk);
		}
	}
}
