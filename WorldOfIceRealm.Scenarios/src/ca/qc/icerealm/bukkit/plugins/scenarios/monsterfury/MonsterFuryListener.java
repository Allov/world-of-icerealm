package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import java.util.logging.Logger;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class MonsterFuryListener implements Listener {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private MonsterWave _currentWave;
	private MonsterFury _scenario;
	
	public MonsterFuryListener(MonsterFury s) {
		_scenario = s;
	}
	
	public void setMonsterWave(MonsterWave wave) {
		if (_scenario.isActive()) {
			_currentWave = wave;
			_currentWave.spawnWave();	
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		
		if (_scenario.isActive()) {
			Entity entity = event.getEntity();
			if (entity instanceof Player) {
				try {
					_scenario.getPlayers().remove((Player)entity);	
				}
				catch (Exception ex) { }
			}
			else {
				_currentWave.processEntityDeath(entity);
			}
		}
		
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		if (_scenario.isActive()) {
			try {
				_currentWave.processDamage(event);	
			}
			catch (Exception ex) {
				this.logger.info("MonsterFury threw an exception on EntityDamageEvent");
			}
		}
		
		
	}
	
}
