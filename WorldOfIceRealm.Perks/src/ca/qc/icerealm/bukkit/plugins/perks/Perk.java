package ca.qc.icerealm.bukkit.plugins.perks;

public class Perk {
	private final String id;
	private final String name;
	private final String description;
	private final int cost;
	private final String[] requires;
	private final String nemesis;
	
	public Perk(String id, String name, String description, int cost, String[] requires, String nemesis) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.cost = cost;
		this.requires = requires;
		this.nemesis = nemesis;
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public int getCost() {
		return cost;
	}
	public String[] getRequires() {
		return requires;
	}
	public String getNemesis() {
		return nemesis;
	}	
}
