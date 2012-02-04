package ca.qc.icerealm.bukkit.plugins.scenarios;

import org.bukkit.Location;
import org.bukkit.Server;

public class WalkingScenario extends Scenario {
		
	private boolean _active = false;
	
	public WalkingScenario() { }

	@Override
	public void triggerScenario() {
		_active = true;
		getServer().broadcastMessage("The walking scenario is triggered!!!");
		
	}

	@Override
	public boolean isTriggered() {
		return _active;
	}

	@Override
	public boolean isComplete() {
		_active = false;
		return true;	
	}

	@Override
	public void abortScenario() {
		_active = false;
		getServer().broadcastMessage("The Walking scenario has been abort");	
	}

	@Override
	public boolean abortWhenLeaving() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canBeTriggered() {
		// TODO Auto-generated method stub
		return false;
	}

}
