package ca.qc.icerealm.bukkit.plugins.scenarios.mobcontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class MobPositionObserver implements Runnable {

	public static int CHECK_INTERVAL = 50;
	private final Logger _logger = Logger.getLogger("Minecraft");
	private ScheduledExecutorService _executor;
	private List<LivingEntity> _living;
	private HashMap<Integer, Location> _destinations;
	private HashMap<Integer, DestinationReachedObserver> _observers;
	private boolean _observerLoopStarted = false;
	
	public MobPositionObserver() {
		_executor = Executors.newSingleThreadScheduledExecutor();
		_living = Collections.synchronizedList(new ArrayList<LivingEntity>());
		_destinations = new HashMap<Integer, Location>();
		_observers = new HashMap<Integer, DestinationReachedObserver>();
	}
	
	public void addDestinationReachedObserver(LivingEntity e, Location l, DestinationReachedObserver ob) {
		if (!_living.contains(e)) {
			synchronized (this) {
				_living.add(e);	
			}
		}
		
		_destinations.put(e.getEntityId(), l);
		_observers.put(e.getEntityId(), ob);
		
		if (!_observerLoopStarted) {
			_executor.scheduleAtFixedRate(this, 0, CHECK_INTERVAL, TimeUnit.MILLISECONDS);
			_observerLoopStarted = true;
		}
	}
	
	@Override
	public void run() {

		try {
			
			for (LivingEntity e : _living) {
				
				DestinationReachedObserver observer = null;
				Location destination = _destinations.get(e.getEntityId());
			
				if (destination != null && !isDifferent(destination, e.getLocation()) &&
					(observer = _observers.get(e.getEntityId())) != null) {
					observer.destinationReached(e, destination);
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private boolean isDifferent(Location l1, Location l2) {
		return (int)l1.getX() != (int)l2.getX() || (int)l1.getY() != (int)l2.getY() || (int)l1.getZ() != (int)l2.getZ();
	}
}
