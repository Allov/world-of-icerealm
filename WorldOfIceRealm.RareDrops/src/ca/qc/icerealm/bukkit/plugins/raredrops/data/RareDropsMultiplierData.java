package ca.qc.icerealm.bukkit.plugins.raredrops.data;

import java.util.Hashtable;

public class RareDropsMultiplierData 
{
    private static final RareDropsMultiplierData instance = new RareDropsMultiplierData();
    private Hashtable<Integer, RareDropsMultipliers> entityMutipliers = new Hashtable<Integer, RareDropsMultipliers>();

    private RareDropsMultiplierData() 
    {

    }
   
    public static synchronized RareDropsMultiplierData getInstance() 
    {
        return instance;
    }

	public Hashtable<Integer, RareDropsMultipliers> getEntityMutipliers() 
	{
		return entityMutipliers;
	}
	
	public boolean entityIsHandled(int entityId)
	{
		return entityMutipliers.get(entityId) != null;
	}
	
	public void setEntityMutipliers(Hashtable<Integer, RareDropsMultipliers> entityMutipliers) 
	{
		this.entityMutipliers = entityMutipliers;
	}
	
	public void addEntityRareDropsMultiplier(int entityId, RareDropsMultipliers multipliers)
	{
		this.entityMutipliers.put(entityId, multipliers);
	}
	
	public void removeEntityRareDropsMultiplier(int entityId)
	{
		this.entityMutipliers.remove(entityId);
	}
}
