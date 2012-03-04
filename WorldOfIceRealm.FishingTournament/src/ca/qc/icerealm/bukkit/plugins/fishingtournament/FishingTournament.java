package ca.qc.icerealm.bukkit.plugins.fishingtournament;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.common.WorldClock;

public class FishingTournament implements Runnable {

	private static final int Chances = 8; 
	private static final int Sunset = 0;

	private HashMap<Player, Catches> playersInTournament;
	private boolean inProgress;
	private int lastWorldTime;
	private FishingEventListener fishingEventListener;
	private RegisteredServiceProvider<Economy> economyProvider;
	
	// Configurations
	private final int Span = 6;
	private final int End = Sunset + Span;
	private final int Half = End / 2;
	private final int NearEnd = End - 1;
	private final int Reward = 300; 
	
	private final FishingTournamentPlugin plugin;
	private final Server server;
	
	public FishingTournament(FishingTournamentPlugin plugin) {
		this.plugin = plugin;
		this.server = plugin.getServer();
	}

	public boolean isInProgress() {
		return inProgress;
	}

	private void notifyHalf() {
		server.broadcastMessage(ChatColor.LIGHT_PURPLE + "The fishing tournament is still going and " + ChatColor.GREEN + "HALF THE TIME" + ChatColor.LIGHT_PURPLE + " have passed!");
	}

	private void notifyNearEnd() {
		server.broadcastMessage(ChatColor.LIGHT_PURPLE + "The fishing tournament is almost finished! ");
	}

	private void startTournament() {
		boolean draw = RandomUtil.getDrawResult(Chances);
		
		Logger.getLogger("Minecraft").info("Fishing Tournament >> Draw result : " + draw);
		if (draw && server.getOnlinePlayers().length > 1) {
			if (fishingEventListener == null) {
				fishingEventListener = new FishingEventListener(this);
				server.getPluginManager().registerEvents(fishingEventListener, plugin);
			}
		
			inProgress = true;
			playersInTournament = null;
			playersInTournament = new HashMap<Player, Catches>();
			
			server.broadcastMessage(ChatColor.LIGHT_PURPLE + "A fishing tournament is now " + ChatColor.GREEN + "STARTED" + ChatColor.LIGHT_PURPLE + "! " +
										 "Get your " + ChatColor.YELLOW + "fishing rod " + ChatColor.LIGHT_PURPLE + "and catch some " + ChatColor.YELLOW + "fish! "+
										 ChatColor.LIGHT_PURPLE + "The " + ChatColor.GREEN + " reward " + ChatColor.LIGHT_PURPLE + "will be " + ChatColor.YELLOW + Reward + ChatColor.LIGHT_PURPLE + " golds!");
		}
	}
	
	private void EndTournament() {
		List<Entry<Player, Catches>> winners = getWinners();
		
		server.broadcastMessage(ChatColor.LIGHT_PURPLE + "The fishing tournament is now " + ChatColor.RED + "FINISHED" + ChatColor.LIGHT_PURPLE + "!");
		
		if (winners.size() == 0) {
			server.broadcastMessage(ChatColor.LIGHT_PURPLE + "No one caught anything =( See you next time!");
		} else {
			server.broadcastMessage(ChatColor.LIGHT_PURPLE + "The winner " + (winners.size() > 1 ? "are" : "is") + 
									ChatColor.YELLOW + " " + getWinnerNames(winners) + 
									ChatColor.LIGHT_PURPLE + " with " + ChatColor.GREEN + winners.get(0).getValue().getCatches() + ChatColor.LIGHT_PURPLE + " catch! " + 
									ChatColor.YELLOW + Reward / winners.size() + " golds " + ChatColor.LIGHT_PURPLE + " has been awarded" + 
									(winners.size() > 1 ? " to them" : " to him") + "! Congratulations!");
			giveMoneyReward(winners);
		}
		
		inProgress = false;
	}

	private String getWinnerNames(List<Entry<Player, Catches>> winners) {
		String names = "";
		
		for(int i = 0; i < winners.size(); i++) {
			if (i > 0) {
				names += ", ";
			}
			
			names += winners.get(i).getKey().getDisplayName();
		}
		
		return names;
	}

	private List<Entry<Player, Catches>> getWinners() {
		List<Entry<Player, Catches>> winners = new ArrayList<Entry<Player,Catches>>();
		int currentHighestAmount = 0;
		
		for (Entry<Player, Catches> entry : playersInTournament.entrySet()) {
			int catches = entry.getValue().getCatches();
			
			if (catches == currentHighestAmount) {
				winners.add(entry);
			} else if (catches > currentHighestAmount) {
				winners.clear();
				winners.add(entry);
				
				currentHighestAmount = catches;
			}			
		}
		
		return winners;
	}

	public void giveMoneyReward(List<Entry<Player,Catches>> winners) {
		if (economyProvider == null) {
			if(server.getPluginManager().isPluginEnabled("Vault")) {
				economyProvider = server
						.getServicesManager()
						.getRegistration(net.milkbowl.vault.economy.Economy.class);
			}
		}
	
		if (economyProvider != null) {
			Economy economy = economyProvider.getProvider();
			
			int reward = Reward / winners.size();
	
			for (Entry<Player, Catches> entry : winners) {
				if (economy.bankBalance(entry.getKey().getName()) != null) 
		        {
		        	economy.depositPlayer(entry.getKey().getName(), reward);
		        }
			}			
		}
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
	
	@Override
	public void run() {
		progress();
	}

	public void progress() {
		int currentWorldTime = WorldClock.getHour(plugin.getServer().getWorld("world"));
		
		if (this.lastWorldTime != currentWorldTime) {
			this.lastWorldTime = currentWorldTime;
			
			if (currentWorldTime == Sunset) {
				startTournament();
			} else if (isInProgress() && currentWorldTime == Half) {
				notifyHalf();
			} else if (isInProgress() && currentWorldTime == NearEnd) {
				notifyNearEnd();				
			} else if (isInProgress() && currentWorldTime == End) {
				EndTournament();
			}
		}
	}
}
