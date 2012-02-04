package ca.qc.icerealm.bukkit.plugins.scenarios;

import org.bukkit.configuration.file.FileConfiguration;

public class ScenarioFactory {
	
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
			String id = "2";
			FileConfiguration config = ScenarioBuilder.getScenarioConfiguration();
			int qty = config.getInt(id + ".qty");
			String monster = config.getString(id + ".monster");
			boolean immune = config.getBoolean(id + ".immune_day_light");
			return new AmbushScenario(qty, monster, immune);
		}
			
		
		return null;
		
	}
	
}
