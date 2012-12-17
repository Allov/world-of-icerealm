package ca.qc.icerealm.bukkit.plugins.scenarios.events;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.TimeFormatter;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneObserver;

public class GlobalZoneTrigger implements ZoneObserver {

	private Logger _logger = Logger.getLogger("Minecraft");
	private List<ZoneTrigger> _runnable;
	private Server _server;
	private WorldZone _zone;
	private boolean _started = false;
	private List<Player> _players;
	private long _coolDown = 0;
	private boolean _lootCreated = false;
	private List<LivingEntity> _entities;
	private double _percent = 0.8;
	private double _additionalPlayerModifier = 0.25;
	private String _startMessage;
	private String _endMessage;
	
	public GlobalZoneTrigger(List<ZoneTrigger> trigger, Server s, double percent, double playerMod) {
		_runnable = trigger;
		_server = s;		
		_players = new ArrayList<Player>();
		_percent = percent;
		_additionalPlayerModifier = playerMod;
		_startMessage = "You are entering a dangerous area!";
		_endMessage = "This place has already been visited!";
	}
	
	public void setStartMessage(String m) {
		_startMessage = m;
	}
	
	public void setEndMessage(String m) {
		_endMessage = m;
	}
	
	public void setLootCreated(boolean b) {
		_lootCreated = b;
	}
	
	public void setCoolDown(long cool) {
		_coolDown = cool;
	}
	
	public void setEntities(List<LivingEntity> entites) {
		_entities = entites;
	}
	
	public void setPlayerList(List<Player> players) {
		_players = players;
	}
	
	@Override
	public Server getCurrentServer() {
		return _server;
	}

	@Override
	public WorldZone getWorldZone() {
		return _zone;
	}
	
	public void setStarted(boolean b) {
		_started = b;
	}

	@Override
	public void playerEntered(Player arg0) {

		boolean playeradded = false;
		if (!_players.contains(arg0)) {
			_players.add(arg0);
			playeradded = true;
		}
		
		// ce n'est pas commencé et le premier joueur vient d'entrer dans la zone!
		if (!_started && !_lootCreated) {
			arg0.sendMessage(ChatColor.YELLOW + _startMessage + ChatColor.RED + " Kill " + (_percent * 100) + "% of the monsters!");
			_started = true;
			// ici, on met les flag a on pour les zonetrigger!
			setActivate(true);
		}	
		else if (_started && playeradded && !_lootCreated) { // c'est commencé, un nouveau joueur est entré et ce n'est pas terminé.
			
			for (Player p : _players) {
				p.sendMessage(ChatColor.GREEN + arg0.getDisplayName() + ChatColor.DARK_GREEN + " joined the battle." + ChatColor.RED +" Monsters are stronger!");
			}
			
			for (LivingEntity entity : _entities) {
				ScenarioService.getInstance().updateHealthModifier(entity, _additionalPlayerModifier);
			}
		}
		
		// le loot a été crée, ce killing spree est terminé et on affiche le cooldown
		if (_lootCreated) {
			long timeLeft = (_coolDown - System.currentTimeMillis());
			if (timeLeft < 0) {
				timeLeft = 0;
			}
			arg0.sendMessage(ChatColor.YELLOW + _endMessage + ChatColor.AQUA + " Come back in " + 
							 ChatColor.GREEN + TimeFormatter.readableTime(timeLeft));
		}
	}

	@Override
	public void playerLeft(Player arg0) {
		// on veut enlever les joueurs qui viennent dans la zone si le loot est créé. 
		// sinon, c'est surment parce que c'est possible de starter le scenario ou
		// qu'il est deja en cours.
		if (_lootCreated) {
			_players.remove(arg0);
		}
		
	}

	@Override
	public void setWorldZone(WorldZone arg0) {
		_zone = arg0;
	}
	
	public void setActivate(boolean a) {
		for (ZoneTrigger trigger : _runnable) {
			trigger.setActivate(a);
		}
	}

}
