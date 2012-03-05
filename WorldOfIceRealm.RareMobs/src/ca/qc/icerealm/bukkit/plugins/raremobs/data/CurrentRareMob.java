package ca.qc.icerealm.bukkit.plugins.raremobs.data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class CurrentRareMob 
{
	private static final CurrentRareMob instance = new CurrentRareMob();
	private RareMob rareMob = null;
	
	private long rareMobEntityId = -1;
	private List<Integer> subordinatesEntityId = new CopyOnWriteArrayList<Integer>();
	private long timeSpawned = 0;

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

	public List<Integer> getSubordinatesEntityId() 
	{
		return subordinatesEntityId;
	}

	public void setSubordinatesEntityId(List<Integer> subordinatesEntityId) 
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

	public long getTimeSpawned() 
	{
		return timeSpawned;
	}

	public void setTimeSpawned(long timeSpawned) 
	{
		this.timeSpawned = timeSpawned;
	}
	
	public void clear()
	{
    	setRareMob(null);
    	setRareMobEntityId(-1);
    	setSubordinatesEntityId(null);
	}
}
