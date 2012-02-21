package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.EntityWave;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.PlayerOutTimer;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioEventsListener;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.WaveTimer;
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
	private ScenarioEventsListener _eventsListener;
	private CommandExecutor _commander;
	private WaveTimer _waveTimer;
	private List<PlayerOutTimer> _playerOutTimers;

	// propriete modifiable de l'externe
	private long _timeoutWhenLeaving;
	private long _coolDownTime;
	private long _coolDownUntilTime;
	private int _nbMonsters;
	private long _timeBetween;
	private String _name;
	private int _experience;
	private int _minimumPlayer;
	private List<EntityWave> _waves;
	
	// propriete sur le status du scenario
	private boolean _coolDownActive;
	private boolean _isActive;
	private List<Player> _players;
	private int _nbWaveDone;
	private int _nbWavesTotal;
	
	/**
	 * Constructeur par défaut. Genere des BasicMonsterWave selon la config.
	 * @param plugin
	 * @param config
	 */
	public MonsterFury(JavaPlugin plugin, MonsterFuryConfiguration config) {
		initializeParameter(plugin, config, new DefaultEventListener());
		createBasicWaves();		
	}
	
	/**
	 * Genere des BasicMonsterWave et prend en parametre un event listenenr. Laisser le Event Listener
	 * a null pour n'avoir aucun feedback.
	 * @param plugin
	 * @param config
	 * @param event
	 */
	public MonsterFury(JavaPlugin plugin, MonsterFuryConfiguration config, ScenarioEventsListener event) {
		initializeParameter(plugin, config, event);
		createBasicWaves();
	}
	
	/**
	 * Prend en parametre la liste des waves qui seront joue dans le scenario. Le Event Listener par defaut.
	 * Il est possible de changer la liste de wave apres la creation de l'objet.
	 * est utilise.
	 * @param plugin
	 * @param config
	 * @param event
	 */
	public MonsterFury(JavaPlugin plugin, MonsterFuryConfiguration config, List<EntityWave> event) {
		initializeParameter(plugin, config, new DefaultEventListener());
		_waves = event;
	}
	
	/**
	 * Prend en parametre un event listener et la liste des waves. Il est possible de changer la liste apres la creation de l'objet.
	 * @param plugin
	 * @param config
	 * @param event
	 * @param wave
	 */
	public MonsterFury(JavaPlugin plugin, MonsterFuryConfiguration config, ScenarioEventsListener event, List<EntityWave> wave) {
		initializeParameter(plugin, config, event);
		_waves = wave;
	}
	
	private void createBasicWaves() {
		for (int i = 0; i < _nbWavesTotal; i++) {
			_waves.add(new BasicMonsterWave(_nbMonsters, 0.0, this, _scenarioZone, _activationZone, _eventsListener));
		}
	}

	
	private void initializeParameter(JavaPlugin p, MonsterFuryConfiguration c, ScenarioEventsListener l) {
		// parametre du scenario et status de celui-ci
		_plugin = p;
		_server = p.getServer();
		_world = _plugin.getServer().getWorld("world");
		_players = new ArrayList<Player>();
		_waves = new ArrayList<EntityWave>();
		_isActive = false;
		_coolDownActive = false;
		_nbWaveDone = 0;
		_playerOutTimers = new ArrayList<PlayerOutTimer>();
		
		// parametre modifiable
		_coolDownTime = c.CoolDownTime;
		_minimumPlayer = c.MinimumPlayer;
		_experience = c.ExperienceReward;
		_nbWavesTotal = c.NumberOfWaves;
		_nbMonsters = c.MonstersPerWave;
		_timeBetween = c.TimeBetweenWave;
		_timeoutWhenLeaving = c.TimeoutWhenLeaving;
		_name = c.Name;
		
		// activation des zones
		_activationZone = new WorldZone(_world, c.ActivationZoneCoords);
		_scenarioZone = new WorldZone(_world, c.ScenarioZoneCoords);

		// set la zone d'activation et enregistre le zone observer
		_activationZoneObserver = new ActivationZoneObserver(_plugin.getServer(), this);
		_activationZoneObserver.setWorldZone(_activationZone);
		ZoneServer.getInstance().addListener(_activationZoneObserver);
		
		// on veut ajouter les joueurs dans le scenario
		ZoneServer.getInstance().addListener(this);
				
		// set les listener bukkit
		_listener = new MonsterFuryListener(this);
		_plugin.getServer().getPluginManager().registerEvents(_listener, _plugin);
		
		// set le listener d'event
		_eventsListener = l;
	}
	
	/**
	 * Appelle les methode remove des different observers.
	 */
	public void removeAllListener() {
		ZoneServer.getInstance().removeListener(_activationZoneObserver);
		TimeServer.getInstance().removeListener(_coolDownTimer);
		ZoneServer.getInstance().removeListener(this);
		TimeServer.getInstance().removeListener(_waveTimer);
		
		for (EntityWave w : _waves) {
			w.cancelWave();
		}
		_waves.clear();
		_waves = null;
		
	}
	
	/**
	 * Demarre le scenario
	 */
	public void triggerScenario() {
		
		if (_waves != null && _waves.size() > 0) {
			// démarrage du cooldown
			setCoolDownActive(true);
							
			if (_eventsListener != null) {
				_eventsListener.scenarioStarting(_waves.size(), getPlayers());
			}

			// on active la premiere wave et le scénario
			_listener.setMonsterWave(_waves.get(_nbWaveDone));
			TimeServer.getInstance().addListener(new WaveTimer(_waves.get(_nbWaveDone), _eventsListener, this), _timeBetween);
			
			
			// on démarre le scénario
			_isActive = true;
		}
		
		
	}
	
	/**
	 * Termnine le scenario de facon normale
	 */
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
			
			getPlayers().clear();
			
			if (_waves != null) {
				for (EntityWave wave : _waves) {
					wave.cancelWave();
				}
			}
			
			_nbWaveDone = 0;	
			_isActive = false;
		}
		
	}
	
	/**
	 * Annulation inattendu du scenario. Il faut releaser les ress ici.
	 */
	public void abortScenario() {
		if (_isActive) {
			getPlayers().clear();
			
			if (_waves != null) {
				for (EntityWave wave : _waves) {
					wave.cancelWave();
				}
			}
		}
		
		_nbWaveDone = 0;
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
			_coolDownUntilTime = System.currentTimeMillis() + _coolDownTime;
			
			if (_eventsListener != null) {
				_eventsListener.coolDownChanged(active, _coolDownTime, this);
			}
		}
		else if (!_coolDownActive && !_isActive && _players.size() >= _minimumPlayer) {
			triggerScenario();
		}
	}
		
	/*
	 * Ajoute un joueur au scenario. Un joueur est ajoute s'il n'est pas contenu dans 
	 * la liste des joueurs.
	 */
	@Override
	public void addPlayerToScenario(Player p) {
		if (_players != null && p != null && !_players.contains(p)) {
			_players.add(p);
			if (_eventsListener != null) {
				_eventsListener.playerAdded(p, this);
			}
		}
	}
	
	/*
	 * Enleve un joueur au scenario.
	 */
	@Override
	public void removePlayerFromScenario(Player p) {
		// TODO Auto-generated method stub
		if (_players != null && p != null && _players.contains(p)) {
			_players.remove(p);
			if (_eventsListener != null) {
				_eventsListener.playerRemoved(p, this);
			}
			if (_players.size() == 0 && isActive()) {
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
	
	public int getMinimumPlayer() {
		return _minimumPlayer;
	}
	
	public List<EntityWave> getEntityWaves() {
		return _waves;
	}
	
	public void setEntityWaves(List<EntityWave> l) {
		_waves = l;
	}
	
	public void waveIsDone() {
		_nbWaveDone++;
		if (_nbWaveDone < _waves.size()) {	
			
			// on load la prochaine wave!
			if (_eventsListener != null) {
				_eventsListener.waveIsDone(_nbWaveDone);
			}
			
			_listener.setMonsterWave(_waves.get(_nbWaveDone));
			_waveTimer = new WaveTimer(_waves.get(_nbWaveDone), _eventsListener, this);
			TimeServer.getInstance().addListener(_waveTimer, _timeBetween);
		}
		else {
			terminateScenario();
		}
	}
	
	public ScenarioEventsListener getEventListener() {
		return _eventsListener;
	}
		
	@Override
	public void playerEntered(Player p) {
		
		boolean playerWasOutside = false;
		for (PlayerOutTimer t : _playerOutTimers) {
			if (t.getPlayer().getEntityId() == p.getEntityId()) {
				TimeServer.getInstance().removeListener(t);
				_playerOutTimers.remove(t);
				playerWasOutside = true;
				break;
			}
		}
		
		if (!playerWasOutside) {
			this.addPlayerToScenario(p);	
		}
		
	}

	@Override
	public void playerLeft(Player p) {
		
		if (_isActive) {
			PlayerOutTimer timer = new PlayerOutTimer(p, _scenarioZone, this);
			TimeServer.getInstance().addListener(timer, _timeoutWhenLeaving);
			if (_eventsListener != null) {
				_eventsListener.playerLeavingWithTimeout(p, _timeoutWhenLeaving);
			}
		}
		else {
			this.removePlayerFromScenario(p);
		}
			
	}

	@Override
	public Server getCurrentServer() {
		return _server;
	}
	
	public Server getScenarioServer() {
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

	@Override
	public Player pickRandomPlayer() {
		int i = RandomUtil.getRandomInt(getPlayersCount());
		if (i >= 0 && i < getPlayersCount()) {
			return getPlayers().get(i);
		}
		return null;
	}

	@Override
	public boolean isCooldownActive() {
		// TODO Auto-generated method stub
		return _coolDownActive;
	}

	@Override
	public WorldZone getScenarioZone() {
		return _scenarioZone;
	}

	@Override
	public long timeBeforeActivationPossible() {
		// TODO Auto-generated method stub
		if (_coolDownUntilTime - System.currentTimeMillis() > 0) {
			return _coolDownUntilTime - System.currentTimeMillis();
		}
		else {
			return 0;
		}
		
	}
}
