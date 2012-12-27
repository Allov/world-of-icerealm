

package ca.qc.icerealm.bukkit.plugins.scenarios.events;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.LocationUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.CombatImplementor;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.CombatObserver;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.CustomMonster;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class Boss extends BaseEvent implements ZoneObserver, CombatImplementor {

	private final Logger _logger = Logger.getLogger(("Minecraft"));
	private WorldZone _zoneActivation; 
	private List<Player> _players;
	private boolean _spawned = false;
	private CombatObserver _observer;
	private Location _lastLocation;
	private long _coolDownTeleport = 20000;
	private long _timeBeforeNextTeleport = 0;
	
	public Boss() {
		_players = new ArrayList<Player>();
		_observer = new CombatObserver(this);
	}
	
	private void spawnBoss() {
		Location l = _zoneActivation.getRandomLocation(_zoneActivation.getWorld());	
		LivingEntity e = (LivingEntity) ScenarioService.getInstance().spawnCreature(_zoneActivation.getWorld(), l, EntityType.GIANT, 2.0, false);
		_spawned = true;
		//_observer.startObservation(ScenarioService.getInstance().getCustomMonsterEntity(e.getEntityId()), e);
		_lastLocation = e.getLocation();
	}
	
	@Override
	public void analyseSituation(CustomMonster m, LivingEntity e) {
		
		Location currentLocation = e.getLocation();
		if (m.Health < 50 && LocationUtil.getDistanceBetween(_lastLocation, currentLocation) < 1 && _timeBeforeNextTeleport < System.currentTimeMillis()) {
			Location l = _zoneActivation.getRandomLocation(_zoneActivation.getWorld());
			e.teleport(l);
			_timeBeforeNextTeleport = _coolDownTeleport + System.currentTimeMillis();
		}
		
		if (m.Health <= 0) {
			_observer.stopObservation();
		}
		
		_lastLocation = e.getLocation();
	}
	

	@Override
	public void activateEvent() {
		_zoneActivation = this.getGeneralZone();
		this.getZoneSubjectInstance().addListener(this);
	}
	
	@Override
	public void playerEntered(Player arg0) {
		if (!_players.contains(arg0)) {
			_players.add(arg0);
			
			if (!_spawned) {
				arg0.sendMessage("you awake a Giant zombie!");
				spawnBoss();
			}
			else if (_spawned) {
				arg0.sendMessage("you joined the fight against the Giant Zombie");
			}
			
			
		}
	}

	@Override
	public void playerLeft(Player arg0) {
		_players.remove(arg0);
	}

	@Override
	public void releaseEvent() {
		this.getZoneSubjectInstance().removeListener(this);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "boss";
	}

	@Override
	public Server getCurrentServer() {
		// TODO Auto-generated method stub
		return _server;
	}

	@Override
	public WorldZone getWorldZone() {
		return _zoneActivation;
	}

	

	@Override
	public void setWorldZone(WorldZone arg0) {
		throw new RuntimeException("cannot set the WorldZone externally");
	}

	
	@Override
	public void setWelcomeMessage(String s) {
	}

	@Override
	public void setEndMessage(String s) {
	}

	@Override
	protected long getCoolDownInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void resetEvent() {
		// TODO Auto-generated method stub
		
	}

}
 