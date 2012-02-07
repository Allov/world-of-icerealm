package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

class PlayerListener implements Listener {
	
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private ScenarioEngine _engine;
	
	public PlayerListener(ScenarioEngine e) {
		_engine = e;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		Player p = event.getPlayer();	
		Scenario s = _engine.findScenarioByPlayer(p);
		if (s != null) {
			// on enleve le joueur du scenario
			s.getPlayers().remove(p);
			
			if (s.getPlayers().size() == 0) {
				s.abortScenario();
				this.logger.info("The player disconnected and the scenario has been abort");
			}
			
			
		}
		
	}
	
	
	
}
