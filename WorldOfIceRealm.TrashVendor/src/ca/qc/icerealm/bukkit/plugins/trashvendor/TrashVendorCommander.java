package ca.qc.icerealm.bukkit.plugins.trashvendor;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class TrashVendorCommander implements CommandExecutor {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private TrashVendor _vendor;
	private JavaPlugin _plugin;
	
	public TrashVendorCommander(JavaPlugin p) {
		_plugin = p;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		
		if (arg0.isOp()) {
			if (arg3.length > 0 && arg3[0].contains("create")) {				
				/*
				if (arg0 instanceof Player) {
					Player p = (Player)arg0;
					Villager e = (Villager)p.getWorld().spawnCreature(p.getLocation(), EntityUtilities.getCreatureType("Villager"));
					TrashZoneListener listener = new TrashZoneListener(p.getLocation(), e);
					listener.timeHasCome(System.currentTimeMillis());
					p.getServer().getPluginManager().registerEvents(listener, _plugin);
				}
				*/
			}
		}
		else {
			arg0.sendMessage("Only OP can do that");
		}
			 
		
		return false;
	}

}
