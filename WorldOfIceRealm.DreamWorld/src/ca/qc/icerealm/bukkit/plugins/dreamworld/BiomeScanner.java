package ca.qc.icerealm.bukkit.plugins.dreamworld;

public class BiomeScanner {
	
	public BiomeScanner() {
		Name = "";
		DesiredWidthX = 1;
		DesiredWidthZ = 1;
		DesiredDiff = 1;
	}
	
	public BiomeScanner(String line) {
		String[] data = line.split(":");
		if (data.length > 3) {
			Name = data[0];
			DesiredWidthX = Integer.parseInt(data[1]);
			DesiredWidthZ = Integer.parseInt(data[2]);
			DesiredDiff = Integer.parseInt(data[3]);
		}
	}
	
	public String Name;
	public int DesiredWidthX;
	public int DesiredWidthZ;
	public int DesiredDiff;
	
	/*
	public int getDesiredWidthX();
	public int getDesiredWidthZ();
	public int getDesiredDiff();
	*/
}
