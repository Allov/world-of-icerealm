package ca.qc.icerealm.bukkit.plugins.dreamworld;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JTable.PrintMode;
import javax.swing.text.Highlighter;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class DreamWorldPlugin extends JavaPlugin implements Listener, CommandExecutor {

	private Logger _logger = Logger.getLogger("Minecraft");
	private final String NEW_LINE = System.getProperty("line.separator");
	private final String WORKING_DIR = "plugins" + System.getProperty("file.separator") + "WoI.DreamWorld" + System.getProperty("file.separator");
	private boolean _generating = false;
	private StructurePattern _pattern = null;
	private IcerealmBlockPopulator _populator;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("dw").setExecutor(this);
		World myWorld = getServer().getWorld("world");
		_populator = new IcerealmBlockPopulator();
		myWorld.getPopulators().add(_populator);
		_logger.info("SeaLevel is: " + getServer().getWorld("world").getSeaLevel());
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if (arg0 instanceof Player) {
			World world = ((Player) arg0).getWorld();
			int centerX = (int)((Player) arg0).getLocation().getX();
			int centerY = (int)((Player) arg0).getLocation().getY();
			int centerZ = (int)((Player) arg0).getLocation().getZ();
			
			try {
				if (arg3.length == 0 || arg3[0].equalsIgnoreCase("help") || arg3[0].equalsIgnoreCase("?")) {
					arg0.sendMessage(ChatColor.GREEN + "[DreamWorld] " + ChatColor.LIGHT_PURPLE + "HELP GUIDE");
					arg0.sendMessage(ChatColor.GREEN + "/dw read [string]: " + ChatColor.YELLOW + "read a structure in the specified file");
					arg0.sendMessage(ChatColor.GREEN + "/dw acquire [int] [int] [int]: " + ChatColor.YELLOW + ChatColor.GOLD + "/dw acquire help" + ChatColor.YELLOW + " for more info");
					arg0.sendMessage(ChatColor.GREEN + "/dw create: " + ChatColor.YELLOW + "create the structure in the world");
					arg0.sendMessage(ChatColor.GREEN + "/dw write [filename]: " + ChatColor.YELLOW + "write the structure in file");
					return true;
				}
				
				if (arg3[0].equalsIgnoreCase("acquire")) {

					if (arg3.length == 2 && arg3[1].equalsIgnoreCase("help")) {
						arg0.sendMessage(ChatColor.GREEN + "[DreamWorld] " + ChatColor.YELLOW + "How to acquire structure");
						arg0.sendMessage(ChatColor.GREEN + "1. " + ChatColor.YELLOW + "Face EAST direction");
						arg0.sendMessage(ChatColor.GREEN + "2. " + ChatColor.YELLOW + "Count the block to your RIGHT");
						arg0.sendMessage(ChatColor.GREEN + "3. " + ChatColor.YELLOW + "Count the block in FRONT");
						arg0.sendMessage(ChatColor.GREEN + "4. " + ChatColor.YELLOW + "Count the HEIGHT");
						arg0.sendMessage(ChatColor.GREEN + "5. " + ChatColor.YELLOW + "/dw acquire HEIGHT RIGHT FRONT ");
						return true;
					}
					
					if (arg3.length != 4) {
						throw new Exception("missing argument: /dw acquire x y z");
					}
					
					int layerNumber = Integer.parseInt(arg3[1]);
					int rowNumber = Integer.parseInt(arg3[2]);
					int colNumber = Integer.parseInt(arg3[3]);
					
					List<List<BlockUnit[]>> layers = new ArrayList<List<BlockUnit[]>>();
					for (int j = 0; j < layerNumber; j++) {
						
		        		List<BlockUnit[]> _rows = new ArrayList<BlockUnit[]>();
		        		for (int x = 0; x < rowNumber; x++) {
		        			
		        			BlockUnit[] blockRow = new BlockUnit[colNumber];
		        			
		        			for (int i = 0; i < colNumber; i++) {
		        				
		        				Block block = world.getBlockAt(centerX + i, centerY + j, centerZ + x);
		        				BlockUnit unit = new BlockUnit();
		        				unit.TypeId = block.getTypeId();
		        				unit.Data = block.getData();
		        				blockRow[i] = unit;
		        			}
		        			_rows.add(blockRow);
		        		}
		        		
		        		layers.add(_rows);
		        	}
					
					_pattern = new StructurePattern();
					_pattern.Blocks = layers;
					_pattern.Layer = layers.size();
					_pattern.Column = colNumber;
					_pattern.Row = rowNumber;
					_pattern.Source = new Location(world, centerX, centerY, centerZ);
					arg0.sendMessage(showMessage("structure successfully acquired!"));
				}
				
				if (arg3[0].equalsIgnoreCase("read")) {
					if (arg3.length != 2) {
						throw new Exception("need a filename to read: /dw read filename.txt");
					}
					
					File file = new File(WORKING_DIR + arg3[1]);
					if (!file.exists()) {
						throw new Exception(file.getPath() + " file was not found!");
					}
					
					_pattern = _populator.readFromFile(arg3[1]);
					arg0.sendMessage(showMessage("structure pattern loaded in memory from file " + arg3[1]));
				}
				
				if (arg3[0].equalsIgnoreCase("write")) {
					if (arg3.length != 2) {
						throw new Exception("need a filename to persist: /dw write filename.txt");
					}
					if (_pattern == null) {
						throw new Exception("the structure pattern is null. Make a acquire operation first");
					}
					
					File file = new File(arg3[1]);
					BufferedWriter writer = new BufferedWriter(new FileWriter(WORKING_DIR + file));
					Integer nbLayer = _pattern.Blocks.size();
					if (nbLayer == 0) {
						throw new Exception("the structure pattern is not valid. Found 0 layer! Must be > 0");
					}

					writer.append(_pattern.toString());
					writer.flush();
					writer.close();
					arg0.sendMessage(showMessage("file: " + arg3[1] + " created!"));					
				}
				
				if (arg3[0].equalsIgnoreCase("create")) {
					if (_pattern == null) {
						throw new Exception("the structure pattern is null. Make a acquire operation first");
					}
					
					_populator.generateStructure(_pattern, new Location(world, centerX, centerY, centerZ));
					_pattern.Source = new Location(world, centerX, centerY, centerZ);
					arg0.sendMessage(showMessage("structure created!"));	
				}
				
				if (arg3[0].equalsIgnoreCase("pin")) {
					if (_pattern == null) {
						throw new Exception("structure pattern not loaded in memory");
					}
					
					Location source = _pattern.Source;
					PinPoint pin = new PinPoint( (int)(centerX - source.getX()), (int)(centerY - source.getY()), (int)(centerZ - source.getZ()));
					
					if (arg3.length > 1) {
						pin.Name = arg3[1];
					}
					else {
						pin.Name = _pattern.PinPoints.size() + "";
					}
					
					_pattern.PinPoints.add(pin);
					arg0.sendMessage(showMessage("pin added: " + pin.X + ", " + pin.Y + ", " + pin.Z));
				}
				
				if (arg3[0].equalsIgnoreCase("loot")) {
					if (_pattern == null) {
						throw new Exception("structure pattern not loaded in memory");
					}
					
					Location source = _pattern.Source;
					PinPoint pin = new PinPoint( (int)(centerX - source.getX()), (int)(centerY - source.getY()), (int)(centerZ - source.getZ()));
					pin.Name = "loot";
					_pattern.LootPoints.add(pin);
					arg0.sendMessage(showMessage("loot added: " + pin.X + ", " + pin.Y + ", " + pin.Z));
				}
			}
			catch (ArrayIndexOutOfBoundsException arrayEx) {
				arg0.sendMessage(ChatColor.GRAY + "[DreamWorld]" + ChatColor.RED + " Command failed: " + ChatColor.GOLD + "array out of bound, check log for details");
				arrayEx.printStackTrace(System.err);
			}
			catch (Exception ex) {
				arg0.sendMessage(ChatColor.GRAY + "[DreamWorld]" + ChatColor.RED + " Command failed: " + ChatColor.GOLD + ex.getMessage());
				ex.printStackTrace();
			}
		}
		return true;
	}	
	
	private String showMessage(String s) {
		return ChatColor.GREEN + "[DreamWorld] " + ChatColor.YELLOW + s;
	}
}
