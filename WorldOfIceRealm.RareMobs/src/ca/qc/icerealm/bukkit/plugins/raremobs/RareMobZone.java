package ca.qc.icerealm.bukkit.plugins.raremobs;

import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.raremobs.data.CurrentRareMob;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class RareMobZone implements ZoneObserver
{
	private final Logger logger = Logger.getLogger(("Minecraft"));
	private WorldZone _world;
	private Server _server;
	private int _playerPresent;
	
	public RareMobZone(Server s) 
	{
		_server = s;
		_playerPresent = 0;
	}

	@Override
	public void setWorldZone(WorldZone z) 
	{
		_world = z;
	}

	@Override
	public WorldZone getWorldZone() 
	{
		return _world;
	}

	@Override
	public void playerEntered(Player p) 
	{
		this.logger.info("you entering in the raremob zone");
		logger.info("attempt to spawn mob");
		
		CurrentRareMob current = CurrentRareMob.getInstance();
		
		RareMobSpawner spawner = new RareMobSpawner(_server, current.getRareMob());
		spawner.spawnMobWithSubordinates(current.getRareMobLocation());
		ZoneServer.getInstance().removeListener(this);
	}

	@Override
	public void playerLeft(Player p) 
	{
		_playerPresent--;
	}

	@Override
	public Server getCurrentServer() 
	{
		return _server;
	}
}
