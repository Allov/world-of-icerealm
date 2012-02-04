package ca.qc.icerealm.bukkit.plugins.moneydrops;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MoneyDropsEventListener implements Listener {
	
	private final MoneyDrops monsterMoney;

	public MoneyDropsEventListener(MoneyDrops monsterMoney) {
		this.monsterMoney = monsterMoney;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity killedEntity = event.getEntity();
		
		if (killedEntity instanceof Monster) {
			LivingEntity livingEntity = (LivingEntity)killedEntity;
			Player killer = livingEntity.getKiller();
			
			if (killer != null) {
				this.monsterMoney.giveMoneyReward(killer);
			}
		}
	}

}
