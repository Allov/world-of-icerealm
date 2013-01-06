package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.mobcontrol.MovementMobController;

public class MonsterLeach implements Runnable {
	
	private Collection<Monster> _entities;
	private WorldZone _zone;
	private MovementMobController _movement;
		
	public MonsterLeach(Collection<Monster> list, WorldZone zone) {
		_entities = list;
		_zone = zone;
		_movement = new MovementMobController();
	}

	@Override
	public void run() {
		
		Player p = Bukkit.getServer().getPlayer("punisher_malade");
		
		
		for (Monster e : _entities) {
			
			if (!e.isDead() && !_zone.isInside(e.getLocation())) {
				Location l = _zone.getRandomLocation(_zone.getWorld());
				_movement.moveEntityToLocation(e, l);
				if (p != null) {
					p.sendMessage(ChatColor.GRAY + "DEBUG: " + ChatColor.WHITE + e.getType() + " moving to " + l.getX() + ", " + l.getY() + ", " + l.getZ());
				}
			}
		}
		
	}
	

}
