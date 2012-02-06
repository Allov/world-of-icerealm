package ca.qc.icerealm.bukkit.plugins.quests.builder;

import java.util.List;

import ca.qc.icerealm.bukkit.plugins.common.Zone;
import ca.qc.icerealm.bukkit.plugins.quests.Fees;
import ca.qc.icerealm.bukkit.plugins.quests.Objective;

public class QuestDefinition {
	private String name;
	private String messageStart;
	private String messageEnd;
	private String group;
	private boolean daily;
	private Zone zone; 
	
	private List<ObjectiveDefinition> objectives;
	
	private FeeDefinition joinFees;
	private FeeDefinition dropFees;
	private RewardDefinition rewards;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMessageStart() {
		return messageStart;
	}
	public void setMessageStart(String messageStart) {
		this.messageStart = messageStart;
	}
	
	public String getMessageEnd() {
		return messageEnd;
	}
	public void setMessageEnd(String messageEnd) {
		this.messageEnd = messageEnd;
	}
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	public boolean isDaily() {
		return daily;
	}
	public void setDaily(boolean daily) {
		this.daily = daily;
	}
	public Zone getZone() {
		return zone;
	}
	public void setZone(Zone zone) {
		this.zone = zone;
	}
	public List<ObjectiveDefinition> getObjectives() {
		return objectives;
	}
	public void setObjectives(List<ObjectiveDefinition> objectives) {
		this.objectives = objectives;
	}
	public FeeDefinition getJoinFees() {
		return joinFees;
	}
	public void setJoinFees(FeeDefinition joinFees) {
		this.joinFees = joinFees;
	}
	public FeeDefinition getDropFees() {
		return dropFees;
	}
	public void setDropFees(FeeDefinition dropFees) {
		this.dropFees = dropFees;
	}
	public RewardDefinition getRewards() {
		return rewards;
	}
	public void setRewards(RewardDefinition rewards) {
		this.rewards = rewards;
	}
}
