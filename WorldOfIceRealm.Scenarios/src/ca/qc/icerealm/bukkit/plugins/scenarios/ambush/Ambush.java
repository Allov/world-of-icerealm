package ca.qc.icerealm.bukkit.plugins.scenarios.ambush;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.scenarios.core.ScenarioService;


public class Ambush implements Runnable, CommandExecutor {

	public final Logger logger = Logger.getLogger(("Minecraft"));
	private Server _server = null;
	private int _interval = 0;
	private int _prob = 0;
	private JavaPlugin _plugin;
	private int _radius = 32;
	private boolean _active = false;
	private int _intervalBeforeSpawn;
	private int _numberOfMonster;
	private String _monsters = "zombie,pigzombie,enderman,skeleton,spider";

	
	public Ambush(JavaPlugin plugin, int interval, int prob, int radius, int intervalBeforeSpawn, int nb) {
		_server = plugin.getServer();
		_interval = interval;
		_prob = prob;
		_plugin = plugin;
		_radius = radius;
		_intervalBeforeSpawn = intervalBeforeSpawn;
		_numberOfMonster = nb;
		
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this, 0, interval, TimeUnit.MILLISECONDS);
		_active = true;
	}

	@Override
	public void run() {
		
		if (_active) {
			
			List<Player> players = new ArrayList<Player>();
			for (Player p : _server.getOnlinePlayers()) {
				players.add(p);
			}
			
			Collections.shuffle(players);
			
			for (Player p : players) {
				
				// ajouté un calcul de probablité selon l'inventaire du joueurs
				boolean createAmbush = RandomUtil.getDrawResult(_prob);
				if (createAmbush && p.getLocation().getY() > p.getWorld().getSeaLevel() - 5) {
					logger.info("creating an ambush for " + p.getDisplayName() + " " + _monsters);
					
					HostilePackAmbush executor = new HostilePackAmbush(_radius, p, _numberOfMonster, _monsters);
					Executors.newSingleThreadScheduledExecutor().schedule(executor, _intervalBeforeSpawn, TimeUnit.MILLISECONDS);
					_server.broadcastMessage(ChatColor.YELLOW + p.getDisplayName() + ChatColor.GREEN + " has been ambushed! Help him!");
					break;
				}
			}	
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		
		if (sender.isOp()) {
			
			if (arg3.length == 0) {
				sender.sendMessage(ChatColor.GRAY + "Ambush active: " + ChatColor.YELLOW + _active);
			}
			
			if (arg3.length == 1 && (arg3[0].contains("help") || arg3[0].contains("?"))) {
				sender.sendMessage(ChatColor.DARK_GREEN + "/am prob [int] - " + ChatColor.YELLOW + "Probability to create an ambush");
				sender.sendMessage(ChatColor.DARK_GREEN + "/am active [bool] - " + ChatColor.YELLOW + "Activate/Deactivate ambushes");
				sender.sendMessage(ChatColor.DARK_GREEN + "/am delay [int] - " + ChatColor.YELLOW + "Interval before spawns");
				sender.sendMessage(ChatColor.DARK_GREEN + "/am nbmonsters [int] - " + ChatColor.YELLOW + "Number of monsters");
				sender.sendMessage(ChatColor.DARK_GREEN + "/am interval [int] - " + ChatColor.YELLOW + "Interval between each draw to create an ambush (1 = always)");
				sender.sendMessage(ChatColor.DARK_GREEN + "/am radius [int] - " + ChatColor.YELLOW + "Radius to spawn monsters (recommended < 30)");
				sender.sendMessage(ChatColor.DARK_GREEN + "/am run - " + ChatColor.YELLOW + "Ambush the command issuer (for debugging)");
			}
			
			if (arg3.length == 1 && arg3[0].contains("prob")) {
				sender.sendMessage(ChatColor.GRAY + "Ambush prob: " + ChatColor.YELLOW + _prob);
			}
			if (arg3.length == 2 && arg3[0].contains("prob")) {
				int prob = Integer.parseInt(arg3[1]);
				if (prob > 0.0) {
					_prob = prob;
					sender.sendMessage(ChatColor.GRAY + "Ambush prob: " + ChatColor.YELLOW + _prob);
				}
				else {
					sender.sendMessage(ChatColor.GRAY + "Ambush prob: " + ChatColor.RED + _prob + " value not valid");
				}
			}
			
			if (arg3.length == 1 && arg3[0].contains("status")) {
				sender.sendMessage(ChatColor.GRAY + "Ambush active: " + ChatColor.YELLOW + _active);
			}
			if (arg3.length == 2 && arg3[0].contains("status")) {
				boolean active = Boolean.parseBoolean(arg3[1]);
				_active = active;
				sender.sendMessage(ChatColor.GRAY + "Ambush active: " + ChatColor.YELLOW + _active);
			}
			
			if (arg3.length == 1 && arg3[0].contains("delay")) {
				sender.sendMessage(ChatColor.GRAY + "Ambush delay: " + ChatColor.YELLOW + _intervalBeforeSpawn);
			}
			if (arg3.length == 2 && arg3[0].contains("delay")) {
				try {
					int delay = Integer.parseInt(arg3[1]);
					_intervalBeforeSpawn = delay;
					sender.sendMessage(ChatColor.GRAY + "Ambush delay: " + ChatColor.YELLOW + _intervalBeforeSpawn);
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Ambush delay:" + ChatColor.RED + " value not valid");
				}
			}
			
			if (arg3.length == 1 && arg3[0].contains("nbmonsters")) {
				sender.sendMessage(ChatColor.GRAY + "Ambush nbmonsters: " + ChatColor.YELLOW + _numberOfMonster);
			}
			if (arg3.length == 2 && arg3[0].contains("nbmonsters")) {
				try {
					int delay = Integer.parseInt(arg3[1]);
					_numberOfMonster = delay;
					sender.sendMessage(ChatColor.GRAY + "Ambush nbmonsters: " + ChatColor.YELLOW + _numberOfMonster);
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Ambush nbmonsters:" + ChatColor.RED + " value not valid");
				}
			}
			
			if (arg3.length == 1 && arg3[0].contains("monsters")) {
				sender.sendMessage(ChatColor.GRAY + "Ambush monsters: " + ChatColor.YELLOW + _monsters);
			}
			if (arg3.length == 2 && arg3[0].contains("monsters")) {
				try {
					_monsters = arg3[1];
					sender.sendMessage(ChatColor.GRAY + "Ambush monsters: " + ChatColor.YELLOW + _monsters);
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Ambush monsters:" + ChatColor.RED + " value not valid");
				}
			}
			
			if (arg3.length == 1 && arg3[0].contains("interval")) {
				sender.sendMessage(ChatColor.GRAY + "Ambush interval: " + ChatColor.YELLOW + _interval);
			}
			if (arg3.length == 2 && arg3[0].contains("interval")) {
				try {
					int delay = Integer.parseInt(arg3[1]);
					_interval = delay;
					sender.sendMessage(ChatColor.GRAY + "Ambush interval: " + ChatColor.YELLOW + _interval);
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Ambush interval:" + ChatColor.RED + " value not valid");
				}
			}
			
			if (arg3.length == 1 && arg3[0].contains("radius")) {
				sender.sendMessage(ChatColor.GRAY + "Ambush radius: " + ChatColor.YELLOW + _radius);
			}
			if (arg3.length == 2 && arg3[0].contains("radius")) {
				try {
					int delay = Integer.parseInt(arg3[1]);
					_radius = delay;
					sender.sendMessage(ChatColor.GRAY + "Ambush radius: " + ChatColor.YELLOW + _radius);
				}
				catch (Exception ex) {
					sender.sendMessage(ChatColor.GRAY + "Ambush radius:" + ChatColor.RED + " value not valid");
				}
			}
			if (arg3.length == 1 && arg3[0].contains("run")) {
				sender.sendMessage(ChatColor.GRAY + "You started an ambush on a random player!");
				this.run();
			}
			
		}
		
		return false;
	}
}
