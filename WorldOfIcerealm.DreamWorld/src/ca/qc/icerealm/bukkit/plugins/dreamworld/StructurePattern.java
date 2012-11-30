package ca.qc.icerealm.bukkit.plugins.dreamworld;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StructurePattern {
	
	private final String NEW_LINE = System.getProperty("line.separator");
	private Logger _logger = Logger.getLogger("Minecraft");
	
	public List<List<BlockUnit[]>> Blocks;
	public Integer Column;
	public Integer Row;
	public Integer Layer;
	public List<PinPoint> PinPoints;
	public List<PinPoint> LootPoints;
	public Location Source;
	
	public StructurePattern() {
		Blocks = new ArrayList<List<BlockUnit[]>>();
		PinPoints = new ArrayList<PinPoint>();
		LootPoints = new ArrayList<PinPoint>();
		Layer = 0;
	}
	
	public void generate(Location location) {
		Source = location;
		if (Blocks.size() > 0) {
			for (int j = 0; j < Blocks.size(); j++) {
	    		List<BlockUnit[]> row = Blocks.get(j);
	    		for (int x = 0; x < row.size(); x++) {       			

	    			BlockUnit[] blocks = row.get(x);
	    			for (int i = 0; i < blocks.length; i++) {
	    				Block block = location.getWorld().getBlockAt((int)location.getX() + i, (int)location.getY() + j, (int)location.getZ() + x);
	       	        	block.setTypeIdAndData(blocks[i].TypeId, blocks[i].Data, true);
	    			}
	    		}
	    	} 
		}
		
		if (PinPoints.size() > 0) {
			for (PinPoint p : PinPoints) {
				/*
				Location l = new Location(location.getWorld(), location.getX() + p.X, location.getY() + p.Y, location.getZ() + p.Z);
				Block b = location.getWorld().getBlockAt(l);
				b.setType(Material.GLOWSTONE);
				*/
			}
		}
		
		_logger.info(LootPoints.size() + " loot size");
		for (PinPoint p : LootPoints) {
			Location l = new Location(location.getWorld(), location.getX() + p.X, location.getY() + p.Y, location.getZ() + p.Z);
			Block b = location.getWorld().getBlockAt(l);
			b.setType(Material.CHEST);
			
			if (b.getType() == Material.CHEST) {
				Chest chest = (Chest)b.getState();
				Inventory inv = chest.getInventory();
				inv.addItem(new ItemStack(Material.ANVIL, 1));
			}
			
			
		}
		
	}
	
	public void readFromFile(String file) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String metadata = reader.readLine();
		String[] firstLine =  metadata.split(" ");
		Layer = Integer.parseInt(firstLine[0]);
		Column = Integer.parseInt(firstLine[1]);
		Row = Integer.parseInt(firstLine[2]);
		PinPoints = new ArrayList<PinPoint>();
		
		while (!(metadata = reader.readLine()).equalsIgnoreCase("[loot]")) {
			
			String[] line = metadata.split(":");
			
			PinPoint pt = new PinPoint();
			if (line.length >= 4) {
				pt.Name = line[0];
				pt.X = Integer.parseInt(line[1]);
				pt.Y = Integer.parseInt(line[2]);
				pt.Z = Integer.parseInt(line[3]);
			}
			PinPoints.add(pt);				
		}
		
		while (!(metadata = reader.readLine()).equalsIgnoreCase("[blocks]")) {
			
			String[] line = metadata.split(":");
			
			PinPoint pt = new PinPoint();
			if (line.length >= 4) {
				pt.Name = line[0];
				pt.X = Integer.parseInt(line[1]);
				pt.Y = Integer.parseInt(line[2]);
				pt.Z = Integer.parseInt(line[3]);
			}
			LootPoints.add(pt);
						
		}

		
		

		int currentLayer = 0;
		String newLine;
		int currentRow = 0;

		while (currentLayer < Layer) {
			
			List<BlockUnit[]> singleLayer = new ArrayList<BlockUnit[]>();
			while (currentRow < Row && (newLine = reader.readLine()) != null) {

				String[] line = newLine.split(",");
				BlockUnit[] blocks = new BlockUnit[line.length];
				for (int i = 0; i < line.length; i++) {
					BlockUnit b = new BlockUnit();
					String[] singleBlock = line[i].split(" ");
					b.TypeId = Integer.parseInt(singleBlock[0]);
					b.Data = Byte.parseByte(singleBlock[1]);
					blocks[i] = b;
				}
				singleLayer.add(blocks);
				currentRow++;
			}
			Blocks.add(singleLayer);
			currentRow = 0;
			currentLayer++;
		}

		reader.close();		
	}
	
	@Override
	public String toString() {
		Layer = Blocks.size();
		
		StringBuffer buf = new StringBuffer();
		
		// meta data
		buf.append(Layer + " " + Column + " " + Row + NEW_LINE);
		
		for (int i = 0; i < PinPoints.size(); i++) {
			
			PinPoint loc = PinPoints.get(i);
			buf.append(loc.Name + ":" + loc.X + ":" + loc.Y + ":" + loc.Z + ":");
			buf.append(NEW_LINE);
		}
		
		buf.append("[loot]" + NEW_LINE);
		
		for (int i = 0; i < LootPoints.size(); i++) {
			
			PinPoint loc = LootPoints.get(i);
			buf.append(loc.Name + ":" + loc.X + ":" + loc.Y + ":" + loc.Z + ":");
			buf.append(NEW_LINE);
		}
		
		buf.append("[blocks]" + NEW_LINE);
		
		for (List<BlockUnit[]> list : Blocks) {
			
			for (BlockUnit[] b : list) {
				
				for (int i = 0; i < b.length; i++) {
					
					buf.append(b[i].TypeId + " " + b[i].Data);
					if (i < b.length - 1) {
						buf.append(",");
					}
					else {
						buf.append(NEW_LINE);
					}
				}
			}	
		}
		
		return buf.toString();
	}
	
	
}
