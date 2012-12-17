package ca.qc.icerealm.bukkit.plugins.scenarios.mobcontrol;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.MethodProfiler;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathEntity;
import net.minecraft.server.PathfinderGoalSelector;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import ca.qc.icerealm.bukkit.plugins.scenarios.tools.EntityReflection;

public class MovementMobController {

	public MovementMobController() {
		
	}
	
	public void moveEntityToLocation(LivingEntity e, Location loc) {
		moveEntityToLocation(e, loc, false);
	}
	
	public void moveEntityToLocation(LivingEntity e, Location loc, boolean eraseOldBehavior) {
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
