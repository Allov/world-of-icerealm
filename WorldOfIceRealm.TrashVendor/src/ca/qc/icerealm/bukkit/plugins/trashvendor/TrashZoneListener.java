package ca.qc.icerealm.bukkit.plugins.trashvendor;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.Villager; 
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import ca.qc.icerealm.bukkit.plugins.common.WorldZone;
import ca.qc.icerealm.bukkit.plugins.time.TimeObserver;
import ca.qc.icerealm.bukkit.plugins.time.TimeServer;

public class TrashZoneListener implements TimeObserver, Listener {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private long _alarm;
	private Location _location;
	private Villager _villager;
	private WorldZone _zone;
	
	public TrashZoneListener(Location l, Villager v) {
		_location = l;
		_villager = v;	
		_zone = new WorldZone(l, 1);
	}
	
	@Override
	public void timeHasCome(long time) {
		_villager.teleport(_location);		
		TimeServer.getInstance().addListener(this, 100);
		
	}

	@Override
	public void setAlaram(long time) {
		// TODO Auto-generated method stub
		_alarm = time;
		
	}

	@Override
	public long getAlarm() {
		// TODO Auto-generated method stub
		return _alarm;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void itemDropToVendor(PlayerDropItemEvent event) {
		if (_zone.isInside(event.getItemDrop().getLocation())) {
			
			this.logger.info(event.getItemDrop().getItemStack().getAmount() + " of " + event.getItemDrop().getItemStack().getType().toString());
			this.logger.info("Giving " + event.getItemDrop().getItemStack().getAmount() + " to " + event.getPlayer().getName());
			
			
		}
		
		
		
	}

}
