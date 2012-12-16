package ca.qc.icerealm.bukkit.plugins.perks;

import java.util.ArrayList;

public abstract class PerkTree {
	
	private ArrayList<Perk> perks = new ArrayList<Perk>();
	
	public PerkTree() {
		initializePerks();
	}
	
	public abstract void initializePerks();

	public ArrayList<Perk> getPerks() {
		return perks;
	}
}
