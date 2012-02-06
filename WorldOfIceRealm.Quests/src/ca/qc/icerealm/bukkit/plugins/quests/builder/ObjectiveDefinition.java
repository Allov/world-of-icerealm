package ca.qc.icerealm.bukkit.plugins.quests.builder;

public class ObjectiveDefinition {
	private ObjectiveType type;
	private int what;
	private int amount;

	public ObjectiveType getType() {
		return type;
	}

	public void setType(ObjectiveType type) {
		this.type = type;
	}

	public int getWhat() {
		return what;
	}

	public void setWhat(int what) {
		this.what = what;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
