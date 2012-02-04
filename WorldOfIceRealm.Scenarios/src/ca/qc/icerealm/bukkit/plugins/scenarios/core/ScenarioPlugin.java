package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import org.bukkit.plugin.java.JavaPlugin;


public class ScenarioPlugin extends JavaPlugin {
	
	@Override
	public void onDisable() {
		if (ScenarioEngine.getInstance().isScenarioActive()) {
			ScenarioEngine.getInstance().shutdownScenarioEngine();
		}
	}

	@Override
	public void onEnable() {
		if (ScenarioEngine.getInstance().isScenarioActive()) {
			ScenarioEngine.getInstance().initializeScenarioEngine(this);
		}
	}
}
