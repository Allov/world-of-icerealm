package ca.qc.icerealm.bukkit.plugins.scenarios.mobcontrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import ca.qc.icerealm.bukkit.plugins.common.LocationUtil;

public class MobPositionObserver implements Runnable {

	private final Logger _logger = Logger.getLogger("Minecraft");
	private ScheduledExecutorService _executor;
	private List<LivingEntity> _living;
	private HashMap<Integer, Location> _destinations;
	private HashMap<Integer, DestinationReachedObserver> _observers;
	private boolean _observerLoopStarted = false;
	
	public MobPositionObserver() {
		_executor = Executors.newSingleThreadScheduledExecutor();
		_living = new ArrayList<LivingEntity>();
		_destinations = new HashMap<Integer, Location>();
		_observers = new HashMap<Integer, DestinationReachedObserver>();
	}

	
	public void addDestinationReachedObserver(LivingEntity e, Location l, DestinationReachedObserver ob) {
		_living.add(e);
		_destinations.put(e.getEntityId(), l);
		_observers.put(e.getEntityId(), ob);
		
		if (!_observerLoopStarted) {
			_executor.scheduleAtFixedRate(this, 0, 50, TimeUnit.MILLISECONDS);
			_observerLoopStarted = true;
		}
	}
	
	@Override
	public void run() {
		
		DestinationReachedObserver observer = null;
		
		for (LivingEntity e : _living) {
			
			Location destination = _destinations.get(e.getEntityId());
			
			if (destination != null && !isDifferent(destination, e.getLocation()) &&
				(observer = _observers.get(e.getEntityId())) != null) {
				
				observer.destinationReached(e, destination);
				_observers.remove(e.getEntityId());
				_destinations.remove(e.getEntityId());
			}
		}
	}
	
	private boolean isDifferent(Location l1, Location l2) {
		return (int)l1.getX() != (int)l2.getX() || (int)l1.getY() != (int)l2.getY() || (int)l1.getZ() != (int)l2.getZ();
	}
}
