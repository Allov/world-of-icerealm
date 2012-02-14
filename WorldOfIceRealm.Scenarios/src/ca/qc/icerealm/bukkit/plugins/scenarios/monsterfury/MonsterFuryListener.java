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
		_currentWave = wave;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		logger.info("processing death");
		if (_scenario.isActive() && _currentWave != null) {
			Entity entity = event.getEntity();
			if (entity instanceof Player) {
				try {
					_scenario.getPlayers().remove((Player)entity);	
				}
				catch (Exception ex) { }
			}
			else {
				logger.info("processing death");
				_currentWave.processEntityDeath(entity);
			}
		}
		
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		if (_scenario.isActive() && _currentWave != null) {
			try {
				_currentWave.processDamage(event);	
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		
	}
	
}
