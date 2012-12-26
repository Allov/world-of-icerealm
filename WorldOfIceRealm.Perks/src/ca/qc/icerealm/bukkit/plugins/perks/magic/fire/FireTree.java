package ca.qc.icerealm.bukkit.plugins.perks.magic.fire;

import ca.qc.icerealm.bukkit.plugins.perks.Perk;
import ca.qc.icerealm.bukkit.plugins.perks.PerkTree;

public class FireTree extends PerkTree {
	public static final String FireballId = "fb";
	public static final String BigFireballId = "bfb";

	public static final Perk Fireball = new Perk(	FireballId, 
													"Fireball", 
													"Ability to throw small fireballs (Fire Rod is required)", 
													5, 
													null, 
													null);

	public static final Perk BigFireball = new Perk(	BigFireballId, 
														"Big Fireball", 
														"Ability to throw big exploding fireballs (Fire Rod is required)", 
														10, 
														new String[] { FireballId },
														null);

	public static final Perk[] Perks = new Perk[] {
		Fireball,
		BigFireball
	};
	
	@Override
	public void initializePerks() {
		for(Perk perk : Perks) {
			getPerks().add(perk);
		}
	}
}
