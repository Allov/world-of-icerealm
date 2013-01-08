package ca.qc.icerealm.bukkit.plugins.simplemobs;

import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.EntityType;

import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;
import ca.qc.icerealm.bukkit.plugins.simplemobs.data.SimpleMob;

public class SimpleMobsFactory 
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	
	public SimpleMob[] BuildSimpleMobs(String[] params) throws UnknownEntityException
	{
		StringBuilder fullParams = new StringBuilder();

		for (int i = 0 ; i < params.length; i++)
		{
			if (i != 0)
			{
				fullParams.append(" ");
			}
			
			fullParams.append(params[i]);	
		}
		
		String[] mobParams = fullParams.toString().split(",");	
		SimpleMob[] simpleMobs = new SimpleMob[mobParams.length];
		
		for (int i = 0; i < mobParams.length; i++)
		{	
			String[] singleMobParam = mobParams[i].trim().split(" ");
			simpleMobs[i] = new SimpleMob();
			
			simpleMobs[i].setAmount(Integer.parseInt(singleMobParam[0].trim()));
			EntityType type = EntityUtilities.getEntityType(singleMobParam[1].trim().toUpperCase());
			
			if (type == null )
			{
				throw new UnknownEntityException(singleMobParam[1]);
			}
			
			simpleMobs[i].setEntityType(type);
		}
		
		return simpleMobs;
	}
}
