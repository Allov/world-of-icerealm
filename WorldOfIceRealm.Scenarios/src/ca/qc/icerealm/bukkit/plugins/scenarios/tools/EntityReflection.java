package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;

import net.minecraft.server.EntityCreature;

public class EntityReflection {
	
	private final static Logger _logger = Logger.getLogger("Minecraft");
	
	public final static String HEALTH	 			= "health";
	public final static String SPEED	 			= "bG";
	public final static String FIRE_PROOF 			= "fireProof";
	public final static String PATH_GOAL_SELECTOR 	= "goalSelector";
	public final static String TARGET_GOAL_SELECTOR = "targetSelector";
	public final static String NAVIGATION 			= "navigation";

	public static <T> T getEntityPropertyValue(LivingEntity e, String prop) {
		
		T field = null;
		
		try {
			EntityCreature creature = getEntityCreature(e);
			Field f = getField(creature.getClass(), prop);
			f.setAccessible(true);
			field = (T)f.get(creature);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return field;
	}
	
	public static void setEntityPropertyValue(LivingEntity e, String prop, Object value) {
		
		try {
			EntityCreature creature = getEntityCreature(e);
			Field f = getField(creature.getClass(), prop);
			f.setAccessible(true);
			f.set(creature, value);	
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }
	
	public static EntityCreature getEntityCreature(LivingEntity creature) {
		CraftEntity c = (CraftEntity)creature;
		return (EntityCreature)c.getHandle();
	}
}
