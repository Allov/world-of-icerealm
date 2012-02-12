package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.avaje.ebeaninternal.server.transaction.TransactionMap.State;

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldClock;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;

public class FishingTournament extends Scenario {

	private HashMap<Player, Catches> playersInTournament;
	private FishingEventListener fishingEventListener;
	private boolean inProgress = false;
	private boolean hasBeenNotified = false;
	private int lastTimeChecked = -1;
	private long created;
	
	private final int Sunset = 0;
	private final int Span = 6;
	private final int End = Sunset + Span;
	private final int Half = End / 2;
	private final int NearEnd = End - 2;
	private final long TimeBeforeStart = 10000;
	private final int Reward = 300; 

	private RegisteredServiceProvider<Economy> economyProvider;
	private final Logger logger = Logger.getLogger("Minecraft");
	
	public FishingTournament() {
		created = System.currentTimeMillis();
	}
	
	@Override
	public boolean isTriggered() {
		// TODO Auto-generated method stub
		return inProgress;
	}

	@Override
	public void triggerScenario() {
		if (fishingEventListener == null) {
			fishingEventListener = new FishingEventListener(this);
			getServer().getPluginManager().registerEvents(fishingEventListener, getPlugin());
		}
	
		inProgress = true;
		playersInTournament = null;
		playersInTournament = new HashMap<Player, Catches>();
		
		getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "A fishing tournament is now " + ChatColor.GREEN + "STARTED" + ChatColor.LIGHT_PURPLE + "! " +
									 "Get your " + ChatColor.YELLOW + "fishing rod " + ChatColor.LIGHT_PURPLE + "and catch some " + ChatColor.YELLOW + "fish! "+
									 ChatColor.LIGHT_PURPLE + "The " + ChatColor.GREEN + " reward " + ChatColor.LIGHT_PURPLE + "will be " + ChatColor.YELLOW + Reward + ChatColor.LIGHT_PURPLE + " golds!");
	}

	@Override
	public void abortScenario() {
		
	}

	@Override
	public boolean abortWhenLeaving() {
		return false;
	}

	@Override
	public boolean canBeTriggered() {
		if (inProgress)
			return false;

		if (created + TimeBeforeStart > System.currentTimeMillis())
			return false;			
		
		int currentTime = WorldClock.getHour(getWorld());
		if (lastTimeChecked == currentTime)
			return false;
		
		boolean draw = RandomUtil.getDrawResult(5);
		lastTimeChecked = currentTime;
		return currentTime == Sunset && draw && getServer().getOnlinePlayers().length > 1;
	}

	@Override
	public void progressHandler() {
		int currentTime = WorldClock.getHour(getWorld());
		
		if (currentTime == Half && currentTime != lastTimeChecked) {
			getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "The fishing tournament is still going and " + ChatColor.GREEN + "HALF THE TIME" + ChatColor.LIGHT_PURPLE + " have passed!");
		} else if (currentTime == NearEnd && currentTime != lastTimeChecked) {
			getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "The fishing tournament is almost finished! ");
		}

		lastTimeChecked = currentTime;
	}

	@Override
	public boolean mustBeStop() {
		return inProgress && WorldClock.getHour(getWorld()) >= End;
	}

	@Override
	public void terminateScenario() {
		Entry<Player, Catches> winner = getWinner();
		
		getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "The fishing tournament is now " + ChatColor.RED + "FINISHED" + ChatColor.LIGHT_PURPLE + "!");
		
		if (winner == null || winner.getValue().getCatches() == 0) {
			getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "No one caught anything =( See you next time!");
		} else {
			getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "The winner is " + ChatColor.YELLOW + winner.getKey().getDisplayName().toUpperCase() + 
					 ChatColor.LIGHT_PURPLE + " with " + ChatColor.GREEN + winner.getValue().getCatches() + ChatColor.LIGHT_PURPLE + " catch! " + 
					 ChatColor.YELLOW + Reward + ChatColor.LIGHT_PURPLE + " golds! Congratulations!");
			giveMoneyReward(winner.getKey());
		}
		
		inProgress = false;
	}

	private Entry<Player, Catches> getWinner() {
		Entry<Player, Catches> largestAmount = null;
		for (Entry<Player, Catches> entry : playersInTournament.entrySet()) {
			if (largestAmount == null || largestAmount.getValue().getCatches() < entry.getValue().getCatches()) {
				largestAmount = entry;
			}
		}
		
		return largestAmount;
	}

	@Override
	public boolean isComplete() {
		return false;
	}

	public void addCatchToPlayer(Player player) {
		Catches catches;
		if (!playersInTournament.containsKey(player)) {
			catches = new Catches();
			playersInTournament.put(player, catches);
		} else {
			catches = playersInTournament.get(player);
		}
		
		catches.addCatch();
		player.sendMessage(ChatColor.YELLOW + "You caught " + ChatColor.GREEN + catches.getCatches() + ChatColor.YELLOW + " fish! Keep going!");
	}
	
	public void giveMoneyReward(Player player) {
		if (economyProvider == null) {
			if(getServer().getPluginManager().isPluginEnabled("Vault")) {
				economyProvider = getServer()
						.getServicesManager()
						.getRegistration(net.milkbowl.vault.economy.Economy.class);
			}
		}
		
		if (economyProvider != null) {
			Economy economy = economyProvider.getProvider();
	
			if (economy.bankBalance(player.getName()) != null) 
	        {
	        	economy.depositPlayer(player.getName(), Reward);
	        }
		}
	}
}

class FishingEventListener implements Listener {
	private final FishingTournament scenario;
	private Logger logger = Logger.getLogger("Fishing");

	public FishingEventListener(FishingTournament scenario) {
		this.scenario = scenario;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onFishOn(PlayerFishEvent event) {
		if (scenario.isTriggered()) {
			Entity theCatch = event.getCaught();
			
			if (theCatch instanceof CraftItem) {
				scenario.addCatchToPlayer(event.getPlayer());
			}
		}
	}
}

class Catches {
	private int catches = 0;
	public int addCatch() {
		return catches++;
	}
	
	public int getCatches() {
		return catches;
	}
}
