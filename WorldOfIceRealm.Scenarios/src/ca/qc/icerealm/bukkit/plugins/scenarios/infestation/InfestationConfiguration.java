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
		config.UseLowestBlock = c.UseLowestBlock;
		config.Server = c.Server;
		config.DelayBeforeRespawn = c.DelayBeforeRespawn;
		config.SpawnerRadiusActivation = c.SpawnerRadiusActivation;
		config.UseInfestedZoneAsRadius = c.UseInfestedZoneAsRadius;
		config.ResetWhenNoPlayerAround = c.ResetWhenNoPlayerAround;
		return config;
	}
	
	public String InfestedZone;
	public boolean UseInfestedZoneAsRadius;
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
	public boolean UseLowestBlock;
	public org.bukkit.Server Server;
	public long DelayBeforeRespawn;
	public double SpawnerRadiusActivation;
	public boolean ResetWhenNoPlayerAround;
	public String EnterZoneMessage;
	public String LeaveZoneMessage;
	// faire un objet pour changer l'algorithme de random location
}
