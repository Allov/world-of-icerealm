package ca.qc.icerealm.bukkit.plugins.quests;

public class Fees {
	private int level;
	private int money;
	private int hunger;
	private int health;
	
	public Fees(int level, int money, int hunger, int health) {
		this.level = level;
		this.money = money;
		this.hunger = hunger;
		this.health = health;		
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

	public int getHunger() {
		return hunger;
	}

	public void setHunger(int hunger) {
		this.hunger = hunger;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
}
