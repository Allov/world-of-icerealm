package ca.qc.icerealm.bukkit.plugins.dreamworld;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.block.Block;

import ca.qc.icerealm.bukkit.plugins.scenarios.events.Event;
import ca.qc.icerealm.bukkit.plugins.scenarios.events.EventService;
import ca.qc.icerealm.bukkit.plugins.scenarios.tools.PinPoint;

public class StructurePattern {
	
	private final String NEW_LINE = System.getProperty("line.separator");
	private Logger _logger = Logger.getLogger("Minecraft");
	
	public List<List<BlockUnit[]>> Blocks;
	public Integer Column;
	public Integer Row;
	public Integer Layer;
	public List<PinPoint> PinPoints;
	public List<PinPoint> LootPoints;
	public List<List<PinPoint>> ActivationZone;
	public Location Source;
	public String Name;
	public HashMap<String, String> ConfigEvents;
	public Integer GroundLevel = 0;
	
	private Event _event;
	private EventService _eventService;
	
	public StructurePattern() {
		Blocks = new ArrayList<List<BlockUnit[]>>();
		PinPoints = new ArrayList<PinPoint>();
		LootPoints = new ArrayList<PinPoint>();
		ActivationZone = new ArrayList<List<PinPoint>>();
		//Events = new ArrayList<String>();
		ConfigEvents = new HashMap<String,String>();
		Layer = 0;
		Name = "";
		GroundLevel = 0;
		_event = null;
		_eventService = EventService.getInstance();
	}
	
	public StructurePattern(Event e) {
		Blocks = new ArrayList<List<BlockUnit[]>>();
		PinPoints = new ArrayList<PinPoint>();
		LootPoints = new ArrayList<PinPoint>();
		ActivationZone = new ArrayList<List<PinPoint>>();
		ConfigEvents = new HashMap<String,String>();
		Layer = 0;
		Name = "";
		GroundLevel = 0;
		_event = e;
		_eventService = EventService.getInstance();
	}
	
	public void generate(Location location) {
		
		// on commence avec les blocks!
		Source = location;
		if (Blocks.size() > 0) {
			for (int j = 0; j < Blocks.size(); j++) {
	    		List<BlockUnit[]> row = Blocks.get(j);
	    		for (int x = 0; x < row.size(); x++) {       			

	    			BlockUnit[] blocks = row.get(x);
	    			for (int i = 0; i < blocks.length; i++) {
	    				Block block = location.getWorld().getBlockAt((int)location.getX() + i, ((int)location.getY() + j) - GroundLevel, (int)location.getZ() + x);
	       	        	block.setTypeIdAndData(blocks[i].TypeId, blocks[i].Data, true);
	    			}
	    		}
	    	} 
		}
		
		if (_event != null) {
			_event.setSourceLocation(location);
			_event.setPinPoints(PinPoints);
			_event.setActivateZone(ActivationZone);
			_event.setLootPoints(LootPoints);
			_event.setEventArea(Layer, Row, Column);
			_event.activateEvent();
			_eventService.addEvent(_event);
		}
	}
		
	public void attachEvent(Event e) {
		_event = e;
	}
	
	public void readFromFile(String file) throws Exception {
		_logger.info("reading structure file: " + file);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String metadata = reader.readLine();
		String[] firstLine =  metadata.split(" ");
		Layer = Integer.parseInt(firstLine[0]);
		Row = Integer.parseInt(firstLine[1]);
		Column = Integer.parseInt(firstLine[2]);
		GroundLevel =  Integer.parseInt(firstLine[3]);
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
		
		while (!(metadata = reader.readLine()).equalsIgnoreCase("[events]")) {
			
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
		
		while (!(metadata = reader.readLine()).equalsIgnoreCase("[zones]")) {
			String[] line = metadata.split(":");
			
			if (line.length == 1) {
				ConfigEvents.put(line[0], "");
			}
			else if (line.length > 1) {
				ConfigEvents.put(line[0], line[1]);
			}					
		}
		
		while (!(metadata = reader.readLine()).equalsIgnoreCase("[blocks]")) {
			
			String[] line = metadata.split(":");
			List<PinPoint> points = new ArrayList<PinPoint>();
			if (line.length >= 3) {
				PinPoint first = new PinPoint();
				first.Name = line[0];
				
				String[] lowerZone = line[1].split(",");
				if (lowerZone.length == 3) {
					first.X = Integer.parseInt(lowerZone[0]);
					first.Y = Integer.parseInt(lowerZone[1]);
					first.Z = Integer.parseInt(lowerZone[2]);
					points.add(first);
				}
				
				String[] higherZone = line[2].split(",");
				if (higherZone.length == 3) {
					PinPoint higher = new PinPoint();
					higher.X = Integer.parseInt(higherZone[0]);
					higher.Y = Integer.parseInt(higherZone[1]);
					higher.Z = Integer.parseInt(higherZone[2]);
					points.add(higher);
				}
			}
			ActivationZone.add(points);				
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
		buf.append(Layer + " " + Row + " " + Column + " " + GroundLevel + NEW_LINE);
		
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
		
		buf.append("[events]" + NEW_LINE);
	
		for (String key : ConfigEvents.keySet()) {
			buf.append(key + ":" + ConfigEvents.get(key));
			buf.append(NEW_LINE);
		}	
		
		buf.append("[zones]" + NEW_LINE);
		
		for (int i = 0; i < ActivationZone.size(); i++) {
			
			List<PinPoint> loc = ActivationZone.get(i);
			if (loc.size() == 2) {
				buf.append(loc.get(0).Name + ":" + loc.get(0).X + "," + loc.get(0).Y + "," + loc.get(0).Z + ":" + 
						   loc.get(1).X + "," + loc.get(1).Y + "," + loc.get(1).Z);
				buf.append(NEW_LINE);
			}
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
