package ca.qc.icerealm.bukkit.plugins.scenarios.core;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScenarioCommander implements CommandExecutor {

	private CommandSender _sender;
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] params) {
		
		_sender = sender;
		
		if (sender.isOp()) {
			
			if (params.length == 1 && params[0].contains("list")) {
				List<Scenario> scenarios = ScenarioService.getInstance().getScenarios();
				displayListScenario(scenarios);
			}
			else if (params.length == 2 && params[1].contains("info")) {
				List<Scenario> searches = ScenarioService.getInstance().findScenarios(params[0]);
				if (searches.size() == 1) {
					displayScenarioDetails(searches.get(0));
					List<Player> players = searches.get(0).getPlayers();
					displayListPlayer(players);					
				}
				else {
					displayListScenario(searches);
				}
			}
			else if (params.length == 2 && params[1].contains("start")) {
				List<Scenario> searches = ScenarioService.getInstance().findScenarios(params[0]);
				if (searches.size() == 1) {
					searches.get(0).triggerScenario();
					displayScenarioDetails(searches.get(0));
				}
				else {
					displayListScenario(searches);
				}
			}
			else if (params.length == 2 && params[1].contains("abort")) {
				List<Scenario> searches = ScenarioService.getInstance().findScenarios(params[0]);
				if (searches.size() == 1) {
					displayScenarioDetails(searches.get(0));
					searches.get(0).abortScenario();
				}
				else {
					displayListScenario(searches);
				}
			}
			else if (params.length == 2 && params[1].contains("terminate")) {
				List<Scenario> searches = ScenarioService.getInstance().findScenarios(params[0]);
				if (searches.size() == 1) {
					displayScenarioDetails(searches.get(0));
					searches.get(0).abortScenario();
				}
				else {
					displayListScenario(searches);
				}
				
			}
			else {
				sender.sendMessage("unknown command");
			}
			
			
			
		}
		else
		{
			sender.sendMessage("[Scenario] - Only OP can do that!");
		}
		
		return false;
	}
	
	private void displayListPlayer(List<Player> list) {
		int MAXCHAR = 40;
		StringBuffer buf = new StringBuffer();
		StringBuffer line = new StringBuffer();
		for (Player p : list) {
			int totalChar = p.getName().length() + line.length();
			if (totalChar <= MAXCHAR) {
				line.append(p.getName() + " ");
			}
			else {
				_sender.sendMessage(line.toString());
				line = new StringBuffer();
				line.append(p.getName() + " ");
			}
		}
		_sender.sendMessage(line.toString());
	}
	
	private void displayScenarioDetails(Scenario s) {
		_sender.sendMessage("- " + s.getName() + ", " + s.getPlayers().size() + " players, active? " + s.isActive());
	}
	
	private void displayListScenario(List<Scenario> l) {
		if (l.size() == 0) {
			_sender.sendMessage("No scenario returned");
		}
		else {
			_sender.sendMessage("More than one scenario returned");
			for (Scenario s : l) {
				displayScenarioDetails(s);
			}
		}

	}
	

}
