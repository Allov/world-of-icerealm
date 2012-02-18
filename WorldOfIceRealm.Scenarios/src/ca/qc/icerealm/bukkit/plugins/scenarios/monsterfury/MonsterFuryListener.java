package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import java.util.logging.Logger;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ca.qc.icerealm.bukkit.plugins.scenarios.core.EntityWave;

public class MonsterFuryListener implements Listener {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private EntityWave _currentWave;
	private MonsterFury _scenario;
	
	public MonsterFuryListener(MonsterFury s) {
		_scenario = s;
	}
	
	public void setMonsterWave(EntityWave wave) {
		_currentWave = wave;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		if (_scenario.isActive() && _currentWave != null) {
			Entity entity = event.getEntity();
			if (entity instanceof Player) {
				
				try {
					_scenario.getPlayers().remove((Player)entity);
					if (_scenario.getEventListener() != null) {
						_scenario.getEventListener().playerDied((Player)entity, _scenario.getPlayers());
					}
				}
				catch (Exception ex) { }
			}
			else {
				_currentWave.processEntityDeath(entity);
			}
		}
		
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onDisconnectPlayer(PlayerQuitEvent quit) {
		_scenario.removePlayerFromScenario(quit.getPlayer());
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
