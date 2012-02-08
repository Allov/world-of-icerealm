package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;

public class FishingTournament extends Scenario {

	private HashMap<Player, Catches> playersInTournament;
	private FishingEventListener fishingEventListener;
	private boolean inProgress;
	private long created;
	private long started;
	private final long elapsed = 120000;
	
	private Logger logger = Logger.getLogger("Minecraft");
	
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
	
		started = System.currentTimeMillis();
		inProgress = true;
		playersInTournament = null;
		playersInTournament = new HashMap<Player, Catches>();
		
		getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "A fishing tournament is now " + ChatColor.GREEN + "STARTED" + ChatColor.LIGHT_PURPLE + "! " +
									 "Get your " + ChatColor.YELLOW + "fishing rod " + ChatColor.LIGHT_PURPLE + "and catch some " + ChatColor.YELLOW + "fish!");
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
		return (created + 1000) < System.currentTimeMillis() && !inProgress;
	}

	@Override
	public void progressHandler() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean mustBeStop() {
		return (started + elapsed) < System.currentTimeMillis();
	}

	@Override
	public void terminateScenario() {
		Entry<Player, Catches> winner = getWinner();
		
		getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "The fishing tournament is now " + ChatColor.RED + "FINISHED" + ChatColor.LIGHT_PURPLE + "!");
		
		if (winner == null || winner.getValue().getCatches() == 0) {
			getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "No one caught anything =( See you next time!");
		} else {
			getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "The winner is " + ChatColor.YELLOW + winner.getKey().getDisplayName().toUpperCase() + 
					 ChatColor.LIGHT_PURPLE + " with " + ChatColor.GREEN + winner.getValue().getCatches() + ChatColor.LIGHT_PURPLE + " catch! Congratulations!");
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
