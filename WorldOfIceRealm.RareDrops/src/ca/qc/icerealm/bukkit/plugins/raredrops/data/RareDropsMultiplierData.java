package ca.qc.icerealm.bukkit.plugins.raredrops.data;

import java.util.Hashtable;

public class RareDropsMultiplierData 
{
    private static final RareDropsMultiplierData instance = new RareDropsMultiplierData();
    private Hashtable<Integer, Double> entityMutipliers = new Hashtable<Integer, Double>();

    private RareDropsMultiplierData() 
    {

    }
   
    public static synchronized RareDropsMultiplierData getInstance() 
    {
        return instance;
    }

	public Hashtable<Integer, Double> getEntityMutipliers() 
	{
		return entityMutipliers;
	}
	
	public void setEntityMutipliers(Hashtable<Integer, Double> entityMutipliers) 
	{
		this.entityMutipliers = entityMutipliers;
	}
	
	public void addEntityRareDropsMultiplier(int entityId, double multiplier)
	{
		this.entityMutipliers.put(entityId, multiplier);
	}
	
	public void removeEntityRareDropsMultiplier(int entityId)
	{
		this.entityMutipliers.remove(entityId);
	}
}
