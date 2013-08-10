package ca.qc.icerealm.bukkit.plugins.scenarios.mobcontrol;

import java.util.List;
import java.util.logging.Logger;

import net.minecraft.server.v1_6_R2.EntityCreature;
import net.minecraft.server.v1_6_R2.MethodProfiler;
import net.minecraft.server.v1_6_R2.Navigation;
import net.minecraft.server.v1_6_R2.PathEntity;
import net.minecraft.server.v1_6_R2.PathPoint;
import net.minecraft.server.v1_6_R2.PathfinderGoalFollowParent;
import net.minecraft.server.v1_6_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_6_R2.Vec3D;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.EntityReflection;

public class MovementMobController {

	private final Logger _logger = Logger.getLogger("Minecraft");
	private MobPositionObserver _positionObserver;
	private MobFollowingObserver _followingObserver;
	
	public MovementMobController() {
		_positionObserver = new MobPositionObserver();
	}
	
	public void moveEntityToLocation(LivingEntity e, Location loc) {
		moveEntityToLocation(e, loc, false);
	}
	public void moveEntityToLocation(LivingEntity e, Location loc, boolean eraseOldBehavior) {
		moveEntityToLocation(e, loc, eraseOldBehavior, null);
	}
	
	public void moveEntityToLocation(LivingEntity e, Location loc, boolean eraseOldBehavior, DestinationReachedObserver ob) {
		
		if (ob != null) {
			_positionObserver.addDestinationReachedObserver(e, loc, ob);
		}
		
		EntityCreature eCreature = EntityReflection.getEntityCreature(e);
		float speed = EntityReflection.getEntityPropertyValue(e, EntityReflection.SPEED);

		if (eraseOldBehavior) {
			EntityReflection.setEntityPropertyValue(e, EntityReflection.PATH_GOAL_SELECTOR, new PathfinderGoalSelector(eCreature.world.methodProfiler));
			EntityReflection.setEntityPropertyValue(e, EntityReflection.TARGET_GOAL_SELECTOR, new PathfinderGoalSelector(eCreature.world.methodProfiler));
		}

		try {
			PathEntity path = eCreature.getNavigation().a(loc.getX(), loc.getY(), loc.getZ());
			eCreature.getNavigation().a(path, speed);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void moveEntityToZone(LivingEntity e, WorldZone zone, boolean eraseOldBehavior, DestinationReachedObserver ob) {
		
		if (ob != null) {
			_positionObserver.addDestinationReachedObserver(e, zone, ob);
		}
		
		this.moveEntityToLocation(e, zone.getCentralPointAt(zone.getMinHeight() + 1));
	}
	
	public void moveEntityToLocation(LivingEntity e, List<Location> locations) {
		
		if (locations.size() > 1) {
			PatrolControllerObserver observer = new PatrolControllerObserver(this, locations);
			this.moveEntityToLocation(e, locations.get(0), true, observer);
		}
		else if (locations.size() == 1) {
			this.moveEntityToLocation(e, locations.get(0), true);
		}
	}
	
	public void followAnotherEntity(LivingEntity follower, LivingEntity followee) {
		if (_followingObserver == null) {
			_followingObserver = new MobFollowingObserver(this);
		}
		
		_followingObserver.addFollowingAction(follower, followee);
	}
	
	public void stopFollowAnotherEntity(LivingEntity follower) {
		if (_followingObserver == null) {
			_followingObserver = new MobFollowingObserver(this);
		}
		
		_followingObserver.removeFollowingAction(follower);
	}
	
	public void freezeEntityToPosition(LivingEntity entity) {
		EntityReflection.setEntityPropertyValue(entity, EntityReflection.PATH_GOAL_SELECTOR, new PathfinderGoalSelector(new MethodProfiler()));
		EntityReflection.setEntityPropertyValue(entity, EntityReflection.TARGET_GOAL_SELECTOR, new PathfinderGoalSelector(new MethodProfiler()));
	}
	
	public void modifyMovementSpeed(LivingEntity e, float modifier) {
		float speed = EntityReflection.getEntityPropertyValue(e, EntityReflection.SPEED);
		speed *= modifier;
		EntityReflection.setEntityPropertyValue(e, EntityReflection.SPEED, speed);
	}
	
}

class PatrolControllerObserver implements DestinationReachedObserver {

	private final Logger _logger = Logger.getLogger("Minecraft");
	private MovementMobController _movementController;
	private List<Location> _location;
	private int _currentLocIndex = 0;
	
	public PatrolControllerObserver(MovementMobController mov, List<Location> list) {
		_movementController = mov;
		_location = list;
	}
	
	@Override
	public void destinationReached(LivingEntity e, Location l) {

		if (_currentLocIndex == _location.size() - 1) {
			_currentLocIndex = 0;
		}
		else {
			_currentLocIndex++;
		}
		
		Location nextLoc = _location.get(_currentLocIndex);
		_movementController.moveEntityToLocation(e, nextLoc, true, this);
	}
}