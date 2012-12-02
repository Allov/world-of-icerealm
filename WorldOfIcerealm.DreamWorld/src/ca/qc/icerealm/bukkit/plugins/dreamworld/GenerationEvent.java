package ca.qc.icerealm.bukkit.plugins.dreamworld;

import java.util.logging.Logger;

public class GenerationEvent {

	private Logger _logger = Logger.getLogger("Minecraft");
	public PinPoint LastLocation;
	public long CoolDown;
	public String Name;
	public int NbGeneration;
	public int WidthX;
	public int WidthZ;
	public long IntervalCoolDown;
	public int MinDistance;
	
	public GenerationEvent() {
		LastLocation = new PinPoint();
		CoolDown = 0;
		Name = "";
		NbGeneration = 0;
		WidthX = 0;
		WidthZ = 0;
	}
	
	public GenerationEvent(String line) {
		String[] data = line.split(":");
		if (data.length > 7) {
			Name = data[0];
			LastLocation = new PinPoint(data[1]);
			CoolDown = Long.parseLong(data[2]);
			WidthX = Integer.parseInt(data[3]);
			WidthZ = Integer.parseInt(data[4]);
			NbGeneration = Integer.parseInt(data[5]);
			IntervalCoolDown = Long.parseLong(data[6]);
			MinDistance = Integer.parseInt(data[7]);
		}
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Name + ":" + LastLocation + ":" + CoolDown + ":" + WidthX + ":" + WidthZ + ":" + NbGeneration + ":" + IntervalCoolDown + ":" + MinDistance);
		return buffer.toString();
	}
	
}
