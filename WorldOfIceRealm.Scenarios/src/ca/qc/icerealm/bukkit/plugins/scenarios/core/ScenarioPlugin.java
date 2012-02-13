package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.scenarios.waves.MonsterFury;


public class ScenarioPlugin extends JavaPlugin {
	
	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {
		MonsterFury fury = new MonsterFury(this);
		
	}
}
