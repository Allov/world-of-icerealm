package ca.qc.icerealm.bukkit.plugins.scenarios;

import org.bukkit.Location;
import org.bukkit.Server;

import ca.qc.icerealm.bukkit.plugins.scenarios.core.Scenario;

public class WalkingScenario extends Scenario {
		
	private boolean _active = false;
	private long _started;
	private long _elapsed = 30000;
	private long _created;
	private boolean _cannotBeStartedEver = false;
	
	public WalkingScenario() { 
		_created = System.currentTimeMillis();
	}

	@Override
	public void triggerScenario() {
		_active = true;
		_started = System.currentTimeMillis();
		getServer().broadcastMessage("The walking scenario is triggered!!!");
		
		
	}

	@Override
	public boolean isTriggered() {
		return _active;
	}
	


	@Override
	public void abortScenario() {
		_active = false;
		_cannotBeStartedEver = true;
		getServer().broadcastMessage("The Walking scenario has been abort");	
	}

	@Override
	public boolean abortWhenLeaving() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canBeTriggered() {
		return (_created + _elapsed) < System.currentTimeMillis() && !_cannotBeStartedEver;
	}

	@Override
	public boolean mustBeStop() {
		return (_started + _elapsed) < System.currentTimeMillis();
	}

	@Override
	public void terminateScenario() {
		// TODO Auto-generated method stub
		getServer().broadcastMessage("The Walking scenario has been terminated");	
		_active = false;
		_cannotBeStartedEver = true;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

}
