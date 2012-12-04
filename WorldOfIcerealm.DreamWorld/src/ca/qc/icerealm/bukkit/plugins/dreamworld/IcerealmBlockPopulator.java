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
import java.util.logging.Logger;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.java.JavaPlugin;

import ca.qc.icerealm.bukkit.plugins.common.LocationUtil;
import ca.qc.icerealm.bukkit.plugins.dreamworld.events.Event;
import ca.qc.icerealm.bukkit.plugins.dreamworld.events.FactoryEvent;
import ca.qc.icerealm.bukkit.plugins.dreamworld.scanner.BiomeScanner;
import ca.qc.icerealm.bukkit.plugins.dreamworld.scanner.CustomScanner;
import ca.qc.icerealm.bukkit.plugins.dreamworld.scanner.DefaultBiome;
import ca.qc.icerealm.bukkit.plugins.dreamworld.scanner.DesertBiome;
import ca.qc.icerealm.bukkit.plugins.dreamworld.scanner.ExtremeHillBiome;
import ca.qc.icerealm.bukkit.plugins.dreamworld.scanner.ForestBiome;
import ca.qc.icerealm.bukkit.plugins.dreamworld.scanner.JungleBiome;
import ca.qc.icerealm.bukkit.plugins.dreamworld.scanner.MarshBiome;
import ca.qc.icerealm.bukkit.plugins.dreamworld.scanner.PlainBiome;


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
	private CustomScanner _customScanner;
	private boolean _useCustomScanner = false;
	private BiomeScanner _defaultScanner;
	private Location _lastGenerationLocation;
	private double _minDistanceFromLastGeneration = 0;
	private double _globalCoolDown = 0;
	private double _lastGenerationTime = 0;
	
		
	public IcerealmBlockPopulator(Server s, JavaPlugin j) {
		_server = s;
		_plugin = j;
		_generatedStructure = readGeneratedStructure();
		_seaLevel = s.getWorld("world").getSeaLevel();
		_scanners = fillBiomeScanner();
		_customScanner = new CustomScanner();
		_defaultScanner = new DefaultBiome();
		_globalCoolDown = 60000; // 1 minute
		
		_minDistanceFromLastGeneration = 0; // 500m
		_lastGenerationLocation = s.getWorld("world").getSpawnLocation();
	}
	
	@Override
	public void populate(World world, Random random, Chunk source) {

		if (_canGenerate && _lastGenerationTime < System.currentTimeMillis()) {
			boolean valid = true;
			int centerX = (source.getX() << 4) + random.nextInt(16);
	        int centerZ = (source.getZ() << 4) + random.nextInt(16);
	        int centerY = world.getHighestBlockYAt(centerX, centerZ) - 1;
	        //_logger.info("source block is: " + world.getBlockAt(centerX, centerY, centerZ).getType());

	        Location currentLocation = new Location(world, centerX, centerY, centerZ);
	        if (LocationUtil.getDistanceBetween(currentLocation, _lastGenerationLocation) <= _minDistanceFromLastGeneration) {
	        	valid = false;
	        }

	        BiomeScanner scanner = getBiomeScanner(world.getBiome(centerX, centerZ).name());
	        //_logger.info(scanner.getClass().toString());
	        
	        if (valid && centerY < world.getSeaLevel()) {
	        	return;
	        }
	        
	        if (valid && world.getHighestBlockAt(centerX, centerZ).getType() == Material.WATER) {
	        	return;
	        }
	        
	        if (valid && world.getBiome(centerX, centerZ).name() == Biome.OCEAN.name()) {
	        	return;
	        }
	                
	        int desiredWidthX = scanner.getDesiredWidthX();
	        int desiredWidthZ = scanner.getDesiredWidthZ();
	        int desiredDiffY = scanner.getDesiredDiff();
	        int minHeightFound = centerY;
	        
	        for (int i = 0; i < desiredWidthX && valid; i++) {
	        	
	        	for (int j = 0; j < desiredWidthZ && valid; j++) {
	        	
	        		int checkX = centerX + i;
	        		int checkZ = centerZ + j;
	        		int height = world.getHighestBlockYAt(checkX, checkZ) - 1;

	        		if (_ignoreVegetation) {
	        			height = skipVegetation(checkX, checkZ, height - 1, world);
	        		}
	        		
	        		int minHeight = centerY - desiredDiffY;
	        		int maxHeight = centerY + desiredDiffY;
	        		//_logger.info("height found was: " + height + " max: " + maxHeight + " min: " + minHeight);
	        		if (height > maxHeight || height < minHeight) {
	        			return;
	        			//_logger.info("not valid: " + height);
	        		}
	        		
	        		if (centerY < minHeightFound) {
	        			minHeightFound = centerY;
	        		}
	        	}
	        }
	        
	        if (valid) {
	        	
	        	GenerationEvent generation = chooseValidStructure(new Location(world, centerX, minHeightFound, centerZ), desiredWidthX, desiredWidthZ);
	        	if (generation != null) {
	        		
	        		
	        		
	        		generation.NbGeneration++;
	        		generation.LastLocation.X = centerX;
	        		generation.LastLocation.Y = minHeightFound;
	        		generation.LastLocation.Z = centerZ;
	        		generation.CoolDown = generation.IntervalCoolDown + System.currentTimeMillis();
	        		_lastGenerationLocation = new Location(world, centerX, world.getSeaLevel() - 1, centerZ);
	        		generateStructure(_lastGenerationLocation, generation.Name);
	        		_lastGenerationTime = System.currentTimeMillis() + _globalCoolDown;
	        		
	        		if (_minDistanceFromLastGeneration == 0) {
	        			_minDistanceFromLastGeneration = 500;
	        		}
	        		
	        		_logger.info("generating structure: " + generation.toString() + " found lowest height at: " + minHeightFound + " and sealevel = " +  world.getSeaLevel());
	        		writePersistenceGeneration();
	        	}
	        }
		}
	}
	
	private int skipVegetation(int x, int z, int height, World w) {
		boolean foundGround = false;
		int downHeight = height;
		
		//_logger.info("found a " + w.getBlockAt(x, height, z).getType());
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
				//_logger.info("found ground at " + x + ", " + z + ", " + downHeight + " Mat = " + mat.name() + " Biome: " + w.getBiome(x, z));
			}
		}
		
		return downHeight;
		
	}
	
	private GenerationEvent chooseValidStructure(Location location, int widthX, int widthZ) {
		
		List<GenerationEvent> possible = new ArrayList<GenerationEvent>();
		
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
			
		}
		return _generatedStructure;
	}
	
	private HashMap<String, BiomeScanner> fillBiomeScanner() {
		HashMap<String, BiomeScanner> biomes = new HashMap<String, BiomeScanner>();
		biomes.put("default", new DefaultBiome());
		biomes.put(Biome.FOREST.name(), new ForestBiome());
		biomes.put(Biome.JUNGLE.name(), new JungleBiome());
		biomes.put(Biome.DESERT.name(), new DesertBiome());
		biomes.put(Biome.MUSHROOM_ISLAND.name(), new MarshBiome());
		biomes.put(Biome.SWAMPLAND.name(), new MarshBiome());
		biomes.put(Biome.TAIGA.name(), new ForestBiome());
		biomes.put(Biome.PLAINS.name(), new PlainBiome());
		biomes.put(Biome.TAIGA_HILLS.name(), new ExtremeHillBiome());
		biomes.put(Biome.EXTREME_HILLS.name(), new ExtremeHillBiome());
		biomes.put(Biome.DESERT_HILLS.name(), new ExtremeHillBiome());
		biomes.put(Biome.JUNGLE_HILLS.name(), new ExtremeHillBiome());
		biomes.put(Biome.SMALL_MOUNTAINS.name(), new ExtremeHillBiome());
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
			_logger.info(pattern.Events + "");
			Event event = chooseRandomEvent(pattern.Events);

			if (event != null) {
				pattern.attachEvent(event);
				writePersistentEvent(event.getName(), pattern.Name, location);
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
	
	public Event chooseRandomEvent(List<String> event) {
		Collections.shuffle(event);
		Event e = null;
		_logger.info("event size: " + event.size());
		
		if (event.size() > 0) {
			FactoryEvent factory = new FactoryEvent();
			e = factory.getEvent(event.get(0));
			if (e == null) {
				_logger.info("event is null");
			}
			
			if (e != null) {
				e.setServer(_server);
				_server.getPluginManager().registerEvents(e, _plugin);
			}
		}
		
		return e;
	}
	
	public void writePersistentEvent(String event, String name, Location location) {
		if (!event.equalsIgnoreCase("")) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(WORKING_DIR + "events.config", true));
				StringBuffer buf = new StringBuffer();
				buf.append(event + ":" + name + ":" + location.getX() + "," + location.getY() + "," + location.getZ());
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
		_customScanner.setDesiredDiff(diff);
	}
	
	public int getHeightDifferential() {
		return _customScanner.getDesiredDiff();
	}
	
	public int getWidthX() {
		return _customScanner.getDesiredWidthX();
	}

	public void setWidthX(int _widthX) {
		_customScanner.setDesiredWidthX(_widthX);
	}

	public int getWidthZ() {
		return _customScanner.getDesiredWidthZ();
	}

	public void setWidthZ(int _widthZ) {
		_customScanner.setDesiredWidthZ(_widthZ);
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
	
	public double getGlobalCoolDown() {
		return _globalCoolDown;
	}

	public void setGlobalCoolDown(double use) {
		_globalCoolDown = use;
	}
}

