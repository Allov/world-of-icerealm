package ca.qc.icerealm.bukkit.plugins.quests.builder;

import java.util.ArrayList;
import java.util.List;

public class RewardDefinition {
	private int level;
	private int money;
	private List<ItemRewardDefinition> items;

	public RewardDefinition(int level, int money) {
		this.level = level;
		this.money = money;		
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	
	public List<ItemRewardDefinition> getItems() {
		if (items == null) {
			items = new ArrayList<ItemRewardDefinition>();
		}
		
		return items;
	}
}
