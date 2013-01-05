package ca.qc.icerealm.bukkit.plugins.perks;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;


public class PerkCommandExecutor implements CommandExecutor {

	private static final String PerkCommandName = "perk";
	private static final String PerkPurchaseParam = "purchase";
	private static final String PerkPurchaseShortCutParam = "p";
	private static final String PerkListParam = "list";
	private static final String PerkListShortCutParam = "l";
	private static final String PerkMyPerksParam = "myperks";
	private static final String PerkMyPerksShortCutParam = "m";
	private static final String PerkTestParam = "t";
	private final PerksPlugin plugin;
	private final PerkService perkService = PerkService.getInstance();
	
	public PerkCommandExecutor(PerksPlugin plugin) {
		this.plugin = plugin;		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandName, String[] params) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			
			if (commandName.equalsIgnoreCase(PerkCommandName)) {
				if (params.length > 1 && (params[0].equalsIgnoreCase(PerkPurchaseParam) || params[0].equalsIgnoreCase(PerkPurchaseShortCutParam))) {
					
					try {
						
						Perk perk = perkService.purchasePerk(player, params[1].toString());
						player.sendMessage("  > " + ChatColor.DARK_GREEN + " Purchased perk " + ChatColor.GREEN + perk.getName() + ChatColor.DARK_GREEN + " for " + ChatColor.YELLOW + perk.getCost() + " levels.");
						
					} catch (PerkNotFoundException e) {
						player.sendMessage(ChatColor.RED + e.getMessage());
					} catch (NotEnoughLevelExpcetion e) {
						player.sendMessage(ChatColor.RED + e.getMessage());
					} catch (AlreadyPurchasedPerkException e) {
						player.sendMessage(ChatColor.RED + e.getMessage());
					} catch (CannotPurchaseNemesisPerkException e) {
						player.sendMessage(ChatColor.RED + e.getMessage());
					} catch (RequirePerkException e) {
						player.sendMessage(ChatColor.RED + e.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else if (params.length > 0 && (params[0].equalsIgnoreCase(PerkListParam) || params[0].equalsIgnoreCase(PerkListShortCutParam))) {

					player.sendMessage(ChatColor.LIGHT_PURPLE + ">> Perk list");
					
					String treeFilter = "";
					if (params.length == 2) {
						treeFilter = params[1];
					}
					
					for (String tree : perkService.getPerks().keySet()) {
						if (treeFilter.length() == 0 || tree.equalsIgnoreCase(treeFilter)) {
							player.sendMessage("  >>> ");
							player.sendMessage("  >>> " + ChatColor.YELLOW + tree + ChatColor.WHITE + " <<<");
							player.sendMessage("  >>> ");
							for (Perk perk : perkService.getPerks().get(tree)) {
								if (!perkService.playerHasPerk(player, perk.getId())) {
									player.sendMessage("    > " + ChatColor.LIGHT_PURPLE + "[" + ChatColor.YELLOW + perk.getId() + ChatColor.LIGHT_PURPLE + "] " + ChatColor.GREEN + perk.getName() + ChatColor.LIGHT_PURPLE + " (" + ChatColor.YELLOW + perk.getCost() + " levels" + ChatColor.LIGHT_PURPLE + ")" + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + perk.getDescription());
								} else {
									player.sendMessage("" + ChatColor.GRAY + "    > " + "[" + perk.getId() + "] " + perk.getName() + " (You already have that perk)" + " : " + perk.getDescription());
								}
							}
						}
					}
					
				} else if (params.length > 0 && (params[0].equalsIgnoreCase(PerkMyPerksParam) || params[0].equalsIgnoreCase(PerkMyPerksShortCutParam))) {
					
					ArrayList<Perk> playerPerks = perkService.getPlayerPerks(player);
					if (playerPerks != null && playerPerks.size() > 0) {
						player.sendMessage(ChatColor.LIGHT_PURPLE + ">> Perks");
						
						for(Perk perk : playerPerks) {
							player.sendMessage("  > " + ChatColor.YELLOW + perk.getName() + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + perk.getDescription());
						}
					} else {
						player.sendMessage(ChatColor.RED + "You have no perk.");
					}
				} else if (params.length > 0 && params[0].equalsIgnoreCase(PerkTestParam)) {
					
				} else {
					player.sendMessage(new String[] {
							ChatColor.LIGHT_PURPLE + ">> Perk help",
							ChatColor.LIGHT_PURPLE + "[ " + ChatColor.YELLOW + "p[urchase] [perk], l[ist], m[yperks]" + ChatColor.LIGHT_PURPLE + " ]",
							"  > " + ChatColor.YELLOW + "/perk p [perk]" + ChatColor.WHITE + ": " + ChatColor.DARK_GREEN + "purchase a perk.",
							"  > " + ChatColor.YELLOW + "/perk l [tree]" + ChatColor.WHITE + ": " + ChatColor.DARK_GREEN + "list all perk.",
							"  > " + ChatColor.YELLOW + "/perk m" + ChatColor.WHITE + ": " + ChatColor.DARK_GREEN + "list my perks.",
							"  > " + ChatColor.YELLOW + "/perk" + ChatColor.WHITE + ": " + ChatColor.DARK_GREEN + "this blob."
							});
				}
			}
			
			return true;
		} else {
			return false;
		}		
	}
}
