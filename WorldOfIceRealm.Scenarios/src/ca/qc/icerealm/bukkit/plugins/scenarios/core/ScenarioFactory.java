package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

import ca.qc.icerealm.bukkit.plugins.scenarios.AmbushScenario;
import ca.qc.icerealm.bukkit.plugins.scenarios.DragonFury;
import ca.qc.icerealm.bukkit.plugins.scenarios.WalkingScenario;
import ca.qc.icerealm.bukkit.plugins.scenarios.WaveScenario;

class ScenarioFactory {
	public static final Logger logger = Logger.getLogger(("Minecraft"));
	
	public static Scenario getInstanceOf(String typeOfScenario) {
		
		if (typeOfScenario.equalsIgnoreCase("walking")) {
			return new WalkingScenario();
		}
		
		if (typeOfScenario.equalsIgnoreCase("wave")) {
			// ajouté les objets nécessaires au scenario dans le constructuer, ou 
			// utiliser les get set de la classe...
			return new WaveScenario();
		}
		
		if (typeOfScenario.equalsIgnoreCase("ambush")) {
			String id = "0";
			FileConfiguration config = ScenarioBuilder.getScenarioConfiguration();
			int qty = config.getInt(id + ".qty");
			String monster = config.getString(id + ".monster");
			boolean immune = config.getBoolean(id + ".immune_day_light");
			return new AmbushScenario(qty, monster, immune);
		}
		
		if (typeOfScenario.equalsIgnoreCase("dragon_fury")) {
			String id = "0";
			FileConfiguration config = ScenarioBuilder.getScenarioConfiguration();
			int ghastSpawn = config.getInt(id + ".ghast_prob");
			int ghastMax = config.getInt(id + ".ghast_max");
			long coolDown = config.getLong(id + ".cooldown");
			int nbDragon = config.getInt(id + ".dragons");
			int max_health = config.getInt(id + ".max_health");
			int experienceDrop = config.getInt(id + ".experience");
			int monsterMax = config.getInt(id + ".monster_max");
			String monsters = config.getString(id + ".monsters");
			return new DragonFury(ghastSpawn, ghastMax, coolDown, nbDragon, max_health, experienceDrop, monsterMax, monsters);
		}

		return null;
	}
	
}
