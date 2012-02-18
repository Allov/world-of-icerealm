package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class CompassPlayerObserver implements TimeObserver 
{
	public final Logger logger = Logger.getLogger(("Minecraft"));

	private long alarm;
	private Player player = null;
	private Player pointingPlayer = null;
	private boolean active = true;

	public CompassPlayerObserver(Player player, Player pointingPlayer)
	{
		setPlayer(player);
		setPointingPlayer(pointingPlayer);
	}
	
	@Override
	public void timeHasCome(long time) 
	{
		if (active && player.isOnline())
		{
			CompassPlayersInfo compassPlayersInfo = CompassPlayersInfo.getInstance();
			PlayerCompassData compassData = compassPlayersInfo.getPlayerCompassData(getPlayer().getName());
			
			if (compassData.getCurrentCompassMode() == CompassMode.Player)
			{
				boolean found = false;
				for (int i = 0; i < getPointingPlayer().getServer().getOnlinePlayers().length; i++)
				{
					// Validate if player still exists
					if (getPointingPlayer().getServer().getOnlinePlayers()[i].getName().equalsIgnoreCase(compassData.getCurrentPlayerModePlayerName()))
					{
						getPlayer().setCompassTarget(getPlayer().getServer().getPlayer(compassData.getCurrentPlayerModePlayerName()).getLocation());
		    			
		    			// Re-Register
						//TimeServer.getInstance().removeListener(this, AdvancedCompass.PLAYER_MODE_INTERVAL);
		    			TimeServer.getInstance().addListener(this, AdvancedCompass.PLAYER_MODE_INTERVAL);
		    			found = true;
		    			break;
					}
				}	
				
				// Player left, unregister this listener
				
				if (!found)
				{
					getPlayer().sendMessage(ChatColor.RED + ">> Compass pointing player has gone offline");
				}
			}
		}
	}
	
	@Override
	public void setAlaram(long time) 
	{
		this.alarm = time;
	}

	@Override
	public long getAlarm() 
	{
		return this.alarm;
	}

	public Player getPlayer() 
	{
		return player;
	}

	public void setPlayer(Player player) 
	{
		this.player = player;
	}

	public Player getPointingPlayer() 
	{
		return pointingPlayer;
	}

	public void setPointingPlayer(Player pointingPlayer) 
	{
		this.pointingPlayer = pointingPlayer;
	}
}
