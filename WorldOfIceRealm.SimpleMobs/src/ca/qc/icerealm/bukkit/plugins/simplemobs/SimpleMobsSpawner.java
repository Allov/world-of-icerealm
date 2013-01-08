package ca.qc.icerealm.bukkit.plugins.simplemobs;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.simplemobs.data.SimpleMob;

public class SimpleMobsSpawner 
{
	private Player player;
	private SimpleMob[] mobs;
	
	public SimpleMobsSpawner(Player p)
	{
		this.player = p;
	}
	
	public SimpleMobsSpawner(Player p, SimpleMob[] m)
	{
		this.player = p;
		this.setMobs(m);
	}

	public Player getPlayer() 
	{
		return player;
	}

	public void setPlayer(Player player) 
	{
		this.player = player;
	}
	
	public SimpleMob[] getMobs() 
	{
		return mobs;
	}

	public void setMobs(SimpleMob[] mobs) 
	{
		this.mobs = mobs;
	}
	
	public void spawnMobs()
	{
        /*double rot = (player.getLocation().getYaw() - 90) % 360;
        if (rot < 0) {
            rot += 360.0;
        }*/
		
		List<Block> blocks = player.getLineOfSight(null, 15);
		Location lastBlock = blocks.get(blocks.size() - 1).getLocation();
		int x = 0;
		
		for (int i = 0; i < mobs.length; i++)
		{
			for (int j = 0; j < mobs[i].getAmount(); j++)
			{		
				lastBlock.setX(lastBlock.getX() + x);
				player.getWorld().spawnEntity(lastBlock, mobs[i].getEntityType());
				x++;
			}
		}
	}
}
