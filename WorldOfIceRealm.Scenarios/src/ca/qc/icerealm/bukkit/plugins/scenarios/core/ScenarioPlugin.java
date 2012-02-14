package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.scenarios.monsterfury.MonsterFury;


public class ScenarioPlugin extends JavaPlugin {
	
	MonsterFury _monster;
	
	@Override
	public void onDisable() {
		_monster.removeAllListener();
	}

	@Override
	public void onEnable() {
		_monster = new MonsterFury(this);
		
		
	}
}
