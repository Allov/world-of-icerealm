package ca.qc.icerealm.bukkit.plugins.scenarios.mobcontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.LivingEntity;

public class MobFollowingObserver implements Runnable {

	public static int CHECK_INTERVAL = 1000;
	private List<LivingEntity> _followers;
	private HashMap<Integer, LivingEntity> _followees;
	private ScheduledExecutorService _executor;
	private boolean _started = false;
	private MovementMobController _controller;
	
	public MobFollowingObserver(MovementMobController c) {
		_followers = Collections.synchronizedList(new ArrayList<LivingEntity>());
		_followees = new HashMap<Integer, LivingEntity>();
		_executor = Executors.newSingleThreadScheduledExecutor();
		_controller = c;
		
	}
	
	public void addFollowingAction(LivingEntity follower, LivingEntity followee) {
		if (!_followers.contains(follower)) {
			synchronized (this) {
				_followers.add(follower);
			}
		}
		
		_followees.put(follower.getEntityId(), followee);
		
		if (!_started) {
			_executor.scheduleAtFixedRate(this, 0, CHECK_INTERVAL, TimeUnit.MILLISECONDS);
			_started = true;
		}
	}
	
	public void removeFollowingAction(LivingEntity follower) {
		synchronized(this) {
			if (_followers.remove(follower)) { // was removed
				_followees.remove(follower.getEntityId());
			}		
		}
			
	}

	@Override
	public void run() {
		try {
			
			for (LivingEntity e : _followers) {
				
				LivingEntity followee = _followees.get(e.getEntityId());
				if (followee != null) {
					_controller.moveEntityToLocation(e, followee.getLocation(), true);
				}			
			}			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
