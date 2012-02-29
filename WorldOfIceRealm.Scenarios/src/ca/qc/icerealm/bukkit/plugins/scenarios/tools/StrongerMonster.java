package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class StrongerMonster implements Listener {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private Location _loc;
	private CreatureType _type;
	private int _max;
	private int _entityId;
	
	public StrongerMonster(Monster m, int max) {
		this.logger.info("StrongerMonster class");
		_max = max;
		_entityId = m.getEntityId();
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMonsterDamage(EntityDamageEvent e) {
		if (_entityId == e.getEntity().getEntityId()) {
			this.logger.info(String.valueOf(e.getDamage()));
			
		}
	}
	
	
}
