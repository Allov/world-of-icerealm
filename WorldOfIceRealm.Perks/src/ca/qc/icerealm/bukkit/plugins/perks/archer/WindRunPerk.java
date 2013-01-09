package ca.qc.icerealm.bukkit.plugins.perks.archer;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.qc.icerealm.bukkit.plugins.perks.Cooldown;
import ca.qc.icerealm.bukkit.plugins.perks.PerkService;

public class WindRunPerk implements Listener {
	
	private static final long CooldownTime = 60000;
	private static final long ActivationCooldownTime = 10000; 

	private PerkService perkService = PerkService.getInstance();
	private HashMap<String, Cooldown> cooldowns = new HashMap<String, Cooldown>();
	private HashMap<String, Cooldown> activated = new HashMap<String, Cooldown>();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerHit(EntityDamageEvent evt) {
		if (evt.getEntity() instanceof Player && perkService.playerHasPerk((Player)evt.getEntity(), ArcherTree.WindRunId) && 
				(evt.getCause().equals(DamageCause.ENTITY_ATTACK) || evt.getCause().equals(DamageCause.PROJECTILE))) {
			boolean canWindRun = false;
			Player player = (Player)evt.getEntity();
			
			if (cooldowns.containsKey(player.getName())) {
				if (!cooldowns.get(player.getName()).isOnCooldown()) {
					Cooldown cd = new Cooldown(CooldownTime);
					cooldowns.put(player.getName(), cd);
					cd.start();
					
					canWindRun = true;
				}
			} else {
				Cooldown cd = new Cooldown(CooldownTime);
				cooldowns.put(player.getName(), cd);
				cd.start();

				canWindRun = true;
			}
			
			if (canWindRun) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*10, 1));
				
				Cooldown cd = new Cooldown(ActivationCooldownTime);
				activated.put(player.getName(), cd);
				cd.start();				
			}			
		}
	}
	
	public boolean isActive(Player player) {
		if (activated.containsKey(player.getName())) {
			return activated.get(player.getName()).isOnCooldown();
		} else {
			return false;
		}
	}
}
