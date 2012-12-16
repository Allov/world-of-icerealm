package ca.qc.icerealm.bukkit.plugins.perks.archer;

import java.util.Calendar;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import ca.qc.icerealm.bukkit.plugins.perks.PerkService;
import ca.qc.icerealm.bukkit.plugins.perks.warrior.WarriorTree;

public class LightningReflexesPerk implements Listener {
	private final PerkService perkService = PerkService.getInstance();
	private WindRunPerk windRunPerk;
	
	public LightningReflexesPerk(WindRunPerk windRunPerk) {
		this.windRunPerk = windRunPerk;		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerHit(EntityDamageEvent evt) {
		if (evt.getEntity() instanceof Player) {
			if (evt.getCause().equals(DamageCause.ENTITY_ATTACK) || evt.getCause().equals(DamageCause.PROJECTILE)) {
				Player player = (Player)evt.getEntity();
				
				if (perkService.playerHasPerk(player, ArcherTree.LightningReflexesId)) {
					Logger.getLogger("Minecraft").info("Windrun? " + windRunPerk.isActive(player));
					if (windRunPerk.isActive(player) && new Random(Calendar.getInstance().getTimeInMillis()).nextInt(99) < 90) {
						evt.setCancelled(true);
						Logger.getLogger("Minecraft").info("Windrun!");
					} else if (new Random(Calendar.getInstance().getTimeInMillis()).nextInt(5) == 1) {
						evt.setCancelled(true);
						Logger.getLogger("Minecraft").info("Pas Windrun =(");
					}
				}
			}
		}
	}
}
