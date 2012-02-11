package ca.qc.icerealm.bukkit.plugins.common;

import java.util.logging.Logger;

import org.bukkit.World;

public class WorldClock {

	public final static Logger logger = Logger.getLogger(("Minecraft"));
	
	public static int getHour(World w) {
		return (int)(w.getTime() / 1000);
	}
	
}
