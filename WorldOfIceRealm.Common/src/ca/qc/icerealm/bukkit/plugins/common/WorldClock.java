package ca.qc.icerealm.bukkit.plugins.common;

import java.util.logging.Logger;

import org.bukkit.World;

public class WorldClock {

	public final static Logger logger = Logger.getLogger(("Minecraft"));
	
	/**
	 * This method is deprecated and lacks precision. Since an instance of
	 * world is necessary, simply use the method getTime() instead.
	 */
	@Deprecated 
	public static int getHour(World w) {
		return (int)(w.getTime() / 1000);
	}	
}
