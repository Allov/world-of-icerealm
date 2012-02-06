package ca.qc.icerealm.bukkit.plugins.quests.builder;

public class ItemRewardDefinition {
	private int id;
	private int amount;
	
	public ItemRewardDefinition(int id, int amount) {
		this.id = id;
		this.amount = amount;		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
