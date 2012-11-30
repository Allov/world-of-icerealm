package ca.qc.icerealm.bukkit.plugins.dreamworld;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.generator.BlockPopulator;

public class IcerealmBlockPopulator extends BlockPopulator {

	private Logger _logger = Logger.getLogger("Minecraft");
	private final String WORKING_DIR = "plugins" + System.getProperty("file.separator") + "WoI.DreamWorld" + System.getProperty("file.separator");
	private boolean _canGenerate = false;
	private boolean _ignoreVegetation = true;
	private int _seaLevel = 0;
	private double _timestamp = System.currentTimeMillis();
	private double _coolDown = 120000; // 5 minute
		
	public IcerealmBlockPopulator() {
		
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
	        	}
	        }
	        
	        if (valid && System.currentTimeMillis() > _timestamp + _coolDown) {
	        	_logger.info(centerX + ", " + centerZ + " height: " + centerY + " and building a structure");     
	        	_timestamp = System.currentTimeMillis();
	        	generateStructure(new Location(world, centerX, world.getSeaLevel(), centerZ), "castle");
	        	
	        }	
	        else if (valid && System.currentTimeMillis() < _timestamp + _coolDown) {
	        	_logger.info(centerX + ", " + centerZ + " height: " + world.getSeaLevel() + " cool down still active!!!");      
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
	
	public void generateStructure(Location location, String file) {
		
		StructurePattern pattern = readFromFile(WORKING_DIR + file);
		this.generateStructure(pattern, location);
	}
	
	public void generateStructure(StructurePattern pattern, Location location) {
		
		if (pattern != null) {
			pattern.generate(location);
		}
	}
	
	public StructurePattern readFromFile(String file) {
		
		StructurePattern pattern = new StructurePattern();
		
		try {
			pattern.readFromFile(WORKING_DIR + file);
		}
		catch (Exception ex) {
			_logger.info("could not read structure from file: " + file);
			ex.printStackTrace();
		}
		
		return pattern;
	}
}

