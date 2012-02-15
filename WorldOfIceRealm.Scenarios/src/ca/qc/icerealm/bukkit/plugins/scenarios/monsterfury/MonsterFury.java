package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class MonsterFury implements ZoneObserver, Scenario {
	public final Logger logger = Logger.getLogger(("Minecraft"));

	// objet de plus haut niveau
	private JavaPlugin _plugin;
	private World _world;
	private Server _server;
	
	// zone d'activation et celle du scenario
	private WorldZone _scenarioZone;
	private WorldZone _activationZone;
	
	// different Observer et Listener
	private CoolDownTimer _coolDownTimer;
	private ActivationZoneObserver _activationZoneObserver;
	private MonsterFuryListener _listener;
	private MonsterFuryEventsListener _eventsListener;

	// propriete modifiable de l'externe
	private long _coolDownTime;
	private int _nbMonsters;
	private long _timeBetween;
	private String _name;
	private int _experience;
	private int _minimumPlayer;
	private List<MonsterWave> _waves;
	
	// propriete sur le status du scenario
	private boolean _coolDownActive;
	private boolean _isActive;
	private List<Player> _players;
	private int _nbWaveDone;
	private int _nbWavesTotal;
	
	public MonsterFury(JavaPlugin plugin, MonsterFuryConfiguration config) {
		initializeParameter(plugin, config, new DefaultEventListener());
	}
	
	public MonsterFury(JavaPlugin plugin, MonsterFuryConfiguration config, MonsterFuryEventsListener event) {
		initializeParameter(plugin, config, event);
	}
	
	private void initializeParameter(JavaPlugin p, MonsterFuryConfiguration c, MonsterFuryEventsListener l) {
		// parametre du scenario et status de celui-ci
		_plugin = p;
		_server = p.getServer();
		_world = _plugin.getServer().getWorld("world");
		_players = new ArrayList<Player>();
		_waves = new ArrayList<MonsterWave>();
		_isActive = false;
		_coolDownActive = false;
		_nbWaveDone = 0;
		
		// parametre modifiable
		_coolDownTime = c.CoolDownTime;
		_minimumPlayer = c.MinimumPlayer;
		_experience = c.ExperienceReward;
		_nbWavesTotal = c.NumberOfWaves;
		_nbMonsters = c.MonstersPerWave;
		_timeBetween = c.TimeBetweenWave;
		_name = c.Name;
		
		// activation des zones
		_activationZone = new WorldZone(_world, c.ActivationZoneCoords);
		_scenarioZone = new WorldZone(_world, c.ScenarioZoneCoords);

		// set la zone d'activation et enregistre le zone observer
		_activationZoneObserver = new ActivationZoneObserver(_plugin.getServer(), this);
		_activationZoneObserver.setWorldZone(_activationZone);
		ZoneServer.getInstance().addListener(_activationZoneObserver);
				
		// set les listener bukkit
		_listener = new MonsterFuryListener(this);
		_plugin.getServer().getPluginManager().registerEvents(_listener, _plugin);
		
		// set le listener d'event
		_eventsListener = l;
	}
	
	public void removeAllListener() {
		ZoneServer.getInstance().removeListener(_activationZoneObserver);
		TimeServer.getInstance().removeListener(_coolDownTimer);
		ZoneServer.getInstance().removeListener(this);
	}
	
	public void triggerScenario() {

		for (int i = 0; i < _nbWavesTotal; i++) {
			_waves.add(new MonsterWave(_nbMonsters, 0.0, this, _scenarioZone, _activationZone));
		}
		
		// démarrage du cooldown
		_coolDownActive = true;
		_coolDownTimer = new CoolDownTimer(this);
		TimeServer.getInstance().addListener(_coolDownTimer, _coolDownTime);
		
		// on update la zone du scenario en enlevant la zone d'activation 
		ZoneServer.getInstance().addListener(this);
		this.logger.info("Scenario has been trigged");
				
		if (_eventsListener != null) {
			_eventsListener.scenarioStarting(_waves.size(), getPlayers());
		}

		// on active la premiere wave et le scénario
		_listener.setMonsterWave(_waves.get(_nbWaveDone));
		TimeServer.getInstance().addListener(new WaveTimer(_waves.get(_nbWaveDone), _eventsListener), _timeBetween);
		
		
		// on démarre le scénario
		_isActive = true;
	}
	
	public void terminateScenario() {
		if (_isActive) {
			if (_eventsListener != null) {
				_eventsListener.scenarioEnding(_nbWaveDone);
			}
			
			int xp = _experience / getPlayers().size();
			for (Player p : getPlayers()) {
				p.setLevel(p.getLevel() + xp);
				if (_eventsListener != null) {
					_eventsListener.playerRewared(p, xp);
				}
			}
			
			_nbWaveDone = 0;	
			_waves.clear();
			ZoneServer.getInstance().removeListener(this);
			_isActive = false;
		}
		
	}
	
	public void abortScenario() {
		this.logger.info("Monster fury aborted");
		if (_isActive) {
			getPlayers().clear();
			
			if (_waves != null) {
				for (MonsterWave wave : _waves) {
					wave.removeMonsters();
				}
				_waves.clear();
			}
		}
		
		_nbWaveDone = 0;
		ZoneServer.getInstance().removeListener(this);
		_isActive = false;
	}

	public boolean isCoolDownActive() {
		return _coolDownActive;
	}
	
	public boolean isActive() {
		return _isActive;
	}
	
	public void setCoolDownActive(boolean active) {
			
		_coolDownActive = active;

		// on doit enlever le listener et en créer un nouveau
		TimeServer.getInstance().removeListener(_coolDownTimer);
		
		if (_coolDownActive) {
			_coolDownTimer = new CoolDownTimer(this);
			TimeServer.getInstance().addListener(_coolDownTimer, _coolDownTime);
			
			if (_eventsListener != null) {
				_eventsListener.coolDownChanged(active, _coolDownTime);
			}
		}
		else if (!_coolDownActive && !_isActive && _players.size() >= _minimumPlayer) {
			triggerScenario();
		}
	}
		
	public void addPlayerToScenario(Player p) {
		if (_players != null && p != null && !_players.contains(p)) {
			_players.add(p);
			if (!_coolDownActive && _players.size() >= _minimumPlayer) {
				
				triggerScenario();
			}
		}
	}
	
	public void removePlayerFromScenario(Player p) {
		// TODO Auto-generated method stub
		if (_players != null && p != null && _players.contains(p)) {
			_players.remove(p);
			this.logger.info(p.getName() + " has been removed from the scenario");
			if (_players.size() == 0) {
				if (_eventsListener != null) {
					_eventsListener.scenarioAborting(_nbWaveDone, p);
				}
				abortScenario();
			}
		}
	}
	
	public int getPlayersCount() {
		return _players.size();
	}
	
	public List<Player> getPlayers() {
		return _players;
	}
	
	public World getWorld() {
		return _world;
	}
	
	public void waveIsDone() {
		_nbWaveDone++;
				
		if (_nbWaveDone < _waves.size()) {	
			
			// on load la prochaine wave!
			if (_eventsListener != null) {
				_eventsListener.waveIsDone(_nbWaveDone);
			}
			
			_listener.setMonsterWave(_waves.get(_nbWaveDone));
			TimeServer.getInstance().addListener(new WaveTimer(_waves.get(_nbWaveDone), _eventsListener), _timeBetween);
		}
		else {
			terminateScenario();
		}
	}
	
	public MonsterFuryEventsListener getEventListener() {
		return _eventsListener;
	}
		
	@Override
	public void playerEntered(Player p) {
		// TODO Auto-generated method stub
		if (_isActive) {
			this.addPlayerToScenario(p);	
		}
	}

	@Override
	public void playerLeft(Player p) {
		if (_isActive) {
			this.removePlayerFromScenario(p);	
		}
	}

	@Override
	public Server getCurrentServer() {
		return _server;
	}
	
	@Override
	public void setWorldZone(WorldZone z) {
		_scenarioZone = z;
	}

	@Override
	public WorldZone getWorldZone() {
		return _scenarioZone;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return _name;
	}
}
