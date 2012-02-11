package ca.qc.icerealm.bukkit.plugins.race;

import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class BoatRace extends JavaPlugin implements TimeObserver, ZoneObserver {
	private final Logger logger = Logger.getLogger(("Minecraft"));
	private WorldZone _zone;
	private Server _server;
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		ZoneServer.getInstance().removeListener(this);
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		WorldZone zone = new WorldZone(getServer().getWorld("world"), "232,-685,238,-680,0,128");
		setWorldZone(zone);
		_server = this.getServer();
		ZoneServer.getInstance().addListener(this);
	}
	
	@Override
	public Server getCurrentServer() {
		return _server;
	}

	@Override
	public void setWorldZone(WorldZone z) {
		// TODO Auto-generated method stub
		_zone = z;
	}

	@Override
	public WorldZone getWorldZone() {
		// TODO Auto-generated method stub
		return _zone;
	}

	@Override
	public void playerEntered(Player p) {
		// TODO Auto-generated method stub
		this.logger.info(p.getName() + " entered the zone");
	}

	@Override
	public void playerLeft(Player p) {
		// TODO Auto-generated method stub
		//this.logger.info(p.getName() + " leaving the zone");
	}

	@Override
	public void timeHasCome(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAlaram(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getAlarm() {
		// TODO Auto-generated method stub
		return 0;
	}

}
