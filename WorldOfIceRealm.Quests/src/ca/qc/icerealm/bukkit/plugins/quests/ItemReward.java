package ca.qc.icerealm.bukkit.plugins.quests;

public class ItemReward {
	private int id;
	private int amount;
	
	public ItemReward(int id, int amout) {
		this.setId(id);
		setAmount(amout);			
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
