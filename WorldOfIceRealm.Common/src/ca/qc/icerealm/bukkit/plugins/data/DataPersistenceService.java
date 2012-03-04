package ca.qc.icerealm.bukkit.plugins.data;

public interface DataPersistenceService 
{
	public boolean save(String pluginName, String key, Object data);
	public boolean exists(String pluginName, String key);
	public Object load(String pluginName, String key);
}
