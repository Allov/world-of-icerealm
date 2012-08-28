package ca.qc.icerealm.bukkit.plugins.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigWrapper {
	
	private final FileConfiguration config;

	public ConfigWrapper(FileConfiguration config) {
		this.config = config;	
	}
	
	public boolean exists(String path) {
		return getConfig().get(path) != null;
	}
	
	public int getInt(String path, int defaultIfNull) {
		if (getConfig().get(path) == null) {
			return defaultIfNull;
		}
		
		return getConfig().getInt(path);			
	}
	
	public String getString(String path, String defaultIfNull) {
		if (getConfig().get(path) == null) {
			return defaultIfNull;
		}
		
		return getConfig().getString(path);
	}
	
	public boolean getBoolean(String path, boolean defaultIfNull) {
		if (getConfig().get(path) == null) {
			return defaultIfNull;
		}
		
		return getConfig().getBoolean(path);
	}
	
	public long getLong(String path, long defaultIfNull) {
		if (getConfig().get(path) == null) {
			return defaultIfNull;
		}
		
		return getConfig().getLong(path);
	}

	public List<MapWrapper> getMapList(String path, List<MapWrapper> defaultIfNull) {
		if (getConfig().get(path) == null) {
			return defaultIfNull;
		}
		
		List<MapWrapper> mapValues = new ArrayList<MapWrapper>();
		
		for (Map<?, ?> map : getConfig().getMapList(path)) {
			mapValues.add(new MapWrapper(map));
		}
		
		return mapValues;
	}

	public FileConfiguration getConfig() {
		return config;
	}
}
