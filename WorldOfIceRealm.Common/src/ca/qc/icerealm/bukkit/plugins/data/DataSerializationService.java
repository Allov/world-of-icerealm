package ca.qc.icerealm.bukkit.plugins.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Logger;

public class DataSerializationService implements DataPersistenceService 
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	public final String FILE_EXTENSION = "woir";
	public final String FILE_SEPARATOR = System.getProperty("file.separator");
	public final String PLUGINS_FOLDER = "plugins";
	
	
	@Override
	public boolean save(String pluginName, String key, Object data)
	{
		boolean success = false;
		testFolder(pluginName);
		
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		
		try 
		{
			fos = new FileOutputStream(PLUGINS_FOLDER + FILE_SEPARATOR + pluginName + FILE_SEPARATOR + key + "." + FILE_EXTENSION);
			out = new ObjectOutputStream(fos);
			out.writeObject(data);
			out.close();
			fos.close();
			
			success = true;
		}
		catch (Exception e)
		{
			logger.throwing(DataSerializationService.class.getName(), "save", e);
		}
		finally
		{
			try
			{
				out.close();
				fos.close();
			}
			catch (Exception ee)
			{
				
			}
		}

		return success;
	}

	@Override
	public boolean exists(String pluginName, String key) 
	{
		testFolder(pluginName);
		
		File f = new File(PLUGINS_FOLDER + FILE_SEPARATOR + pluginName + FILE_SEPARATOR + key + "." + FILE_EXTENSION);
		
		if(f.exists())
		{ 
			return true;
		}
		
		return false;
	}

	@Override
	public Object load(String pluginName, String key) 
	{
		Serializable data = null;
		testFolder(pluginName);

		FileInputStream fis = null;
		ObjectInputStream in = null;
		
		try 
		{
			File ff = new File(PLUGINS_FOLDER + FILE_SEPARATOR + pluginName + FILE_SEPARATOR + key + "." + FILE_EXTENSION);
			fis = new FileInputStream(ff);
			in = new ObjectInputStream(fis);
			data = (Serializable) in.readObject();
		} 
		catch (Exception e) 
		{
			logger.throwing(DataSerializationService.class.getName(), "load", e);
		} 
		finally
		{
			try
			{
				in.close();
				fis.close();
			}
			catch (Exception ee)
			{
				
			}
		}
		
		return data;
	}

	private void testFolder(String plugin)
	{
		File f = new File(PLUGINS_FOLDER + FILE_SEPARATOR + plugin);
		
		if(!f.exists())
		{ 
			f.mkdirs();
		}
	}
}
