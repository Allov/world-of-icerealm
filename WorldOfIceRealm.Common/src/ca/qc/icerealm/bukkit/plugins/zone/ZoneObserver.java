package ca.qc.icerealm.bukkit.plugins.zone;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;

public interface ZoneObserver {
	void setWorldZone(WorldZone z);
	WorldZone getWorldZone();
	void playerEntered(Player p);
	void playerLeft(Player p);
	Server getCurrentServer();
}
