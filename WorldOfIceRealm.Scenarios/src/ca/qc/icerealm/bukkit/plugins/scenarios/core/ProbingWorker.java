package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;


class ProbingWorker implements Runnable {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private Server _server;
	private long _interval;
	private ScenarioEngine _engine;
	private boolean _stop = false;
	
	public ProbingWorker(Server s, ScenarioEngine engine, long interval) {
		_server = s;
		_engine = engine;
		_interval = interval;
	}
	
	@Override
	public void run() {
		
		while (!_stop) {
			
			for (Player p : _server.getOnlinePlayers()){
				
				Scenario s = _engine.findScenarioAtLocation(p.getLocation());			
				
				if (s != null && !s.getPlayers().contains(p) && s.canBeTriggered()) { // le joueur est a l'interieur d'une zone
					s.addPlayer(p);
					
					if (!s.isTriggered() && s.canBeTriggered()) {
						s.triggerScenario();
					}
				}
				else if (s == null) { // le joueur est a l'exterieur, on check s'il est présent dans un scénario
					Scenario scenario  = _engine.findScenarioByPlayer(p);
					if (scenario != null && scenario.abortWhenLeaving()) {
						scenario.getPlayers().remove(p);
						
						if (scenario.getPlayers().size() == 0) {
							scenario.abortScenario();
						}
					}
				}
			}
			
			try {
				Thread.sleep(_interval);
			} 
			catch (InterruptedException e) {
				this.logger.warning("Scenario Probing Worker could not fall asleep");
				stopProbing();
				_engine.shutdownScenarioEngine();
			}
		}
	}
	
	public void stopProbing() {
		_stop = true;
	}

}
