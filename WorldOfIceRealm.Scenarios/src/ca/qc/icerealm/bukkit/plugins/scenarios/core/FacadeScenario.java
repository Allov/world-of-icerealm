package ca.qc.icerealm.bukkit.plugins.scenarios.core;


public class FacadeScenario {
	
	public void addScenario(Scenario s) {
		if (s != null) {
			ScenarioEngine.getInstance().getScenarios().add(s);	
		}
	}
	
	public void initializeScenario(Scenario s) {
		if (s != null) {
			ScenarioEngine.getInstance().getBuilder().initializeScenario(s);
		}
	}
	
}
