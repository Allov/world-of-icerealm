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

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.dreamworld.events.Event;
import ca.qc.icerealm.bukkit.plugins.dreamworld.events.FactoryEvent;
import ca.qc.icerealm.bukkit.plugins.dreamworld.events.TreasureHunt;

public class DreamWorldPlugin extends JavaPlugin implements Listener, CommandExecutor {

	private Logger _logger = Logger.getLogger("Minecraft");
	private final String NEW_LINE = System.getProperty("line.separator");
	private final String WORKING_DIR = "plugins" + System.getProperty("file.separator") + "WoI.DreamWorld" + System.getProperty("file.separator");
	private boolean _generating = false;
	private StructurePattern _pattern = null;
	private IcerealmBlockPopulator _populator;
	private List<Event> _events;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("dw").setExecutor(this);
		World myWorld = getServer().getWorld("world");
		_populator = new IcerealmBlockPopulator(getServer(), this);
		_events = new ArrayList<Event>();
		myWorld.getPopulators().add(_populator);	
		
		// loading
		try {
			
			FactoryEvent factory = new FactoryEvent();
			BufferedReader reader = new BufferedReader(new FileReader(WORKING_DIR + "events.config"));
			String line;
			
			while ((line = reader.readLine()) != null) {
				
				String[] event = line.split(":");
				if (event.length > 2) {
					String eventName = event[0];
					String structureName = event[1];
					String[] sourceLocation = event[2].split(",");
					
					double locX, locY, locZ;
					locX = Double.parseDouble(sourceLocation[0]);
					locY = Double.parseDouble(sourceLocation[1]);
					locZ = Double.parseDouble(sourceLocation[2]);
										
					StructurePattern pattern = new StructurePattern();
					pattern.readFromFile(WORKING_DIR + structureName);
					pattern.Source = new Location(myWorld, locX, locY, locZ);
					
					Event e = factory.getEvent(eventName);
					
					if (e != null) {
						e.setServer(getServer());
						e.setSourceLocation(pattern.Source);
						e.setLootPoints(pattern.LootPoints);
						e.setPinPoints(pattern.PinPoints);
						e.setActivateZone(pattern.ActivationZone);
						getServer().getPluginManager().registerEvents(e, this);
						e.activateEvent();
						_events.add(e);
					}
				}
			}
		}
		catch (Exception ex) {
			_logger.info("exception occured while loading generated structure events");
			ex.printStackTrace(System.err);
		}
	}
	
	@Override
	public void onDisable() {
		for (Event e : _events) {
			e.releaseEvent();
		}
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if (arg0 instanceof Player && arg0.isOp()) {
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
					arg0.sendMessage(ChatColor.GREEN + "/dw pin: " + ChatColor.YELLOW + "pin point a location");
					arg0.sendMessage(ChatColor.GREEN + "/dw loot: " + ChatColor.YELLOW + "pin point the loot location");
					arg0.sendMessage(ChatColor.GREEN + "/dw zone [int] [int] [int]: " + ChatColor.YELLOW + "create an activation zone");
					arg0.sendMessage(ChatColor.GREEN + "/dw event [string]: " + ChatColor.YELLOW + "add an event to the structure");
					arg0.sendMessage(ChatColor.GREEN + "/dw active [boolean]: " + ChatColor.YELLOW + "activate/deactivate random generation");
					arg0.sendMessage(ChatColor.GREEN + "/dw customscan [boolean]: " + ChatColor.YELLOW + "activate/deactivate custom scanner");
					arg0.sendMessage(ChatColor.GREEN + "/dw diff [int]: " + ChatColor.YELLOW + "set the height differential (default = 4)");
					arg0.sendMessage(ChatColor.GREEN + "/dw surface [int] [int]: " + ChatColor.YELLOW + "set the surface scanned (default = 85 x 85 blocks)");
					arg0.sendMessage(ChatColor.GREEN + "/dw mindistance [int]: " + ChatColor.YELLOW + "set the min distance before next gen.");
					arg0.sendMessage(ChatColor.GREEN + "/dw globalcooldown [int]: " + ChatColor.YELLOW + "set the global cool down before next gen.");

					return true;
				}
				
				if (arg3[0].equalsIgnoreCase("acquire")) {

					if (arg3.length == 2 && arg3[1].equalsIgnoreCase("help")) {
						arg0.sendMessage(ChatColor.GREEN + "[DreamWorld] " + ChatColor.YELLOW + "How to acquire structure");
						arg0.sendMessage(ChatColor.GREEN + "1. " + ChatColor.YELLOW + "Face EAST direction");
						arg0.sendMessage(ChatColor.GREEN + "2. " + ChatColor.YELLOW + "Count the HEIGHT");
						arg0.sendMessage(ChatColor.GREEN + "3. " + ChatColor.YELLOW + "Count the block to your RIGHT");
						arg0.sendMessage(ChatColor.GREEN + "4. " + ChatColor.YELLOW + "Count the block in FRONT");
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
					
					_pattern.Source = new Location(world, centerX, centerY, centerZ);
					_populator.generateStructure(_pattern, new Location(world, centerX, centerY, centerZ));
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
				
				if (arg3[0].equalsIgnoreCase("zone")) {
					if (_pattern == null) {
						throw new Exception("structure pattern not loaded in memory");
					}
					
					if (arg3.length < 4) {
						throw new Exception("missing argument for zone: /dw zone x y z");
					}
					
					int curX = Integer.parseInt(arg3[1]);
					int curY = Integer.parseInt(arg3[2]);
					int curZ = Integer.parseInt(arg3[3]);

					Location source = _pattern.Source;
					PinPoint lower = new PinPoint((centerX - source.getX()), (centerY - source.getY()), (centerZ - source.getZ()));
					PinPoint higher =  new PinPoint(curX + lower.X, curY + lower.Y, curZ + lower.Z);
					
					if (arg3.length > 4) {
						higher.Name = arg3[4];
						lower.Name = arg3[4];
					}
					else {
						higher.Name = _pattern.ActivationZone.size() + "";
						lower.Name = _pattern.ActivationZone.size() + "";
					}
					
					List<PinPoint> zone = new ArrayList<PinPoint>();
					zone.add(lower);
					zone.add(higher);		
					
					_pattern.ActivationZone.add(zone);
					arg0.sendMessage(showMessage("zone added: " + lower.X + ", " + lower.Y + ", " + lower.Z) + " -- " + higher.X + ", " + higher.Y + ", " + higher.Z);
				}
				
				if (arg3[0].equalsIgnoreCase("event")) {
					if (_pattern == null) {
						throw new Exception("structure pattern not loaded in memory");
					}
					
					if (arg3.length < 2) {
						throw new Exception("missing argument for event: /dw event name");
					}
					
					String name = arg3[1];
					FactoryEvent factory = new FactoryEvent();
					Event e = factory.getEvent(name);
					if (e != null) {
						_pattern.Events.add(name);
					}
					else {
						throw new Exception("event not found: " + name);
					}
					
					arg0.sendMessage(showMessage("event added to pattern: " + name));
				}
				
				if (arg3.length == 1 && arg3[0].contains("customscan")) {
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Custom scanner: " + ChatColor.YELLOW + _populator.getUseCustomScanner()));
				}
				if (arg3.length == 2 && arg3[0].contains("customscan")) {
					_populator.setUseCustomScanner(Boolean.parseBoolean(arg3[1]));
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Custom scanner: " + ChatColor.YELLOW + _populator.getUseCustomScanner()));
				}
				
				if (arg3.length == 1 && arg3[0].contains("active")) {
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Status: " + ChatColor.YELLOW + _populator.getActive()));
				}
				if (arg3.length == 2 && arg3[0].contains("active")) {
					_populator.setActive(Boolean.parseBoolean(arg3[1]));
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Status: " + ChatColor.YELLOW + _populator.getActive()));
				}
				
				if (arg3.length == 1 && arg3[0].contains("diff")) {
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Height diff.: " + ChatColor.YELLOW + _populator.getHeightDifferential()));
				}
				if (arg3.length == 2 && arg3[0].contains("diff")) {
					_populator.setHeightDifferential(Integer.parseInt(arg3[1]));
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Height diff.: " + ChatColor.YELLOW + _populator.getHeightDifferential()));
				}
				
				if (arg3.length == 1 && arg3[0].contains("surface")) {
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Surface scanned: " + ChatColor.YELLOW + _populator.getWidthX() + ", " + _populator.getWidthZ()));
				}
				if (arg3.length == 3 && arg3[0].contains("surface")) {
					_populator.setWidthX(Integer.parseInt(arg3[1]));
					_populator.setWidthZ(Integer.parseInt(arg3[2]));
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Surface scanned: " + ChatColor.YELLOW + _populator.getWidthX() + ", " + _populator.getWidthZ()));
				}
				
				if (arg3.length == 1 && arg3[0].contains("mindistance")) {
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Minimum dist.: " + ChatColor.YELLOW + _populator.getMinDistanceFromLastGen()));
				}
				if (arg3.length == 3 && arg3[0].contains("mindistance")) {
					_populator.setMinDistanceFromLastGen(Double.parseDouble(arg3[1]));
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Minimum dist.: " + ChatColor.YELLOW + _populator.getMinDistanceFromLastGen()));
				}
				
				if (arg3.length == 1 && arg3[0].contains("globalcooldown")) {
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Global cooldown: " + ChatColor.YELLOW + _populator.getGlobalCoolDown()));
				}
				if (arg3.length == 3 && arg3[0].contains("globalcooldown")) {
					_populator.setGlobalCoolDown(Double.parseDouble(arg3[1]));
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Global cooldown: " + ChatColor.YELLOW + _populator.getGlobalCoolDown()));
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
		else {
			arg0.sendMessage(showMessage("Only OP can operate this plugin."));
		}
		return true;
	}	
	
	private Location getLocation(PinPoint pin, World w) {
		return new Location(w, pin.X, pin.Y, pin.Z);
	}
	
	private String showMessage(String s) {
		return ChatColor.GREEN + "[DreamWorld] " + ChatColor.YELLOW + s;
	}
}
