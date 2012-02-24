package ca.qc.icerealm.bukkit.plugins.scenarios.infestation;

public class InfestationConfiguration {

	public static InfestationConfiguration clone(InfestationConfiguration c) {
		InfestationConfiguration config = new InfestationConfiguration();
		config.BurnDuringDaylight = c.BurnDuringDaylight;
		config.DelayBeforeRegeneration = c.DelayBeforeRegeneration;
		config.HealthModifier = c.HealthModifier;
		config.InfestedZone = c.InfestedZone;
		config.IntervalBetweenSpawn = c.IntervalBetweenSpawn;
		config.MaxHealth = c.MaxHealth;
		config.MaxMonstersPerSpawn = c.MaxMonstersPerSpawn;
		config.ProbabilityToSpawn = c.ProbabilityToSpawn;
		config.RegenerateExplodedBlocks = c.RegenerateExplodedBlocks;
		config.SpawnerMonsters = c.SpawnerMonsters;
		config.SpawnerQuantity = c.SpawnerQuantity;		
		return config;
	}
	
	public String InfestedZone;
	public int SpawnerQuantity;
	public String SpawnerMonsters;
	public long IntervalBetweenSpawn;
	public int ProbabilityToSpawn;
	public double HealthModifier;
	public int MaxMonstersPerSpawn;
	public boolean BurnDuringDaylight;
	public boolean RegenerateExplodedBlocks;
	public long DelayBeforeRegeneration;
	public int MaxHealth;
	
}
