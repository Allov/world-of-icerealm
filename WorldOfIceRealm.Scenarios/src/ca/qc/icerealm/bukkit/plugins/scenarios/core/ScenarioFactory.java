package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.AmbushScenario;
import ca.qc.icerealm.bukkit.plugins.scenarios.DragonFury;
import ca.qc.icerealm.bukkit.plugins.scenarios.MonsterFury;
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
		/*
		if (typeOfScenario.equalsIgnoreCase("monster_fury")) {
			
			String id = "0";
			FileConfiguration config = ScenarioBuilder.getScenarioConfiguration();
			int minPlayer = config.getInt(id + ".minplayer");
			long coolDown = config.getLong(id + ".cooldown");
			double protectRadius = config.getDouble(id + ".protectradius");
			int wave = config.getInt(id + ".wave");
			int monster = config.getInt(id + ".monster");
			int exp = config.getInt(id + ".experience");
			int money = config.getInt(id + ".money");
			double armor = config.getDouble(id + ".armor");
			String zone = config.getString(id + ".greater_zone");
			return new MonsterFury(minPlayer, coolDown, protectRadius, wave, monster, exp, money, armor, zone);
		}
*/
		return null;
	}	
}
