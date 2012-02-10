package ca.qc.icerealm.bukkit.plugins.common;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Permet de définir une zone dans le monde.
 * IMPORTANT: s'assurer que les changements que vous ferez sont backward
 * compatible aka, don't break existing code!!!
 */
public class WorldZone {
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private Location _rightBottom;
	private Location _leftTop;
	
	public WorldZone(Location l, double radius) {
		_leftTop = new Location(l.getWorld(), l.getX() - radius, l.getY(), l.getZ() - radius);
		_rightBottom = new Location(l.getWorld(), l.getX() + radius, l.getY(), l.getZ() + radius);
		_leftTop.setY(128);
		_rightBottom.setY(0);
	}
	
	public WorldZone(Location lt, Location rb) {
		_leftTop = lt;
		_rightBottom = rb;
		_leftTop.setY(128);
		_rightBottom.setY(0);
	}
	
	public WorldZone(Location lt, Location rb, double min, double max) {
		_leftTop = lt;
		_rightBottom = rb;
		_leftTop.setY(max);
		_rightBottom.setY(min);
	}
	
	public WorldZone(World w, String data) {
		buildFromString(w, data);
	}
	
	public boolean isInside(Location position) {

		boolean inside = false;
		
		if (_leftTop.getX() < position.getX() && _leftTop.getZ() < position.getZ() &&
			_rightBottom.getX() > position.getX() && _rightBottom.getZ() > position.getZ() &&
			_leftTop.getY() > position.getY() && _rightBottom.getY() < position.getY() ) {
			inside = true;
		}
		return inside;
	}
	
	@Deprecated
	public boolean isInside(double x1, double y1, double x2, double y2) {
		boolean inside = false;
		
		if (_leftTop.getX() < x1 && _leftTop.getZ() < y1 &&
			_rightBottom.getX() > x2 && _rightBottom.getZ() > y2) {
			inside = true;
		}
		return inside;
	}
	
	public Location getTopLeft() {
		return _leftTop;
	}
	
	public Location getRightBottom() {
		return _rightBottom;
	}
	
	public double getMinHeight() {
		return _rightBottom.getY();
	}
	
	public double getMaxHeight() {
		return _leftTop.getY();
	}
	
	
	public Location getCentralPointAt(double height) {
		double x = (this._leftTop.getX() + this._rightBottom.getX()) / 2;
		double z = (this._leftTop.getZ() + this._rightBottom.getZ()) / 2;
		return new Location(_leftTop.getWorld(), x, height, z);
	}
		
	public List<Location> getRandomLocation(World w, int qty) {
		List<Location> list = new ArrayList<Location>();
		for (int i = 0; i < qty; i++) {
			Location loc = getRandomLocation(w);
			list.add(loc);
		}
		return list;
	}
	
	public Location getRandomLocationOutsideThisZone(World w, WorldZone exclude) {
		Location loc = getRandomLocation(w);

		while (exclude.isInside(loc)) {
			loc = getRandomLocation(w);
		}
		return loc;
	}
	
	public Location getRandomLocation(World w) {
		double topLeftX = 0;
		double topLeftZ = 0;
		double bottomRightX = getRelativeBottomRight().getX();
		double bottomRightZ = getRelativeBottomRight().getZ();
		double tlX = RandomUtil.getRandomDouble(topLeftX, bottomRightX);
		double tlZ = RandomUtil.getRandomDouble(topLeftZ, bottomRightZ);
		tlX += getTopLeft().getX();
		tlZ += getTopLeft().getZ();
		
		Location loc = new Location(w, tlX, _rightBottom.getY(), tlZ);
		double y = w.getHighestBlockYAt(loc);
		if (y >= _rightBottom.getY() && y <= _leftTop.getY()) {
			loc.setY(y);	
		}
		else {
			loc.setY(_rightBottom.getY());
		}
		
		return loc;
	}
		
	@Override 
	public String toString() {
		return "Top Left (" + _leftTop.getX() + "," + _leftTop.getZ() + " Right Bottom (" + _rightBottom.getX() + "," + _rightBottom.getZ();
	}
	
	private Location getRelativeBottomRight() {
		Location relativeBottomRight = new Location(_rightBottom.getWorld(), 
													_rightBottom.getX() - _leftTop.getX(), 
													_rightBottom.getY(), 
													_rightBottom.getZ() - _leftTop.getZ());
		return relativeBottomRight;
	}
	
	private void buildFromString(World w,  String zone) {
		String[] coords = zone.split(",");
		double[] coordsDouble = null;
		
		if (coords.length == 4) {
			coordsDouble = new double[] { Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), 
					   Double.parseDouble(coords[2]), Double.parseDouble(coords[3]), 0.0, 128.0};
		}
		else if (coords.length == 6) {
			coordsDouble = new double[] { Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), 
					   Double.parseDouble(coords[2]), Double.parseDouble(coords[3]),
					   Double.parseDouble(coords[4]), Double.parseDouble(coords[5])};
		}
		
		if (coordsDouble != null) {
			_leftTop = new Location(w, coordsDouble[0], coordsDouble[5], coordsDouble[1]);
			_rightBottom = new Location(w, coordsDouble[2], coordsDouble[4], coordsDouble[3]);
		}
	}
	
	
}
