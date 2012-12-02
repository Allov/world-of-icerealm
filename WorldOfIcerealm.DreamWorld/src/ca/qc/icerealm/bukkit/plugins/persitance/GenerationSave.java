package ca.qc.icerealm.bukkit.plugins.persitance;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Logger;

import org.bukkit.World;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;

public class GenerationSave {

	private Logger _logger = Logger.getLogger("Minecraft");
	private static GenerationSave _instance;
	private final String WORKING_DIR = "plugins" + System.getProperty("file.separator") + "WoI.DreamWorld" + System.getProperty("file.separator");
	private final String NEW_LINE = System.getProperty("line.separator");
	private final String ACTIVATION_ZONE_FILE = "activation_zone";
	private ZoneServer _zoneServer;
	
	protected GenerationSave() {
		_zoneServer = ZoneServer.getInstance();
	}
	
	public GenerationSave getInstance() {
		if (_instance == null) {
			_instance = new GenerationSave();
		}
		
		return _instance;
	}
	
	public void persistActivationZone(WorldZone zone) {
		try {
			BufferedWriter input = new BufferedWriter(new FileWriter(WORKING_DIR + ACTIVATION_ZONE_FILE, true));
			input.append(zone.toString() + NEW_LINE);
			input.flush();
			input.close();
		}
		catch (Exception ex) {
			_logger.info("exception occured when persisting an activation zone");
			ex.printStackTrace(System.err);
		}
	}
	
	public void loadActivationZone(World w) {
		try {
			/*
			File f = new File(WORKING_DIR + ACTIVATION_ZONE_FILE);
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line;
			
			while ((line = reader.readLine()) != null) {
				
				
				
				
			}
			*/
			
		}
		catch (Exception ex) {
			_logger.info("exception occured when reading activation zone");
			ex.printStackTrace(System.err);
		}
	}
	
}
