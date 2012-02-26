package ca.qc.icerealm.bukkit.plugins.quests;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class ZoneObjective extends Objective implements ZoneObserver {
	
	private final Server server;

	public ZoneObjective(Player player, WorldZone zone, String name, Server server) {
		super(player, zone, name);
		this.server = server;	}

	@Override
	public String toString() {
		return  ChatColor.YELLOW + this.getName() +
				(isCompleted() ? ChatColor.GRAY + " (COMPLETED)" : ""); 
	}

	@Override
	public void setWorldZone(WorldZone zone) {
		zone = this.getZone();
	}

	@Override
	public WorldZone getWorldZone() {
		// TODO Auto-generated method stub
		return this.getZone();
	}

	@Override
	public void playerEntered(Player player) {
		if (player == this.getPlayer()) {
			this.setCompleted(true);
			this.objectiveProgressed();
			this.objectiveCompleted();
		}
	}

	@Override
	public void playerLeft(Player player) {
	}

	@Override
	public Server getCurrentServer() {
		return this.server;
	}
	
	@Override
	public void reset() {
		super.reset();
		
		ZoneServer.getInstance().addListener(this);
	}
	
	@Override
	protected void objectiveCompleted() {
		super.objectiveCompleted();
		
		ZoneServer.getInstance().removeListener(this);
	}

	@Override
	public String getType() {
		return "zone";
	}
}
