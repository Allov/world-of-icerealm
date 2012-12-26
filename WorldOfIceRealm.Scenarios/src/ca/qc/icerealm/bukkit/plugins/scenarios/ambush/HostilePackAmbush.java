package ca.qc.icerealm.bukkit.plugins.scenarios.ambush;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.BaseEvent;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.HostilePack;

public class HostilePackAmbush extends MercenaryAmbush {

	private Player _player;
	private int _radius;
	
	public HostilePackAmbush(int radius, Player player, int numberMonster,	String monsters) {
		super(radius, player, numberMonster, monsters);
		_radius = radius;
		_player = player;
	}
	
	@Override
	public void run() {
		HostilePack pack = new HostilePack();
		WorldZone zone = new WorldZone(_player.getLocation(), _radius);
		Location sourceEvent = zone.getRandomLocation(zone.getWorld());
		pack.setSource(sourceEvent);
		pack.addTarget(_player);
		pack.activateEvent();
	}

}


