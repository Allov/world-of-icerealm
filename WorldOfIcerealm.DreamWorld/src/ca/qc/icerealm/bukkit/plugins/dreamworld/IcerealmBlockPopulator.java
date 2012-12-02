package ca.qc.icerealm.bukkit.plugins.dreamworld;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
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


public class IcerealmBlockPopulator extends BlockPopulator {

	private Logger _logger = Logger.getLogger("Minecraft");
	private final String WORKING_DIR = "plugins" + System.getProperty("file.separator") + "WoI.DreamWorld" + System.getProperty("file.separator");
	private boolean _canGenerate = true;
	private boolean _ignoreVegetation = true;
	private int _seaLevel = 0;
	private Server _server;
	private JavaPlugin _plugin;
	private HashSet<GenerationEvent> _generatedStructure;
		
	public IcerealmBlockPopulator(Server s, JavaPlugin j) {
		_server = s;
		_plugin = j;
		_generatedStructure = readGeneratedStructure();		
	}
	
	@Override
	public void populate(World world, Random random, Chunk source) {

		if (_canGenerate) {
			_seaLevel = world.getSeaLevel();
			boolean valid = true;
			int centerX = (source.getX() << 4) + random.nextInt(16);
	        int centerZ = (source.getZ() << 4) + random.nextInt(16);
	        int centerY = world.getHighestBlockYAt(centerX, centerZ) - 1;
	        //_logger.info("source block is: " + world.getBlockAt(centerX, centerY, centerZ).getType());

	        if (centerY < world.getSeaLevel()) {
	        	valid = false;
	        }
	        
	        if (world.getHighestBlockAt(centerX, centerZ).getType() == Material.WATER) {
	        	valid = false;
	        }
	        
	        if (world.getBiome(centerX, centerZ).name() == Biome.OCEAN.name()) {
	        	valid = false;
	        }
	        
	        // on cherche de quoi de flat de 57,56
	        int desiredWidthX = 70;
	        int desiredWidthZ = 70;
	        int desiredDiffY = 4;
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
	        			valid = false;
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
	        		generateStructure(new Location(world, centerX, minHeightFound, centerZ), generation.Name);
	        		_logger.info("generating structure: " + generation.toString());
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
			if (currentFromSpawn > lastFromSpawn && currentFromSpawn > gen.MinDistance && gen.CoolDown < System.currentTimeMillis() &&
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
}

