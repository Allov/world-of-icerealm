package ca.qc.icerealm.bukkit.plugins.raremobs.data;

import java.util.ArrayList;
import java.util.List;


public class CurrentRareMob 
{
	private static final CurrentRareMob instance = new CurrentRareMob();
	private RareMob rareMob = null;
	
	private long rareMobEntityId = 0;
	private List<Long> subordinatesEntityId = new ArrayList<Long>();

    private CurrentRareMob() 
    {

    }
   
    public static synchronized CurrentRareMob getInstance() 
    {
    	return instance;
    }

	public long getRareMobEntityId() 
	{
		return rareMobEntityId;
	}

	public void setRareMobEntityId(long rareMobEntityId) 
	{
		this.rareMobEntityId = rareMobEntityId;
	}

	public List<Long> getSubordinatesEntityId() 
	{
		return subordinatesEntityId;
	}

	public void setSubordinatesEntityId(List<Long> subordinatesEntityId) 
	{
		this.subordinatesEntityId = subordinatesEntityId;
	}

	public RareMob getRareMob() 
	{
		return rareMob;
	}

	public void setRareMob(RareMob rareMob) 
	{
		this.rareMob = rareMob;
	}
}
