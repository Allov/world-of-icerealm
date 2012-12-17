package ca.qc.icerealm.bukkit.plugins.scenarios.mobcontrol;

import java.util.logging.Logger;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.MethodProfiler;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathEntity;
import net.minecraft.server.PathfinderGoalSelector;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import ca.qc.icerealm.bukkit.plugins.scenarios.tools.EntityReflection;

public class MovementMobController {

	private final Logger _logger = Logger.getLogger("Minecraft");
	private MobPositionObserver _positionObserver;
	
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
			EntityReflection.setEntityPropertyValue(e, EntityReflection.PATH_GOAL_SELECTOR, new PathfinderGoalSelector(new MethodProfiler()));
			EntityReflection.setEntityPropertyValue(e, EntityReflection.TARGET_GOAL_SELECTOR, new PathfinderGoalSelector(new MethodProfiler()));	
		}

		EntityReflection.setEntityPropertyValue(e, EntityReflection.NAVIGATION, new Navigation(eCreature, eCreature.world, 64F));
		PathEntity path = eCreature.getNavigation().a(loc.getX(), loc.getY(), loc.getZ());
		eCreature.getNavigation().a(path, speed);
	}
	
}
