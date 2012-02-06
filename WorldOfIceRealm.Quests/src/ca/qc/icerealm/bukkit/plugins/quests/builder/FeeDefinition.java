package ca.qc.icerealm.bukkit.plugins.quests.builder;

public class FeeDefinition {
	private int money;
	private int level;
	
	public FeeDefinition(int level, int money) {
		this.money = money;
		this.level = level;
		
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int xp) {
		this.level = xp;
	}
}
