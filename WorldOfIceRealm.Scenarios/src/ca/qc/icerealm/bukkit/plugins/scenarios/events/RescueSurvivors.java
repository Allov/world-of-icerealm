package ca.qc.icerealm.bukkit.plugins.scenarios.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.qc.icerealm.bukkit.plugins.advancedcompass.AdvancedCompass;
import ca.qc.icerealm.bukkit.plugins.advancedcompass.CustomCompassManager;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.scenarios.frontier.Frontier;
import ca.qc.icerealm.bukkit.plugins.scenarios.mobcontrol.DestinationReachedObserver;
import ca.qc.icerealm.bukkit.plugins.scenarios.mobcontrol.MovementMobController;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.Loot;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.LootGenerator;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.PinPoint;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.TimeFormatter;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class RescueSurvivors extends BaseEvent implements ZoneObserver, Runnable {

	public static long COOLDOWN_INTERVAL = 20000;
	private final Logger _logger = Logger.getLogger("Minecraft");
	private MovementMobController _mobControl;
	private ScenarioService _service;
	private WorldZone _general;
	private Boolean _rescueStarted = false;
	private GreetingTrigger _globalTrigger = null;
	private List<LivingEntity> _monstersEntities;
	private LivingEntity _greeter;
	private CustomCompassManager _compass;
	private Loot _loot;
	
	public RescueSurvivors() {
		_mobControl = new MovementMobController();
		_service = ScenarioService.getInstance();
		_monstersEntities = new ArrayList<LivingEntity>();
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onQuestGiverAttacked(EntityDamageEvent event) {
		if (_greeter != null && _greeter.getEntityId() == event.getEntity().getEntityId()) {
			sendMessageToPlayers("The quest giver is attacked! He MUST NOT DIE!");
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onSurvivorDies(EntityDeathEvent event) {

		if (_livingEntities.contains(event.getEntity())) {
			
			_livingEntities.remove(event.getEntity());
			
			if (_greeter.getEntityId() == event.getEntity().getEntityId()) {
				sendMessageToPlayers("The quest giver is dead! You failed the mission!");
				activateCoolDown();
				clearPlayers();
				resetEvent();
			}
			else {
				if (_globalTrigger.survivorDied(event.getEntity()) == 0) {
					sendMessageToPlayers("All the survivors died, you failed the mission!");
					activateCoolDown();
					clearPlayers();
					resetEvent();
				}
				else {
					sendMessageToPlayers("A survivor died! Defend them against monsters!");
				}
			}
		}
	}
	
	public void activateCoolDown() {
		activateCoolDown(COOLDOWN_INTERVAL);
	}
	
	@Override
	public void setWelcomeMessage(String s) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setEndMessage(String s) {
		// TODO Auto-generated method stub
	}

	@Override
	public void activateEvent() {
		setWorldZone(getAutomaticGeneralZone());
		_logger.info(_general.toString());
		getZoneSubjectInstance().addListener(this);
	}

	@Override
	public void releaseEvent() {
		for (ZoneObserver ob : _worldZones) {
			getZoneSubjectInstance().removeListener(ob);
		}
		
		_worldZones.clear();
		
		for (LivingEntity e : _livingEntities) {
			e.remove();
		}
		
		_livingEntities.clear();
		
		for (LivingEntity e : _monstersEntities) {
			e.remove();
		}
		
		_monstersEntities.clear();

	}
	
	public void resetEvent() {	
		_rescueStarted = false;
		_globalTrigger.setActivated(false);
		for (ZoneObserver ob : _worldZones) {
			getZoneSubjectInstance().removeListener(ob);
		}
		
		
	}

	@Override
	public String getName() {
		return "rescuesurvivor";
	}

	@Override
	public void setWorldZone(WorldZone z) {
		_general = z;
	}

	@Override
	public WorldZone getWorldZone() {
		return _general;
	}

	@Override
	public void playerEntered(Player p) {
		
		if (!_players.contains(p)) {
			_players.add(p);
		}
		
		// le villager te donne la mission
		if (!_rescueStarted && !_coolDownActive) {
			
			if (_loot != null) {
				_loot.removeLoot();
			}
			
			List<WorldZone> greeting = this.getWorldZoneByName("greeting");
			List<WorldZone> follow = this.getWorldZoneByName("follow");
			
			for (WorldZone z : greeting) { // devrait en avoir seulement une
				
				// creation du mob et on le freeze sur place
				Location l = z.getCentralPointAt(10);
				
				_compass = new CustomCompassManager(getName(), p);
				_compass.setCustomLocation(l, ChatColor.AQUA + "Your compass is pointing in the safe zone");
				p.sendMessage("Go to the safe zone to talk with the quest giver!");
						
				_greeter = (LivingEntity)_service.spawnCreature(l.getWorld(), l, EntityType.VILLAGER);
				_mobControl.freezeEntityToPosition(_greeter);
				this.addLivingEntity(_greeter);
				
				// enregistrement du trigger pour donner la mission!
				_globalTrigger = new GreetingTrigger(_server, follow.size(), _mobControl, this);
				_globalTrigger.setWorldZone(z);
				_globalTrigger.setGreeter(_greeter);
				getZoneSubjectInstance().addListener(_globalTrigger);
				addZoneObserver(_globalTrigger);
			}
			
			// les zones ou les villager vont commencer a te suivre
			for (WorldZone z : follow) {
				// creation des survivors
				Location l = z.getCentralPointAt(10);
				LivingEntity follower = (LivingEntity)_service.spawnCreature(l.getWorld(), l, EntityType.VILLAGER);
				this.addLivingEntity(follower);
				_mobControl.freezeEntityToPosition(follower);
				_mobControl.modifyMovementSpeed(follower, 0.75f);
	
				// enregistrement des trigger pour débuter le following
				FollowingTrigger zoneTrigger = new FollowingTrigger(_server, follower, _mobControl, _globalTrigger);
				zoneTrigger.setWorldZone(z);
				getZoneSubjectInstance().addListener(zoneTrigger);
				_globalTrigger.addFollowingTrigger(zoneTrigger);
				addZoneObserver(zoneTrigger);
			}
			
			List<Location> monstersLocation = transformPinIntoLocations(_pinPoints);
			double modifier = Frontier.getInstance().calculateGlobalModifier(_source);
			for (Location location : monstersLocation) {
				LivingEntity monster = (LivingEntity)_service.spawnCreature(location.getWorld(), location, EntityType.ZOMBIE, modifier, true);
				_monstersEntities.add(monster);
			}
					
			_rescueStarted = true;
		}
		else if (_coolDownActive) {
			p.sendMessage("This place was already visited. Come back in " + TimeFormatter.readableTime(_timeBeforeReactivation - System.currentTimeMillis()));
		}
		else if (_rescueStarted) {
			p.sendMessage("Find the survivors and bring them into safety!");
			_compass = new CustomCompassManager(getName(), p);
			_compass.setCustomLocation(_greeter.getLocation(), ChatColor.AQUA + "Your compass is pointing in the safe zone");			
		}
		
	}
	
	public void sendMessageToPlayers(String msg) {
		for (Player p : _players) {
			p.sendMessage(msg);
		}
	}

	@Override
	public void playerLeft(Player p) {
		if (_coolDownActive) {
			_players.remove(p);
		}
	}

	@Override
	public Server getCurrentServer() {
		return _server;
	}
	
	public void clearPlayers() {
		_players.clear();
	}
	
	public void generateLoot(int modifier) {
		_loot = LootGenerator.generateRescueSurvivorLoot((double)modifier / 10);
		_loot.generateLoot(_globalTrigger.getWorldZone().getRandomLocation(_greeter.getWorld()));
		sendMessageToPlayers("Thank you very much, here is a reward for your courage!");
		
	}

}

class GreetingTrigger implements ZoneObserver, DestinationReachedObserver {
	private final Logger _logger = Logger.getLogger("Minecraft");
	private Server _server;
	private WorldZone _zone;
	private Boolean _started = false;
	private int _numberToSave = 0;
	private int _saved = 0;
	private List<LivingEntity> _entities;
	MovementMobController _mobControl;
	private List<FollowingTrigger> _following;
	private RescueSurvivors _rescue;
	private LivingEntity _entity;
	private boolean _failed = false;
		
	public GreetingTrigger(Server s, int save, MovementMobController mob, RescueSurvivors rescue) {
		_server = s;
		_numberToSave = save;
		_entities = new ArrayList<LivingEntity>();
		_mobControl = mob;
		_following = new ArrayList<FollowingTrigger>();
		_rescue = rescue;
	}
		
	public void addFollowingTrigger(FollowingTrigger t) {
		_following.add(t);
	}
	
	public void setGreeter(LivingEntity entity) {
		_entity = entity;
	}
	
	public void setActivated(boolean a) {
		_started = a;
		for (FollowingTrigger t : _following) {
			t.setActivated(_started);
		}
		_failed = a;
	}
	
	public int survivorDied(LivingEntity dead) {
		_numberToSave--;
		if (_numberToSave == 0) {
			_failed = true;
		}
		
		for (LivingEntity e : _entities) {
			if (dead.getEntityId() == e.getEntityId()) {
				_entities.remove(e);
				break;
			}
		}
		return _numberToSave;
	}
	
	@Override
	public void setWorldZone(WorldZone z) {
		_zone = z;
	}

	@Override
	public WorldZone getWorldZone() {
		return _zone;
	}

	@Override
	public void playerEntered(Player p) {
		
		if (!_started) {
			p.sendMessage("please help my friends! " + _numberToSave + " of them are trapped!");
			setActivated(true);
			_failed = false;
		}
		else if (_started && _saved < _numberToSave && _entities.size() < (_numberToSave - _saved)) {
			p.sendMessage("Did you find all of them? There is still some left!");
		}
		else if (_started && _saved == _numberToSave && !_failed) {
			p.sendMessage("You did it! Thank you very much!");
		}
		
		if (_started && _entities.size() > 0) {
			for (LivingEntity e : _entities) {
				_mobControl.stopFollowAnotherEntity(e);
				_mobControl.moveEntityToZone(e, getWorldZone(), true, this);
			}
			
			_entities.clear();
		}
	}

	@Override
	public void playerLeft(Player p) {
	}

	@Override
	public Server getCurrentServer() {
		return _server;
	}
	
	public void addLivingEntity(LivingEntity e) {
		_entities.add(e);
	}

	@Override
	public void destinationReached(LivingEntity e, Location l) {
		
		_entities.remove(e);
		_mobControl.freezeEntityToPosition(e);
		_saved++;
		if (_saved == _numberToSave) {
			_rescue.activateCoolDown();
			_rescue.sendMessageToPlayers("The survivors are now safe! Thank you!");
			_rescue.generateLoot(_saved);
			_rescue.clearPlayers();
			_rescue.resetEvent();
		}
		else {
			_rescue.sendMessageToPlayers("A survivor is now in the safe zone!");
		}
	}
	
}

class FollowingTrigger implements ZoneObserver {

	private Server _server;
	private WorldZone _zone;
	private LivingEntity _entity;
	private MovementMobController _mobControl;
	private GreetingTrigger _trigger;
	private boolean _activated = false;
	
	public FollowingTrigger(Server s, LivingEntity e, MovementMobController mob, GreetingTrigger trig) { 
		_server = s;
		_entity = e;
		_mobControl = mob;
		_trigger = trig;
	}
	
	public LivingEntity getLivingEntity() {
		return _entity;
	}
	
	public void setActivated(boolean a) {
		_activated = a;
	}
	
	@Override
	public void setWorldZone(WorldZone z) {
		_zone = z;
	}

	@Override
	public WorldZone getWorldZone() {
		return _zone;
	}

	@Override
	public void playerEntered(Player p) {
		if (_activated) {
			p.sendMessage("this villager will follow you!");
			_mobControl.followAnotherEntity(_entity, p);
			_trigger.addLivingEntity(_entity);
			_activated = false;
		}
		else {
			p.sendMessage("talk to the quest giver first, look at your compass!");
		}
	}

	@Override
	public void playerLeft(Player p) {
	}

	@Override
	public Server getCurrentServer() {
		return _server;
	}
	
}
