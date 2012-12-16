package ca.qc.icerealm.bukkit.plugins.streaks;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.MobEffect;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

public class StreakListener implements Listener {
	
	private static final int SpeedRewardComboCount = 2;
	private static final int StrengthRewardComboCount = 3;
	private static final int RegenRewardComboCount = 4;
	private static final int KillStreakLevelRewardComboCount = 3;
	private static final int KillingStreak = 2;
	private static final int RampageStreak = 3;
	private static final int GodlikeStreak = 4;
	private static final int BaseLevelReward = 1;
	private static final long MaximumStreakInactivityTime = 15000;
	private static final int[] ComboMileStones = new int[] { 5, 12, 20, 32, 48, 68, 92, 120 };

	private Map<Player, Combo> combos;
	private final Plugin plugin;
	
	public StreakListener(Plugin plugin) {
		this.plugin = plugin;		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		
		if (entity instanceof Monster) {
			Player killer = ((Monster) entity).getKiller();
			
			if (killer != null) {
				Combo combo = incrementCombo(killer);
				calculateCombo(killer, combo);
			}				
		}
	}

	private void calculateCombo(Player killer, Combo combo) {
		String comboString = combo.toString();
		
		if (combo.getComboMileStone() <= ComboMileStones.length  &&
			combo.getKillCount() == ComboMileStones[combo.getComboMileStone()]) {
			
			combo.incrementComboMileStone();
			
			if (combo.isFlawless()) {
				int levelReward = giveLevelReward(killer, (int)Math.ceil(((double)combo.getComboMileStone() * 0.5)));
				comboString += ChatColor.DARK_GREEN + " (" + ChatColor.YELLOW + levelReward + " level"; 
			} else if (combo.getComboMileStone() >= KillStreakLevelRewardComboCount) {
				int levelReward = giveLevelReward(killer, 1);				
				comboString += ChatColor.DARK_GREEN + " (" + ChatColor.YELLOW + levelReward + " level"; 
			}			
			
			if (combo.getComboMileStone() == SpeedRewardComboCount) {
				giveSpeedReward(killer);
				comboString += ChatColor.DARK_GREEN + " and " + ChatColor.YELLOW + "swiftness"; 
			}
			
			if (combo.getComboMileStone() == StrengthRewardComboCount) {
				giveStrengthReward(killer);
				comboString += ChatColor.DARK_GREEN + " and " + ChatColor.YELLOW + "strength"; 
			}

			if (combo.getComboMileStone() == RegenRewardComboCount) {
				giveRegenReward(killer);
				comboString += ChatColor.DARK_GREEN + " and " + ChatColor.YELLOW + "regen"; 
			}
			
			if (combo.getComboMileStone() == KillingStreak) {
				plugin.getServer().broadcastMessage("" + ChatColor.YELLOW + killer.getDisplayName() + ChatColor.DARK_GREEN + " is on a " + ChatColor.RED + " killing streak!");
			}

			if (combo.getComboMileStone() == RampageStreak) {
				plugin.getServer().broadcastMessage("" + ChatColor.YELLOW + killer.getDisplayName() + ChatColor.DARK_GREEN + " is on a " + ChatColor.RED + " rampage streak!");
			}

			if (combo.getComboMileStone() == GodlikeStreak) {
				plugin.getServer().broadcastMessage("" + ChatColor.YELLOW + killer.getDisplayName() + ChatColor.DARK_GREEN + " is " + ChatColor.RED + " GODLIKE! A " + ChatColor.GREEN + combo.getKillCount() + ChatColor.DARK_GREEN + " kill streak! Strength has been awarded to everyone.");
				for (Player player : plugin.getServer().getOnlinePlayers()) {
					giveStrengthReward(player);
				}
			}

			comboString += ChatColor.DARK_GREEN + ")";
			
			killer.sendMessage(comboString);
		}
	}
	
	private void giveRegenReward(Player killer) {
		CraftPlayer cp = (CraftPlayer)killer;
		cp.getHandle().addEffect(new MobEffect(10, 1000, 0));
	}

	private void giveStrengthReward(Player killer) {
		CraftPlayer cp = (CraftPlayer)killer;
		cp.getHandle().addEffect(new MobEffect(5, 1000, 0));
	}

	private void giveSpeedReward(Player killer) {
		CraftPlayer cp = (CraftPlayer)killer;
		cp.getHandle().addEffect(new MobEffect(1, 1000, 0));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamaged(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		
		if (entity instanceof Player) {
			Combo combo = getCombo((Player) entity);
			combo.setFlawless(false);
		}
	}
	
	private int giveLevelReward(Player player, int modifier) {
		int levelReward = BaseLevelReward * (int)modifier;
		player.setLevel(player.getLevel() + levelReward);
		
		return levelReward;
	}

	private Combo incrementCombo(Player killer) {
		long currentTime = System.currentTimeMillis();

		Combo combo = getCombo(killer);
		long lastKillTimeDifference = currentTime - combo.getLastKillTime();
		if (lastKillTimeDifference > MaximumStreakInactivityTime) {
			combo.reset(currentTime);
		} 
		
		combo.setKillCount(combo.getKillCount() + 1);
		combo.setLastKillTime(currentTime);
		
		return combo;
	}

	public Map<Player, Combo> getCombos() {
		if (combos == null) {
			combos = new HashMap<Player, Combo>();
		}
		
		return combos;
	}
	
	public Combo getCombo(Player player) {
		Combo combo = getCombos().get(player);
		
		if (combo == null) {
			combo = new Combo();
			getCombos().put(player, combo);
		}
		
		return combo;
	}
}

class Combo {
	private int killCount;
	private boolean flawless;
	private boolean critical;
	private boolean bow;
	private long lastKillTime;
	private int comboMileStone;
	
	public int getKillCount() {
		return killCount;
	}
	
	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}
	
	public boolean isFlawless() {
		return flawless;
	}
	
	public void setFlawless(boolean flawless) {
		this.flawless = flawless;
	}
	
	public boolean isCritical() {
		return critical;
	}
	
	public void setCritical(boolean critical) {
		this.critical = critical;
	}
	
	public boolean isBow() {
		return bow;
	}
	
	public void setBow(boolean bow) {
		this.bow = bow;
	}

	public long getLastKillTime() {
		return lastKillTime;
	}

	public void setLastKillTime(long lastKillTime) {
		this.lastKillTime = lastKillTime;
	}
	
	public void reset(long time) {
		this.lastKillTime = time;
		this.killCount = 0;
		this.flawless = true;
		this.critical = false;
		this.bow = false;
		this.comboMileStone = 0;
	}
	
	@Override
	public String toString() {
		return 	"" + (isFlawless() ? ChatColor.YELLOW + "Flawlessly " : "") + ChatColor.DARK_GREEN + 
				(isFlawless() ? "killed " : "Killed ") + ChatColor.GREEN + getKillCount() + ChatColor.DARK_GREEN + " monsters";
	}

	public int getComboMileStone() {
		return comboMileStone;
	}

	public void incrementComboMileStone() {
		this.comboMileStone++;
	}
}
