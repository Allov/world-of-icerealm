package ca.qc.icerealm.bukkit.plugins.dreamworld;

import java.io.Serializable;

public class PinPoint implements Serializable {
	private static final long serialVersionUID = -89092158402917803L;
	public String Name;
	public int X;
	public int Y;
	public int Z;

	public PinPoint(String s) {
		try {
			String[] coord = s.split(",");
			if (coord.length == 3) {
				X = Integer.parseInt(coord[0]);
				Y = Integer.parseInt(coord[1]);
				Z = Integer.parseInt(coord[2]);
			}
		}
		catch (Exception ex)  {
			X = 0;
			Y = 0;
			Z = 0;
			Name = "";
		}
	
	}
	
	public PinPoint() {
		X = 0;
		Y = 0;
		Z = 0;
		Name = "";
	}
	
	public PinPoint(int x, int y, int z) {
		X = x;
		Y = y;
		Z = z;
		Name = "";
	}
	
	public PinPoint(String name, int x, int y, int z) {
		X = x;
		Y = y;
		Z = z;
		Name = name;
	}
	
	public PinPoint(double x, double y, double z) {
		X = (int)x;
		Y = (int)y;
		Z = (int)z;
		Name = "";
	}
	
	@Override
	public String toString() {
		return X + "," + Y + "," + Z;
	}
}
