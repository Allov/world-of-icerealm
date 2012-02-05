package ca.qc.icerealm.bukkit.plugins.scenarios;

import org.bukkit.ChatColor;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;

public class DragonFury extends Scenario {

	private boolean isActive = false;
	private LivingEntity _theDragon;
	
	@Override
	public boolean isTriggered() {
		return isActive;
	}

	@Override
	public void triggerScenario() {
		// on spawn le dragon dans le milieu de la zone!
		CreatureType dragon = CreatureType.ENDER_DRAGON;
		WorldZone zone = this.getZone();
		_theDragon = getWorld().spawnCreature(zone.getCentralPointAt(100), dragon);
		getServer().broadcastMessage(ChatColor.RED + "The Dragon has been awaken!");
	}

	@Override
	public boolean isComplete() {
		// quand le dragon creve!
		return false;
	}

	@Override
	public void abortScenario() {
		// si les joueurs quittent, on annule le tout!
		if (_theDragon != null) {
			_theDragon.remove();
			getServer().broadcastMessage("The Dragon retreated to his hideout!");
		}
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
