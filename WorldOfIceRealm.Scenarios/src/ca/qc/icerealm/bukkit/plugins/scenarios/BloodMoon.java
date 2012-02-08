package ca.qc.icerealm.bukkit.plugins.scenarios;

import java.util.logging.Logger;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldClock;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;

public class BloodMoon extends Scenario {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private boolean _active;
	
	@Override
	public boolean isTriggered() {
		return _active;
	}

	@Override
	public void triggerScenario() {
		// TODO Auto-generated method stub
		for (Player p : getServer().getOnlinePlayers()) {
			getPlayers().add(p);
		}
		getServer().broadcastMessage("Blood Moon is rising!!!!");
		_active = true;
		
	}

	@Override
	public void abortScenario() {
		getPlayers().clear();
		getServer().broadcastMessage("BloodMoon has been cancelled");
		
	}

	@Override
	public boolean abortWhenLeaving() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canBeTriggered() {
		if (WorldClock.getHour(getWorld()) < 24 && WorldClock.getHour(getWorld()) > 12) {
			return true;
		}
		return false;
	}

	@Override
	public boolean mustBeStop() {
		return !canBeTriggered();
	}

	@Override
	public void terminateScenario() {
		// TODO Auto-generated method stub
		getServer().broadcastMessage("The light is back!!!!");
		_active = false;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void progressHandler() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
