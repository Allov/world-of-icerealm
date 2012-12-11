package ca.qc.icerealm.bukkit.plugins.raremobs.data;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.raremobs.RareMobZone;
import ca.qc.icerealm.bukkit.plugins.zone.ZoneServer;


public class CurrentRareMob 
{
	private static final CurrentRareMob instance = new CurrentRareMob();
	private RareMob rareMob = null;
	
	private long rareMobEntityId = -1;
	private List<Integer> subordinatesEntityId = new CopyOnWriteArrayList<Integer>();
	private long timeSpawned = 0;
	private Location rareMobLocation = null;
	private List<Player> fighters = new CopyOnWriteArrayList<Player>();
	private RareMobZone raremobZone;
	private int currentHealth = 0;
	private Hashtable<Integer, Double> currentSubordinateHealth = new Hashtable<Integer, Double>();

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
	
	public synchronized Location getRareMobLocation() 
	{
		return rareMobLocation;
	}

	public void setRareMobLocation(Location rareMobLocation) 
	{
		this.rareMobLocation = rareMobLocation;
	}

	public List<Player> getFighters() 
	{
		return fighters;
	}

	public void setFighters(List<Player> fighters) 
	{
		this.fighters = fighters;
	}

	public RareMobZone getRaremobZone() 
	{
		return raremobZone;
	}

	public void setRaremobZone(RareMobZone raremobZone) 
	{
		this.raremobZone = raremobZone;
	}

	public int getCurrentHealth() 
	{
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth) 
	{
		this.currentHealth = currentHealth;
	}

	public Hashtable<Integer, Double> getCurrentSubordinateHealth() 
	{
		return currentSubordinateHealth;
	}

	public void setCurrentSubordinateHealth(Hashtable<Integer, Double> currentSubordinateHealth) 
	{
		this.currentSubordinateHealth = currentSubordinateHealth;
	}
	
	public void clear()
	{
    	setRareMob(null);
    	setRareMobEntityId(-1);
    //	setSubordinatesEntityId(null);
    	setRareMobLocation(null);
    	
    	if (raremobZone != null)
    	{
	    	ZoneServer.getInstance().removeListener(getRaremobZone());
	    	raremobZone = null;
    	}
	}
}
