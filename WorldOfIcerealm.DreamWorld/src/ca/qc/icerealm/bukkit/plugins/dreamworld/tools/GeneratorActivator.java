package ca.qc.icerealm.bukkit.plugins.dreamworld.tools;

import ca.qc.icerealm.bukkit.plugins.dreamworld.IcerealmBlockPopulator;

public class GeneratorActivator implements Runnable {

	private IcerealmBlockPopulator _populator;
	
	public GeneratorActivator(IcerealmBlockPopulator pop) {
		_populator = pop;
	}
	
	@Override
	public void run() {
		_populator.setActivate(true);
	}
	
}
