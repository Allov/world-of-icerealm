package ca.qc.icerealm.bukkit.plugins.quests;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;

public class Quest {
	public final Logger logger = Logger.getLogger(("Minecraft"));

	private String name;
	private String messageStart;
	private String messageEnd;
	private boolean daily;
	private Player player; 	
	
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

	public void objectiveProgressed(Objective objective, Entity entity) {
		player.sendMessage(objective.toString() + " " + EntityUtilities.getEntityName(entity));
	}
	
	public void drop() {
		
	}

	public void objectiveFulfilled(Objective objective, Entity entity) {
		for (Objective obj : objectives) {
			this.logger.info("Is it done? " + obj.isDone());
			if (!obj.isDone()) {
				return;
			}
		}
		
		// Objectives are done, reward the player;
		this.reward.giveTo(player);
		player.sendMessage(this.messageEnd);
		player.sendMessage(ChatColor.DARK_PURPLE + "Quest is " + ChatColor.GREEN + "done" + ChatColor.DARK_PURPLE + "!");
		player.sendMessage(ChatColor.DARK_PURPLE + "You received " + this.reward.toString());
	}

	public void start() {
		player.sendMessage("Quest started: " + this.name);
		player.sendMessage(this.messageStart);
	}
}
