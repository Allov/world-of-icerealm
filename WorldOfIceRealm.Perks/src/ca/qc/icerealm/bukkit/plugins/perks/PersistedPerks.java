package ca.qc.icerealm.bukkit.plugins.perks;

import java.io.Serializable;
import java.util.List;

public class PersistedPerks implements Serializable {
	private static final long serialVersionUID = -1353266158729787925L;
	
	private List<PersistedPerk> perks;

	public List<PersistedPerk> getPerks() {
		return perks;
	}

	public void setPerks(List<PersistedPerk> perks) {
		this.perks = perks;
	}
	
}
