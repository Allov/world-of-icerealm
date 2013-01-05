package ca.qc.icerealm.bukkit.plugins.perks;

import java.util.ArrayList;

public abstract class PerkTree {
	
	private ArrayList<Perk> perks = new ArrayList<Perk>();
	private final String name;
	
	public PerkTree(String name) {
		this.name = name;
		initializePerks();
	}
	
	public abstract void initializePerks();

	public ArrayList<Perk> getPerks() {
		return perks;
	}

	public String getName() {
		return name;
	}
}
