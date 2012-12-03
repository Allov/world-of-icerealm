package ca.qc.icerealm.bukkit.plugins.perks;

import java.io.Serializable;

public class PersistedPerk implements Serializable {
	private static final long serialVersionUID = -6678555395964924682L;
	
	private String player;
	private String perkId;
	
	public String getPerkId() {
		return perkId;
	}
	public void setPerkId(String perkId) {
		this.perkId = perkId;
	}
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
}
