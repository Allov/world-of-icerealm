package ca.qc.icerealm.bukkit.plugins.quests;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.questslog.QuestLogService;

public class QuestCommandExecutor implements CommandExecutor {
	
	private final String QuestCommandName = "q";
	private final String QuestParamRandom = "random";
	private final String QuestParamLog = "log";
	private final Quests quests;

	public QuestCommandExecutor(Quests quests) {
		this.quests = quests;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandName, String[] params) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			
			if (commandName.equalsIgnoreCase(QuestCommandName)) {
				
				if (params == null || params.length == 0 || (params.length > 0 && params[0] == QuestParamRandom)) {
					giveRandomQuest(player);
				} else if (params[0] == QuestParamLog) {
					displayQuestLog(player);
				}
			
			}
			
			return true;
		} else {
			return false;
		}
	}

	private void displayQuestLog(Player player) {
		QuestLogService.getInstance().displayLogForPlayer(player);
	}

	private void giveRandomQuest(Player player) {
		Quest quest = this.quests.getQuestService().getQuest(player);
		if (quest != null) {
			quest.start();
		} else {
			player.sendMessage(ChatColor.RED + "You're already on a random quest.");
		}
	}
}
