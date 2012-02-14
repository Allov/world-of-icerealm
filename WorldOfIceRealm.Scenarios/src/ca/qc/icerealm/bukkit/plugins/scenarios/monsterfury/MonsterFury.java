package ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.SimpleTimer;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class MonsterFury implements ZoneObserver {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	/*
	private int _minimumPlayerCount = 1;
	private boolean _active = false;
	private MonsterFuryListener _listener;
	private List<MonsterWave> _waves;
	private int nbWaveDone = 0;
	private long _coolDown = 30000;
	private long _lastRun = 0;
	private int _nbWave = 3;
	private int _nbMonsters = 3;
	private int _experience = 0;
	private int _money;
	private double _damageModifier = 0.0;
	private double _armorIncrement = 0.0;
	private long timeBetweenWave = 15000;
	private String _greater; 
	private WorldZone _greaterZone;
	private WorldZone _activationZone;
	*/
	
	private JavaPlugin _plugin;
	private World _world;
	private WorldZone _scenarioZone;
	private Server _server;
	private List<Player> _players;
	private boolean _coolDownActive;
	private boolean _isActive;
	private long _coolDownTime;
	private CoolDownTimer _coolDownTimer;
	private ActivationZoneObserver _activationZoneObserver;
	private MonsterFuryListener _listener;
	private int _minimumPlayer;
	private WorldZone _activationZone;
	private int _experience;
	private int _nbWaveDone;
	private List<MonsterWave> _waves;
	private int _nbWavesTotal;
	private int _nbMonsters;
	private SimpleTimer _waveDelay;
	private long _timeBetween;
	
	public MonsterFury(JavaPlugin plugin) {
		// parametre du scenario
		_plugin = plugin;
		_server = plugin.getServer();
		_world = _plugin.getServer().getWorld("world");
		_players = new ArrayList<Player>();
		_isActive = false;
		_coolDownActive = false;
		_coolDownTime = 10000;
		_minimumPlayer = 1;
		_activationZone = new WorldZone(_world, "-180,134,-177,137,0,128");
		_scenarioZone = new WorldZone(_world, "-189,127,-168,140,0,128");
		_experience = 100;
		_nbWaveDone = 0;
		_waves = new ArrayList<MonsterWave>();
		_nbWavesTotal = 2;
		_nbMonsters = 3;
		
		// set la zone d'activation et enregistre le zone observer
		_activationZoneObserver = new ActivationZoneObserver(plugin.getServer(), this);
		_activationZoneObserver.setWorldZone(_activationZone);
		ZoneServer.getInstance().addListener(_activationZoneObserver);
		
		// set les listener
		plugin.getServer().getPluginManager().registerEvents(_listener, plugin);
		
		// set les waves
		for (int i = 0; i < _nbWavesTotal; i++) {
			_waves.add(new MonsterWave(_nbMonsters, 0.0, this, _scenarioZone, _activationZone));
		}
		
		
	}
	
	public void removeAllListener() {
		ZoneServer.getInstance().removeListener(_activationZoneObserver);
		TimeServer.getInstance().removeListener(_coolDownTimer);
		ZoneServer.getInstance().removeListener(this);
	}
	
	public void triggerScenario() {
		// démarrage du cooldown
		_coolDownActive = true;
		_coolDownTimer = new CoolDownTimer(this);
		TimeServer.getInstance().addListener(_coolDownTimer, _coolDownTime);
		
		// on update la zone du scenario en enlevant la zone d'activation 
		ZoneServer.getInstance().addListener(this);
		this.logger.info("Scenario has been trigged");
		
		
		sendMessageToPlayers(ChatColor.RED + "The ennemy is approching!");
		sendMessageToPlayers(ChatColor.GREEN + "Get ready to push them back!");
		sendMessageToPlayers(ChatColor.YELLOW + String.valueOf(_nbWavesTotal) + ChatColor.GREEN + " waves have been spotted!");

		// on active la premiere wave et le scénario
		
		sendMessageToPlayers(ChatColor.GREEN + "First wave is coming!!!");
		_listener.setMonsterWave(_waves.get(_nbWaveDone));
		
		TimeServer.getInstance().addListener(new WaveTimer(_waves.get(_nbWaveDone), this), 10000);
		
		
		// on démarre le scénario
		_isActive = true;
	}
	
	public void terminateScenario() {
		if (_isActive) {
			
		}
		this.logger.info("Monster fury terminate");
		this.logger.info("Giving reward to players");
		
		_plugin.getServer().broadcastMessage(ChatColor.GOLD +"The ennemy has been defeated!");
		int xp = _experience / getPlayers().size();
		for (Player p : getPlayers()) {
			p.setLevel(p.getLevel() + xp);
			p.sendMessage(ChatColor.GREEN + "You received " + ChatColor.GOLD + String.valueOf(xp) + " level of XP!");
		}
		
		_nbWaveDone = 0;		
		ZoneServer.getInstance().removeListener(this);
		_isActive = false;
	}
	
	public void abortScenario() {
		this.logger.info("Monster fury aborted");
		if (_isActive) {
			sendMessageToPlayers("You retreated into safety");
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
		this.logger.info("setCoolDownAcitve to " + active);
		_coolDownActive = active;

		// on doit enlever le listener et en créer un nouveau
		TimeServer.getInstance().removeListener(_coolDownTimer);
		
		if (_coolDownActive) {
			_coolDownTimer = new CoolDownTimer(this);
			TimeServer.getInstance().addListener(_coolDownTimer, _coolDownTime);
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
		// on load la prochaine wave!
		_nbWaveDone++;
				
		if (_nbWaveDone < _waves.size()) {	
			sendMessageToPlayers(ChatColor.GOLD + "This wave has been pushed back!");
			TimeServer.getInstance().addListener(new WaveTimer(_waves.get(_nbWaveDone), this), _timeBetween);
		}
		else {
			terminateScenario();
		}
	}
	
	public void sendMessageToPlayers(String msg) {
		for (Player p : getPlayers()) {
			p.sendMessage(msg);
		}
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
	
	
	/*
	public MonsterFury(int minPlayer, long coolDown, double protectRadius, int wave, int monster, int exp, int money, double armor, String greater) {
		_minimumPlayerCount = minPlayer;
		_coolDown = coolDown;

		_nbWave = wave;
		_nbMonsters = monster;
		_experience = exp;
		_money = money;
		_armorIncrement = armor;
		_greater = greater;
	}
	
	@Override
	public void terminateInit() {
		_greaterZone = new WorldZone(getWorld(), _greater);
		_activationZone = getZone();
	}
	
	@Override
	public boolean isTriggered() {
		return _active;
	}

	@Override
	public void triggerScenario() {
		
		setZone(_greaterZone);
		
		if (_waves != null) {
			_waves.clear();
		}
		else {
			_waves = new ArrayList<MonsterWave>();
		}
					
		// creation des listeners
		if (_listener == null) {
			_listener = new MonsterFuryListener(this);
			getServer().getPluginManager().registerEvents(_listener, getPlugin());
		}
		
		// creation des waves
		for (int i = 0; i < _nbWave; i++) {
			MonsterWave wave = new MonsterWave(_nbMonsters, _damageModifier, this, _activationZone);
			_damageModifier += _armorIncrement;
			_waves.add(wave);
		}
		
		getServer().broadcastMessage(ChatColor.RED + "The ennemy is approching!");
		getServer().broadcastMessage(ChatColor.GREEN + "Get ready to push them back!");
		getServer().broadcastMessage(ChatColor.YELLOW + String.valueOf(_nbWave) + ChatColor.GREEN + " waves have been spotted!");

		// on active la premiere wave et le scénario
		/*
		getServer().broadcastMessage(ChatColor.GREEN + "First wave is coming!!!");
		_listener.setMonsterWave(_waves.get(nbWaveDone));
		
		TimeServer.getInstance().addListener(this, 10000);
		
		_active = true;
	}

	@Override
	public void abortScenario() {
		// enleve les joueurs du scénario, enleve les waves
		if (_active) {
			getServer().broadcastMessage("You retreated into safety");
			getPlayers().clear();
			
			if (_waves != null) {
				for (MonsterWave wave : _waves) {
					wave.removeMonsters();
				}
				_waves.clear();
			}
		}
		
		setZone(_activationZone);
		_active = false;
	}

	@Override
	public boolean abortWhenLeaving() {
		// si un joueur se pousse, il est exclu, il perd tt sa progression
		return true;
	}

	@Override
	public boolean canBeTriggered() {
		boolean coolDown = (_lastRun + _coolDown) <= System.currentTimeMillis();
		return coolDown && getPlayers().size() >= _minimumPlayerCount;
	}

	@Override
	public boolean mustBeStop() {
		return (getPlayers().size() == 0);
	}

	@Override
	public void terminateScenario() {
		// donne le XP, du health au joueur
		getServer().broadcastMessage(ChatColor.GOLD +"The ennemy has been defeated!");
		int xp = _experience / getPlayers().size();
		for (Player p : getPlayers()) {
			p.setLevel(p.getLevel() + xp);
			p.sendMessage(ChatColor.GREEN + "You received " + ChatColor.GOLD + String.valueOf(xp) + " level of XP!");
		}
		
		_lastRun = System.currentTimeMillis();
		nbWaveDone = 0;
		setZone(_activationZone);
		_active = false;
	}
	*/


}
/*
	
	public void waveIsDone() {
		// on load la prochaine wave!
		nbWaveDone++;
				
		if (nbWaveDone < _waves.size()) {	
			getServer().broadcastMessage(ChatColor.GOLD + "This wave has been pushed back!");
			TimeServer.getInstance().addListener(this, timeBetweenWave);			
		}
		else {
			terminateScenario();
		}
	}

	@Override
	public void setAlaram(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWorldZone(WorldZone z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WorldZone getWorldZone() {
		// TODO Auto-generated method stub
		return null;
	}
*/

/*

}
*/
/*
class MonsterWave {
	
	

}*/
