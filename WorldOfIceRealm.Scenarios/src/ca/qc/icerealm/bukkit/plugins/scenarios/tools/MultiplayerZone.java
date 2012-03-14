package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.List;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public abstract class MultiplayerZone implements ZoneObserver {

	private WorldZone _zone;
	private List<Player> _players; 
	
	@Override
	public void setWorldZone(WorldZone z) {
		_zone = z;
	}

	@Override
	public WorldZone getWorldZone() {
		return _zone;
	}

	@Override
	public void playerEntered(Player p) {
		if (!_players.contains(p)) {
			_players.add(p);
		}
	}
	
	public abstract void lastPlayerLeft(Player p);
	
	public int getNbPlayers() {
		return _players.size();
	}
	

	@Override
	public void playerLeft(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Server getCurrentServer() {
		// TODO Auto-generated method stub
		return null;
	}

}
