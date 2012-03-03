package ca.qc.icerealm.bukkit.plugins.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MapWrapper {
	public final Logger logger = Logger.getLogger(("RareMobsFactory"));
	private final Map<String, Object> map;

	public MapWrapper(Map<String, Object> map) {
		this.map = map;		
	}
	
	public Map<String, Object> getMap(){
		return map;
	}
	
	public int getInt(String key, int defaultIfNull) {
		if (!map.containsKey(key)) {
			return defaultIfNull;
		}
		
		int value;
		
		try	{
			value = Integer.parseInt(map.get(key).toString());
		} catch(Exception exception) {
			Logger.getLogger("MapWrapper").severe("Key: " + key + " is not a valid integer.");
			value = defaultIfNull;
		}
		
		return value;
	}
	
	public String getString(String key, String defaultIfNull) {
		if (!map.containsKey(key)) {
			return defaultIfNull;
		}
		
		return map.get(key).toString();
	}

	public boolean getBoolean(String key, boolean defaultIfNull) {
		if (!map.containsKey(key)) {
			return defaultIfNull;
		}
		
		boolean value;

		try	{
			value = Boolean.parseBoolean(map.get(key).toString());
		} catch(Exception exception) {
			Logger.getLogger("MapWrapper").severe("Key: " + key + " is not a valid boolean.");
			value = defaultIfNull;
		}

		return value;
	}

	public double getDouble(String key, double defaultIfNull) 
	{
		if (!map.containsKey(key)) {
			return defaultIfNull;
		}
		
		double value;
		
		try	{
			value = Double.parseDouble(map.get(key).toString());
		} catch(Exception exception) {
			Logger.getLogger("MapWrapper").severe("Key: " + key + " is not a valid double.");
			value = defaultIfNull;
		}
		
		return value;
	}

	
	public List<MapWrapper> getMapList(String path, List<MapWrapper> defaultIfNull) {
		if (map.get(path) == null) {
			return defaultIfNull;
		}
		
		List<MapWrapper> mapValues = null;
		
		try	{
			ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) map.get(path);
			mapValues = new ArrayList<MapWrapper>();
			
			for (Map<String, Object> map : list) {
				mapValues.add(new MapWrapper(map));
			}
		} catch(Exception exception) {
			Logger.getLogger("MapWrapper").severe("Path: " + path + " is not a valid list of map.");
			mapValues = defaultIfNull;
		}
		
		return mapValues;
	}
}
