package ca.qc.icerealm.bukkit.plugins.perks.warrior;

import java.util.logging.Logger;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.qc.icerealm.bukkit.plugins.perks.PerkService;

public class MercenaryPerk implements Listener {

	private final PerkService perkService = PerkService.getInstance();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onCreatureKill(EntityDeathEvent evt) {
		if (evt.getEntity() instanceof Monster) {
			
			Monster monster = (Monster)evt.getEntity();
			Player killer = monster.getKiller();
			
			if (killer != null && perkService.playerHasPerk(killer, WarriorTree.MercenaryId)) {
				evt.setDroppedExp((int)((double)evt.getDroppedExp() * 1.20));
			}
		}
	}	
}
