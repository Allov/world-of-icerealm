package ca.qc.icerealm.bukkit.plugins.dreamworld;

public class PinPoint {
	public String Name;
	public int X;
	public int Y;
	public int Z;
	
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
}
