package ca.qc.icerealm.bukkit.plugins.dreamworld;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.Event;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.services.EventService;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.services.FactoryEvent;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.PinPoint;


public class DreamWorldPlugin extends JavaPlugin implements Listener, CommandExecutor {

	private Logger _logger = Logger.getLogger("Minecraft");
	private final String NEW_LINE = System.getProperty("line.separator");
	private final String WORKING_DIR = "plugins" + System.getProperty("file.separator") + "WoI.DreamWorld" + System.getProperty("file.separator");
	private boolean _generating = false;
	private StructurePattern _pattern = null;
	private IcerealmBlockPopulator _populator;
	private EventService _eventService = null;
	
	@Override
	public void onEnable() {	
		_eventService = EventService.getInstance();
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("dw").setExecutor(this);
		World myWorld = getServer().getWorld("world");
		_populator = new IcerealmBlockPopulator(getServer(), this);
		myWorld.getPopulators().add(_populator);	
		
		// loading
		try {
			
			FactoryEvent factory = new FactoryEvent();
			BufferedReader reader = new BufferedReader(new FileReader(WORKING_DIR + "events.config"));
			String line;
			
			while ((line = reader.readLine()) != null) {
				
				try {
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
							
							_logger.info("[DreamWorld] Loading event: " + e.getName() + ", config: " + e.getConfiguration() + 
								     " at: " + pattern.Source.getX() + "," + pattern.Source.getY() + "," + pattern.Source.getZ());
							
							if (event.length > 3) {
								e.setConfiguration(event[3]);
							}
							
							e.setServer(getServer());
							e.setEventArea(pattern.Layer, pattern.Row, pattern.Column);
							e.setSourceLocation(pattern.Source);
							e.setLootPoints(pattern.LootPoints);
							e.setPinPoints(pattern.PinPoints);
							e.setActivateZone(pattern.ActivationZone);
							getServer().getPluginManager().registerEvents(e, this);
							e.activateEvent();
							
							_eventService.addEvent(e);
						}
						else {
							throw new Exception("[DreamWorld] Error while loading event: " + line);
						}
					}
				}
				catch (Exception ex) {
					_logger.info(ex.getMessage());
					ex.printStackTrace(System.err);
				}
				
			}
		}
		catch (Exception ex) {
			_logger.info("[DreamWorld] exception occured while loading generated structure events, stop loading");
			ex.printStackTrace(System.err);
		}
	}
	
	@Override
	public void onDisable() {
		for (Event e : _eventService.getActiveEvents()) {
			e.releaseEvent();
		}
		_eventService.clearEvents();
	}
	
	private void acquireStructure(World world, Location source, int layer, int row, int col) {
		int layerNumber = layer;
		int rowNumber = row;
		int colNumber = col;
		
		int centerX = (int)source.getX();
		int centerY = (int)source.getY();
		int centerZ = (int)source.getZ();
		
		
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
		
		if (_pattern == null) {
			_pattern = new StructurePattern();	
		}

		_pattern.Blocks = layers;
		_pattern.Layer = layers.size();
		_pattern.Column = colNumber;
		_pattern.Row = rowNumber;
		_pattern.Source = source;
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
					arg0.sendMessage(ChatColor.GREEN + "/dw acquire [int] [int] [int] / start|end: " + ChatColor.YELLOW + ChatColor.GOLD + "/dw acquire help" + ChatColor.YELLOW + " for more info");
					arg0.sendMessage(ChatColor.GREEN + "/dw create: " + ChatColor.YELLOW + "create the structure in the world");
					arg0.sendMessage(ChatColor.GREEN + "/dw clear: " + ChatColor.YELLOW + "clear the structure from memory (any unsaved changes lost!)");
					arg0.sendMessage(ChatColor.GREEN + "/dw write [filename]: " + ChatColor.YELLOW + "write the structure in file");
					arg0.sendMessage(ChatColor.GREEN + "/dw pin: " + ChatColor.YELLOW + "pin point a location");
					arg0.sendMessage(ChatColor.GREEN + "/dw loot: " + ChatColor.YELLOW + "pin point the loot location");
					arg0.sendMessage(ChatColor.GREEN + "/dw ground: " + ChatColor.YELLOW + "set ground level of structure");
					arg0.sendMessage(ChatColor.GREEN + "/dw zone [int] [int] [int]: " + ChatColor.YELLOW + "create an activation zone");
					arg0.sendMessage(ChatColor.GREEN + "/dw event [string] +[string]: " + ChatColor.YELLOW + "add an event to the structure, config optional");
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
						
						arg0.sendMessage(ChatColor.GREEN + "1. " + ChatColor.YELLOW + "/dw acquire start" + ChatColor.GOLD + " in the lower corner facing EAST ");
						arg0.sendMessage(ChatColor.GREEN + "2. " + ChatColor.YELLOW + "/dw acquire end" + ChatColor.GOLD + " in the upper corner go toward WEST and up ");
						arg0.sendMessage(ChatColor.LIGHT_PURPLE + "  OR");
						arg0.sendMessage(ChatColor.GREEN + "1. " + ChatColor.YELLOW + "Face EAST direction");
						arg0.sendMessage(ChatColor.GREEN + "2. " + ChatColor.YELLOW + "Count the HEIGHT");
						arg0.sendMessage(ChatColor.GREEN + "3. " + ChatColor.YELLOW + "Count the block to your RIGHT");
						arg0.sendMessage(ChatColor.GREEN + "4. " + ChatColor.YELLOW + "Count the block in FRONT");
						arg0.sendMessage(ChatColor.GREEN + "5. " + ChatColor.YELLOW + "/dw acquire HEIGHT RIGHT FRONT ");
						return true;
					}
					
					if (arg3.length == 2 && arg3[1].equalsIgnoreCase("start")) {
						_pattern = new StructurePattern();
						_pattern.Source = new Location(world, centerX, centerY, centerZ);
						arg0.sendMessage(showMessage("source location acquired! next operation should be /dw acquire end"));
					}
					else if (arg3.length == 2 && arg3[1].equalsIgnoreCase("end")) {
						
						if (_pattern == null) {
							throw new Exception("the structure pattern is null. Use /dw acquire start first");
						}
						
						int layer = centerY - (int)_pattern.Source.getY();
						int row = centerZ - (int)_pattern.Source.getZ();
						int col = centerX - (int)_pattern.Source.getX();
						
						this.acquireStructure(world, _pattern.Source, layer, row, col);
						arg0.sendMessage(showMessage("structure successfully acquired!"));
					}
					else {
						
						if (arg3.length != 4) {
							throw new Exception("missing argument: /dw acquire x y z");
						}
						
						int layerNumber = Integer.parseInt(arg3[1]);
						int rowNumber = Integer.parseInt(arg3[2]);
						int colNumber = Integer.parseInt(arg3[3]);
						this.acquireStructure(world, ((Player) arg0).getLocation(), layerNumber, rowNumber, colNumber);
						arg0.sendMessage(showMessage("structure successfully acquired!"));						
					}
					

				}
				
				if (arg3[0].equalsIgnoreCase("clear")) {
					_pattern = null;
					arg0.sendMessage(showMessage("structure pattern cleared from memory"));
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
						//throw new Exception("the structure pattern is not valid. Found 0 layer! Must be > 0");
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
				
				if (arg3[0].equalsIgnoreCase("ground")) {
					if (_pattern == null) {
						throw new Exception("structure pattern not loaded in memory");
					}
					
					Location source = _pattern.Source;
					_pattern.GroundLevel = (int)(centerY - source.getY());
					arg0.sendMessage(showMessage("new ground level: " +_pattern.GroundLevel));
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
					if (e != null && arg3.length == 2) {
						_pattern.ConfigEvents.put(name, "");
					}
					else if (e != null && arg3.length > 2){
						_pattern.ConfigEvents.put(name, arg3[2]);
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
					_populator.setGlobalCoolDown(Long.parseLong(arg3[1]));
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Global cooldown: " + ChatColor.YELLOW + _populator.getGlobalCoolDown()));
				}
				
				if (arg3.length == 1 && arg3[0].contains("resetgen")) {
					_populator.resetPersistenceGeneration();
					arg0.sendMessage(showMessage(ChatColor.GRAY + "Generation data reset complete"));
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
