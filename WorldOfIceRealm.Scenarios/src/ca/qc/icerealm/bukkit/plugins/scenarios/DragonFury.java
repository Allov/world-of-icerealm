package ca.qc.icerealm.bukkit.plugins.scenarios;

import org.bukkit.entity.CreatureType;

import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;

public class DragonFury extends Scenario {

	private boolean isActive = false;
	
	@Override
	public boolean isTriggered() {
		return isActive;
	}

	@Override
	public void triggerScenario() {
		// on spawn le dragon dans le milieu de la zone!
		CreatureType dragon = CreatureType.ENDER_DRAGON;
		
		
		
	}

	@Override
	public boolean isComplete() {
		// quand le dragon creve!
		return false;
	}

	@Override
	public void abortScenario() {
		// si les joueurs quittent, on annule le tout!
		
	}

	@Override
	public boolean abortWhenLeaving() {
		// on désactive le tout quand y reste pu personne
		return true;
	}

	@Override
	public boolean canBeTriggered() {
		// il faut le triggeré!
		return true;
	}

}
