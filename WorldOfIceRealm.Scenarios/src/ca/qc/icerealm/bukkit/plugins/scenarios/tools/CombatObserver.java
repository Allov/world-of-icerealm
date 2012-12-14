package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.entity.LivingEntity;

public class CombatObserver implements Runnable {

	private final Logger _logger = Logger.getLogger(("Minecraft"));
	private ScheduledExecutorService _executor;
	private CustomMonster _entity;
	private LivingEntity _livingEntity;
	private CombatImplementor _implementor;
	
	public CombatObserver(CombatImplementor impl) {
		_executor = Executors.newSingleThreadScheduledExecutor();
		_implementor = impl;
	}
	
	public void startObservation(CustomMonster entity, LivingEntity e) {
		_executor.scheduleAtFixedRate(this, 0, 100, TimeUnit.MILLISECONDS);
		_entity = entity;
		_livingEntity = e;
	}
	
	public void stopObservation() {
		_executor.shutdown();
	}

	@Override
	public void run() {
		_implementor.analyseSituation(_entity, _livingEntity);
	}
}
