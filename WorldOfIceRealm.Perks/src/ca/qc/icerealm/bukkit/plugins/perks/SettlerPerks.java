package ca.qc.icerealm.bukkit.plugins.perks;

public class SettlerPerks {
	public static final String StoneWorkerId = "sw";
	public static final String WoodmanId = "wm";
	public static final String VassalId = "vas";
	public static final String GreenThumbId = "gt"; 

	public static final Perk StoneWorker = new Perk(StoneWorkerId, "Stone Worker", "Mining stone gives double cobble stone.", 5, null, WoodmanId);
	public static final Perk Woodman = new Perk(WoodmanId, "Woodman", "Cutting a tree will have all attached blocks destroyed instantly. All loot rules apply.", 5, null, StoneWorkerId);
	public static final Perk Vassal = new Perk(VassalId, "Vassal", "Upon harvesting, give one more of harvested resource.", 10, new String[] { WoodmanId, StoneWorkerId	}, GreenThumbId);
	public static final Perk GreenThumb = new Perk(GreenThumbId, "Green Thumb", "Planted crops have 20% chance to grow instantly.", 10, new String[] { WoodmanId, StoneWorkerId	}, VassalId);

	public static final Perk[] Perks = new Perk[] { StoneWorker, Woodman, Vassal, GreenThumb };
}
