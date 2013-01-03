package ca.qc.icerealm.bukkit.plugins.dreamworld;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.java.JavaPlugin;
import ca.qc.icerealm.bukkit.plugins.advancedcompass.CustomCompassManager;
import ca.qc.icerealm.bukkit.plugins.common.LocationUtil;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.Event;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.FactoryEvent;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.PinPoint;

public class IcerealmBlockPopulator extends BlockPopulator {

	private Logger _logger = Logger.getLogger("Minecraft");
	private final String WORKING_DIR = "plugins" + System.getProperty("file.separator") + "WoI.DreamWorld" + System.getProperty("file.separator");
	private boolean _canGenerate = true;
	private boolean _ignoreVegetation = true;
	private int _seaLevel = 0;
	private Server _server;
	private JavaPlugin _plugin;
	private HashSet<GenerationEvent> _generatedStructure;
	private HashMap<String, BiomeScanner> _scanners;
	private BiomeScanner _customScanner;
	private boolean _useCustomScanner = false;
	private BiomeScanner _defaultScanner;
	private Location _lastGenerationLocation;
	private double _minDistanceFromLastGeneration = 0;
	private long _globalCoolDown = 0;
	private double _lastGenerationTime = 0;
	
		
	public IcerealmBlockPopulator(Server s, JavaPlugin j) {
		_server = s;
		_plugin = j;
		_generatedStructure = readGeneratedStructure();
		_seaLevel = s.getWorld("world").getSeaLevel();
		_scanners = readBiomeScanner();
		_customScanner = new BiomeScanner("default:50:50:5");
		_defaultScanner = new BiomeScanner("default:50:50:10"); // scan 50 x 50, diff de +/- 5
		_globalCoolDown = 420000; // 6 minute
		//_globalCoolDown = 10000; // 6 minute
		_minDistanceFromLastGeneration = 500; // 500m
		_lastGenerationLocation = s.getWorld("world").getSpawnLocation();
	}
	
	@Override
	public void populate(World world, Random random, Chunk source) {

		if (_canGenerate && _lastGenerationTime < System.currentTimeMillis()) {
			boolean valid = true;
			int centerX = (source.getX() << 4) + random.nextInt(16);
	        int centerZ = (source.getZ() << 4) + random.nextInt(16);
	        int centerY = world.getHighestBlockYAt(centerX, centerZ) - 1;

	        Location currentLocation = new Location(world, centerX, centerY, centerZ);
	        if (LocationUtil.getDistanceBetween(currentLocation, _lastGenerationLocation) <= _minDistanceFromLastGeneration) {
	        	return;
	        }

	        BiomeScanner scanner = getBiomeScanner(world.getBiome(centerX, centerZ).name());
	        
	        if (centerY < world.getSeaLevel()) {
	        	return;
	        }
	        
	        if (world.getHighestBlockAt(centerX, centerZ).getType() == Material.WATER) {
	        	return;
	        }
	        
	        if (world.getBiome(centerX, centerZ).name() == Biome.OCEAN.name()) {
	        	return;
	        }
	                
	        int desiredWidthX = scanner.DesiredWidthX;
	        int desiredWidthZ = scanner.DesiredWidthZ;
	        int desiredDiffY = scanner.DesiredDiff;
	        
	        for (int i = 0; i <= desiredWidthX && valid; i++) {
	        	
	        	for (int j = 0; j <= desiredWidthZ && valid; j++) {
	        	
	        		int checkX = centerX + i;
	        		int checkZ = centerZ + j;
	        		int height = world.getHighestBlockYAt(checkX, checkZ) - 1;

	        		if (_ignoreVegetation) {
	        			height = skipVegetation(checkX, checkZ, height - 1, world);
	        		}
	        		
	        		int minHeight = centerY - desiredDiffY;
	        		int maxHeight = centerY + desiredDiffY;
	        		if (height > maxHeight || height < minHeight) {
	        			return;
	        		}
	        		
	        		Material mat = world.getBlockAt(checkX, height, checkZ).getType();
	        		if (mat == Material.WATER || mat == Material.STATIONARY_WATER || mat == Material.LAVA || mat == Material.STATIONARY_LAVA) {
	        			return;
	        		}
	        	}
	        }

	        // selon le biome, c'est un emplacement qui a du potentiel
	        if (valid) {
	        	GenerationEvent generation = chooseValidStructure(new Location(world, centerX, centerY, centerZ), desiredWidthX, desiredWidthZ);
	        	if (generation != null && _canGenerate) {
	        		_canGenerate = false;
	        		_lastGenerationTime = System.currentTimeMillis() + _globalCoolDown;
	        			
	        		// on cherche le point le plus bas	        		
	        		int lowerHeight = world.getMaxHeight();
	        		for (int i = 0; i < generation.WidthX; i++) {
	        			for (int j = 0; j < generation.WidthZ; j++) {
	        				int height = world.getHighestBlockYAt(centerX + i, centerZ + j);
	        				if (height < lowerHeight) {
	        					lowerHeight = height;
	        				}
	        			}
	        		}
	        		
	        		// on ajuste le niveau plancher
	        		if (lowerHeight < _seaLevel) {
	        			lowerHeight = _seaLevel;
	        		}
	        		
	        		// on update l'info de génération
	        		generation.NbGeneration++;
	        		generation.LastLocation.X = centerX;
	        		generation.LastLocation.Y = lowerHeight;
	        		generation.LastLocation.Z = centerZ;
	        		generation.CoolDown = generation.IntervalCoolDown + System.currentTimeMillis();
	        		
	        		// on genere la structure
	        		_lastGenerationLocation = new Location(world, centerX, lowerHeight, centerZ);
	        		generateStructure(_lastGenerationLocation, generation.Name);
	        		_logger.info("generating structure: " + generation.toString() + " found lowest height at: " + lowerHeight);
	        		writePersistenceGeneration();
	        		
	        		// cooldown pour generer autre chose, evite de surcharger le CPU et de générer un paquet de structure
	        		Executors.newSingleThreadScheduledExecutor().schedule(new GeneratorActivator(this), _globalCoolDown, TimeUnit.MILLISECONDS);
	        		
	        		// on averti les joueurs online
	        		Player[] players = _server.getOnlinePlayers();
	        		for (Player p : players)  {
	        			CustomCompassManager manager = new CustomCompassManager(generation.Name, p);
	        			manager.setCustomLocation(_lastGenerationLocation, ChatColor.AQUA + "A Point Of Interest has been discovered!");
	        		}
	        	}
	        }
		}
	}
	
	public void setActivate(boolean b) {
		_canGenerate = b;
	}
	
	private int skipVegetation(int x, int z, int height, World w) {
		boolean foundGround = false;
		int downHeight = height;
		
		Material mat;
		while (downHeight > _seaLevel && !foundGround) {
			
			mat = w.getBlockAt(x, height, z).getType();
			if (mat == Material.WOOD || mat == Material.LEAVES || mat == Material.GRASS || mat == Material.CACTUS ||
				mat == Material.DEAD_BUSH || mat == Material.PUMPKIN || mat == Material.RED_ROSE || mat == Material.VINE ||
				mat == Material.YELLOW_FLOWER || mat == Material.SUGAR_CANE_BLOCK || mat == Material.SUGAR_CANE ||
				mat == Material.SPECKLED_MELON || mat == Material.SNOW || mat == Material.AIR || mat == Material.LOG ||
				mat == Material.LONG_GRASS) {
				
				downHeight--;
			}
			else {
				foundGround = true;
			}
		}
		
		return downHeight;
	}
	
	private GenerationEvent chooseValidStructure(Location location, int widthX, int widthZ) {
		
		List<GenerationEvent> possible = new ArrayList<GenerationEvent>();
		
		if (_canGenerate && _lastGenerationTime < System.currentTimeMillis()) {

			for (GenerationEvent gen : _generatedStructure) {
				
				double currentFromSpawn = LocationUtil.getDistanceBetween(location, location.getWorld().getSpawnLocation());
				Location lastLocation = new Location(location.getWorld(), gen.LastLocation.X, gen.LastLocation.Y, gen.LastLocation.Z);
				double lastFromSpawn = LocationUtil.getDistanceBetween(lastLocation, location.getWorld().getSpawnLocation());
				
				// la derniere generation est plus proche que la place qu'on vient de trouver
				// et le cooldown est expiré et les dimensions sont corrects
				if (currentFromSpawn > lastFromSpawn && (currentFromSpawn - lastFromSpawn) > gen.MinDistance && gen.CoolDown < System.currentTimeMillis() &&
					gen.WidthX < widthX && gen.WidthZ < widthZ) {
					possible.add(gen);
				}
			}
			
			Collections.shuffle(possible);
			if (possible.size() > 0) {
				return possible.get(0);	
			}
		}

		// on retourne comme safe guard, on ne peut pas generer pour l'instant, cooldown!
		return null;
	}
	
	private void writePersistenceGeneration() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(WORKING_DIR + "generation.config"));
			for (GenerationEvent gen : _generatedStructure) {
				writer.append(gen.toString() + System.getProperty("line.separator"));
			}
			writer.flush();
			writer.close();
		}
		catch (Exception ex) {
			_logger.info("exception occured while persisting generation");
			ex.printStackTrace();
		}
	}
	
	public void resetPersistenceGeneration() {
		for (GenerationEvent event : _generatedStructure) {
			event.LastLocation = new PinPoint(0,0,0);
			event.NbGeneration = 0;
			event.CoolDown = 0;
		}
		writePersistenceGeneration();
	}
	
	private HashSet<GenerationEvent> readGeneratedStructure() {
		_generatedStructure = new HashSet<GenerationEvent>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(WORKING_DIR + "generation.config"));
			String line;
			
			while ((line = reader.readLine()) != null) {
				GenerationEvent event = new GenerationEvent(line);
				_generatedStructure.add(event);
			}
		}
		catch (Exception ex) {
			_logger.info("exception while reading generated structure!");
			ex.printStackTrace();			
		}
		
		_logger.info("found " + _generatedStructure.size() + " structure to generate!");
		return _generatedStructure;
	}
	
	private HashMap<String, BiomeScanner> readBiomeScanner() {
		
		HashMap<String, BiomeScanner> biomes = new HashMap<String, BiomeScanner>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(WORKING_DIR + "biomes.config"));
			String line;
			
			while ((line = reader.readLine()) != null) {
				BiomeScanner scan = new BiomeScanner(line);
				biomes.put(scan.Name, scan);
			}
			
		}
		catch (Exception ex) {
			_logger.info("exception occured while reading biome config");
			ex.printStackTrace();
		}

		return biomes;
	}
	
	private BiomeScanner getBiomeScanner(String name)  {
		
		if (_useCustomScanner) {
			return _customScanner;	
		}
		
		if (_scanners.containsKey(name)) {
			return _scanners.get(name);
		}

		return _defaultScanner;
	}
	
	public void generateStructure(Location location, String file) {
		
		StructurePattern pattern = readFromFile(file);
		this.generateStructure(pattern, location);
	}
	
	public void generateStructure(StructurePattern pattern, Location location) {
		
		if (pattern != null) {
			// pogne un event au hasard
			Event event = chooseRandomEvent(pattern.ConfigEvents);

			if (event != null) {
				pattern.attachEvent(event);
				writePersistentEvent(event.getName(), pattern.Name, location, event.getConfiguration());
			}
			
			// on genere la structure
			pattern.generate(location);
		}
	}
	
	public StructurePattern readFromFile(String file) {
		
		StructurePattern pattern = new StructurePattern();
		try {
			pattern.Name = file;
			pattern.readFromFile(WORKING_DIR + file);
		}
		catch (Exception ex) {
			_logger.info("could not read structure from file: " + file);
			ex.printStackTrace();
		}
		
		return pattern;
	}
	
	public Event chooseRandomEvent(HashMap<String, String> event) {
		
		Event e = null;
		
		if (event.size() > 0) {
			int index = RandomUtils.nextInt(event.size());
			String[] keys = event.keySet().toArray(new String[event.keySet().size()]);
			String randKey = keys[index];
			if (event.size() > 0) {
				FactoryEvent factory = new FactoryEvent();
				e = factory.getEvent(randKey);

				if (e != null) {
					e.setServer(_server);
					e.setConfiguration(event.get(randKey));
					_server.getPluginManager().registerEvents(e, _plugin);
				}
			}
		}

		return e;
	}
	/*
	public Event chooseRandomEvent(List<String> event) {
		Collections.shuffle(event);
		Event e = null;

		if (event.size() > 0) {
			FactoryEvent factory = new FactoryEvent();
			e = factory.getEvent(event.get(0));

			if (e != null) {
				e.setServer(_server);
				_server.getPluginManager().registerEvents(e, _plugin);
			}
		}
		
		return e;
	}
	*/
	public void writePersistentEvent(String event, String name, Location location, String config) {
		if (!event.equalsIgnoreCase("")) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(WORKING_DIR + "events.config", true));
				StringBuffer buf = new StringBuffer();
				buf.append(event + ":" + name + ":" + location.getX() + "," + location.getY() + "," + location.getZ() + ":" + config);
				buf.append(System.getProperty("line.separator"));
				writer.write(buf.toString());
				writer.flush();
				writer.close();
			}
			catch (Exception ex) {
				_logger.info("exception occured when trying to register generation and events");
				ex.printStackTrace();
			}	
		}
	}
	
	public void setActive(boolean active) {
		_canGenerate = active;
	}
	
	public boolean getActive() {
		return _canGenerate;
	}
	
	public void setHeightDifferential(int diff) {
		_customScanner.DesiredDiff = diff;
	}
	
	public int getHeightDifferential() {
		return _customScanner.DesiredDiff;
	}
	
	public int getWidthX() {
		return _customScanner.DesiredWidthX;
	}

	public void setWidthX(int _widthX) {
		_customScanner.DesiredWidthX = _widthX;
	}

	public int getWidthZ() {
		return _customScanner.DesiredWidthZ;
	}

	public void setWidthZ(int _widthZ) {
		_customScanner.DesiredWidthZ = _widthZ;
	}

	public boolean getUseCustomScanner() {
		return _useCustomScanner;
	}

	public void setUseCustomScanner(boolean use) {
		_useCustomScanner = use;
	}
	
	public double getMinDistanceFromLastGen() {
		return _minDistanceFromLastGeneration;
	}

	public void setMinDistanceFromLastGen(double use) {
		_minDistanceFromLastGeneration = use;
	}
	
	public long getGlobalCoolDown() {
		return _globalCoolDown;
	}

	public void setGlobalCoolDown(long use) {
		_globalCoolDown = use;
	}
}

