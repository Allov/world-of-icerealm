package ca.qc.icerealm.bukkit.plugins.publicwork;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class ShovelingZone implements ZoneObserver {

	private Server _server;
	private WorldZone _zone;
	
	public ShovelingZone(Server s, WorldZone z) {
		_server = s;
		_zone = z;
	}
	
	@Override
	public Server getCurrentServer() {
		return _server;
	}

	@Override
	public WorldZone getWorldZone() {
		return _zone;
	}

	@Override
	public void playerEntered(Player p) {
		p.sendMessage(ChatColor.GREEN + "You are inside a shoveling zone! Dig it!");
	}

	@Override
	public void playerLeft(Player p) {
		
		
	}

	@Override
	public void setWorldZone(WorldZone arg0) {
		_zone = arg0;
	}
	

}
