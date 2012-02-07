package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;

public class Quest implements ObjectiveListener {
	public final Logger logger = Logger.getLogger(("Minecraft"));

	private String name;
	private String messageStart;
	private String messageEnd;
	private boolean daily;
	private Player player;
	private boolean done;
	
	private List<Objective> objectives;
	
	private Fees joinFees;
	private Fees dropFees;
	private Reward reward;
	
	public Quest(Player player, String name, String messageStart, String messageEnd, boolean daily, Fees joinFees, Fees dropFees, Reward reward) {
		this.player = player;
		this.name = name;
		this.messageStart = messageStart;
		this.messageEnd = messageEnd;
		this.daily = daily;
		this.joinFees = joinFees;
		this.dropFees = dropFees;
		this.reward = reward;		
	}
	
	public List<Objective> getObjectives() {
		if (objectives == null) {
			objectives = new ArrayList<Objective>();
		}
		
		return objectives;
	}

	public void drop() {
		
	}
	
	public boolean isDone() {
		return done;
	}

	public void start() {
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Quest started: " + ChatColor.YELLOW + this.name);
		player.sendMessage(ChatColor.DARK_GREEN + this.messageStart);
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Your reward will be: " + this.reward.toString());		
	}

	@Override
	public void objectiveProgressed(Objective objective, Entity entity) {
		this.player.sendMessage(objective.toString() + " " + EntityUtilities.getEntityName(entity));
	}

	@Override
	public void objectiveFailed(Objective objective) {
		
	}

	@Override
	public void objectiveDone(Objective objective) {
		for (Objective obj : objectives) {
			this.logger.info("Is it done? " + obj.isDone());
			if (!obj.isDone()) {
				return;
			}
		}
		
		this.done = true;
		
		// Objectives are done, reward the player;
		this.reward.giveTo(player);
		player.sendMessage(this.messageEnd);
		player.sendMessage(ChatColor.LIGHT_PURPLE + "Quest is " + ChatColor.GREEN + "done" + ChatColor.DARK_GREEN + "!");
		player.sendMessage(ChatColor.LIGHT_PURPLE + "You received " + this.reward.toString());
	}
}
