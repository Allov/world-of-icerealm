package ca.qc.icerealm.bukkit.plugins.scenarios.events;

import java.util.logging.Logger;

import net.minecraft.server.v1_4_6.EntityCreature;
import net.minecraft.server.v1_4_6.EntityLiving;
import net.minecraft.server.v1_4_6.EntityOcelot;
import net.minecraft.server.v1_4_6.EntityWolf;
import net.minecraft.server.v1_4_6.EntityZombie;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftOcelot;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftWolf;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.scenarios.mobcontrol.AgressivityMobControl;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.EntityReflection;

public class HostilePack extends BaseEvent {

	public final Logger _logger = Logger.getLogger(("Minecraft"));
	public static long SINGLE_WOLFPACK_COOLDOWN = 100;
	public int NB_MONSTER = 5;
	private Player _target;
	private String[] PACK_TYPE = new String[] { "zombie"/*, "wolf", "ocelot" */};	
	private AgressivityMobControl _agressionMob;

	public HostilePack() {
		_agressionMob = new AgressivityMobControl();
	}
	
	@Override
	protected long getCoolDownInterval() {
		return SINGLE_WOLFPACK_COOLDOWN;
	}

	@Override
	protected void resetEvent() {
		_source = null;
		
	}
	
	public void addTarget(Player p) {
		_target = p;
	}
	
	@Override
	public void activateEvent() {
		if ( _source != null) {
			WorldZone zone = new WorldZone(_source, 15);
			String randomPack = PACK_TYPE[RandomUtils.nextInt(PACK_TYPE.length)];
			if (randomPack.equalsIgnoreCase("wolf")) {
			
				for (int i = 0; i < NB_MONSTER; i++) {
					Location spawnLoc = zone.getRandomLocation(_source.getWorld());
					try {
						Wolf wolf = (Wolf)ScenarioService.getInstance().spawnCreature(_source.getWorld(), spawnLoc, EntityType.WOLF);	
						CraftWolf cWolf = (CraftWolf)wolf;
						EntityWolf eWolf = cWolf.getHandle();
						eWolf.setAngry(true);
		
						CraftPlayer craftTarget = (CraftPlayer)_target;
						EntityLiving livingTarget = craftTarget.getHandle();
						eWolf.b(livingTarget);	
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
				}	
			}
			else if (randomPack.equalsIgnoreCase("zombie")) {
				for (int i = 0; i < NB_MONSTER; i++) {
					Location spawnLoc = zone.getRandomLocation(_source.getWorld());
					try {
						LivingEntity m = (LivingEntity)ScenarioService.getInstance().spawnCreature(spawnLoc.getWorld(), spawnLoc, EntityType.ZOMBIE, -0.5, false);
						_agressionMob.defineTarget(m, _target);
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			else if (randomPack.equalsIgnoreCase("ocelot")) {
				for (int i = 0; i < NB_MONSTER; i++) {
					Location spawnLoc = zone.getRandomLocation(_source.getWorld());
					try {
						Wolf wolf = (Wolf)ScenarioService.getInstance().spawnCreature(_source.getWorld(), spawnLoc, EntityType.OCELOT);	
						CraftOcelot cWolf = (CraftOcelot)wolf;
						EntityOcelot eWolf = cWolf.getHandle();		
						CraftPlayer craftTarget = (CraftPlayer)_target;
						EntityLiving livingTarget = craftTarget.getHandle();
						eWolf.b(livingTarget);	
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
				}	
			}
			_source = null;
		}
	}
	
	public void setSource(Location loc) {
		_source = loc;
	}

	@Override
	public void releaseEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "wolfpack";
	}
}
