package ca.qc.icerealm.bukkit.plugins.advancedcompass;

import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class CompassPlayerObserver implements TimeObserver 
{
	private long alarm;
	private Player player;
	private Player pointingPlayer;

	@Override
	public void timeHasCome(long time) 
	{
		CompassPlayersInfo compassPlayersInfo = CompassPlayersInfo.getInstance();
		PlayerCompassData compassData = compassPlayersInfo.getPlayerCompassData(player.getName());
		
	//	if (time)
		for (int i = 0; i < pointingPlayer.getServer().getOnlinePlayers().length; i++)
		{
			// Validate if player still exists
			if (pointingPlayer.getServer().getOnlinePlayers()[i].getName().equals(compassData.getCurrentPlayerModePlayerName()))
			{
				pointingPlayer.setCompassTarget(pointingPlayer.getServer().getPlayer(compassData.getCurrentPlayerModePlayerName()).getLocation());
    			
    			// Re-Register
    			TimeServer.getInstance().addListener(this, 5000);
			}
			else
			{
				// Player left, unregister this listener
				
				
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
}
