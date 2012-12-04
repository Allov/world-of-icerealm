package ca.qc.icerealm.bukkit.plugins.dreamworld.scanner;

public class CustomScanner implements BiomeScanner {

	private int x = 0;
	private int y = 0;
	private int z = 0;
	
	@Override
	public int getDesiredWidthX() {
		return x;
	}

	@Override
	public int getDesiredWidthZ() {
		return z;
	}

	@Override
	public int getDesiredDiff() {
		return y;
	}
	
	public void setDesiredWidthX(int width) {
		x = width;
	}

	public void setDesiredWidthZ(int width) {
		z = width;
	}
	
	public void setDesiredDiff(int width) {
		y = width;
	}

}
