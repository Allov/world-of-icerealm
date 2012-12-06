package ca.qc.icerealm.bukkit.plugins.dreamworld.events;

import java.io.Serializable;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.event.Listener;

import ca.qc.icerealm.bukkit.plugins.dreamworld.PinPoint;

public interface Event extends Listener {
	public void setSourceLocation(Location source);
	public void setPinPoints(List<PinPoint> points);
	public void setLootPoints(List<PinPoint> loots);
	public void setActivateZone(List<List<PinPoint>> zones);
	public void setWelcomeMessage(String s);
	public void setEndMessage(String s);
	public void setServer(Server s);
	public void activateEvent();
	public void releaseEvent();
	public String getName();
	public void setConfiguration(String config);
	public String getConfiguration();
}
