package ca.qc.icerealm.bukkit.plugins.common;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.minecraft.server.Material;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

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
	
	public Location getRandomHighestLocation(World w) {
		double topLeftX = 0;
		double topLeftZ = 0;
		double bottomRightX = getRelativeBottomRight().getX();
		double bottomRightZ = getRelativeBottomRight().getZ();
		double tlX = RandomUtil.getRandomDouble(topLeftX, bottomRightX);
		double tlZ = RandomUtil.getRandomDouble(topLeftZ, bottomRightZ);
		tlX += getTopLeft().getX();
		tlZ += getTopLeft().getZ();
		
		Location loc = new Location(w, tlX, _leftTop.getY(), tlZ);
		Location l = loc.clone();
		Block b = w.getHighestBlockAt(l);
		int down = 0;
		double newY = loc.getY();
		while (b.getType() == org.bukkit.Material.AIR && newY > _rightBottom.getY()) {
			down++;
			l = new Location(loc.getWorld(), loc.getX(), newY - down, loc.getZ());
			b = w.getBlockAt(l);
		}
		
		l.setY(l.getY() + 1);
		return l;
	}
	
	public Location getRandomLowestLocation(World w) {
		double topLeftX = 0;
		double topLeftZ = 0;
		double bottomRightX = getRelativeBottomRight().getX();
		double bottomRightZ = getRelativeBottomRight().getZ();
		double tlX = RandomUtil.getRandomDouble(topLeftX, bottomRightX);
		double tlZ = RandomUtil.getRandomDouble(topLeftZ, bottomRightZ);
		tlX += getTopLeft().getX();
		tlZ += getTopLeft().getZ();
		
		Location loc = new Location(w, tlX, _rightBottom.getY(), tlZ);
		Location l = loc.clone();
		Block b = w.getBlockAt(loc);
		int up = 0;
		double newY = loc.getY() + up;
		while (b.getType() != org.bukkit.Material.AIR && newY < _leftTop.getY()) {
			up++;
			l = new Location(loc.getWorld(), loc.getX(), newY + up, loc.getZ());
			b = w.getBlockAt(l);
		}
				
		return l;
	}
	
	public List<Location> getFourCorner(int radius) {
		List<Location> list = new ArrayList<Location>();
		
		Location topLeft = new Location(_leftTop.getWorld(), _leftTop.getX() + radius, _leftTop.getY(), _leftTop.getZ() + radius);
		topLeft.setY(_leftTop.getWorld().getHighestBlockYAt(topLeft));
		
		Location topRight = new Location(_leftTop.getWorld(), _rightBottom.getX() - radius, _leftTop.getY(), _leftTop.getZ() + radius);
		topRight.setY(_leftTop.getWorld().getHighestBlockYAt(topRight));
		
		Location bottomLeft = new Location(_leftTop.getWorld(), _leftTop.getX() + radius, _leftTop.getY(), _rightBottom.getZ() - radius);
		bottomLeft.setY(_leftTop.getWorld().getHighestBlockYAt(bottomLeft));
		
		Location bottomRight = new Location(_leftTop.getWorld(), _rightBottom.getX() - radius, _leftTop.getY(), _rightBottom.getZ() - radius);
		bottomRight.setY(_leftTop.getWorld().getHighestBlockYAt(bottomRight));
		
		
		
		
		list.add(topLeft);
		list.add(topRight);
		list.add(bottomLeft);
		list.add(bottomRight);
		
		return list;
	}
	
	public List<Location> getFourSide(int radius) {
		List<Location> list = new ArrayList<Location>();
		
		Location top = new Location(_leftTop.getWorld(), _leftTop.getX() + ((_rightBottom.getX() - _leftTop.getX()) / 2), _leftTop.getY(), _leftTop.getZ() + radius);
		top.setY(top.getWorld().getHighestBlockYAt(top));
		
		Location right = new Location(_rightBottom.getWorld(), _rightBottom.getX() - radius, _rightBottom.getY(), _rightBottom.getZ() - ((_rightBottom.getZ() - _leftTop.getZ()) / 2));
		right.setY(right.getWorld().getHighestBlockYAt(right));
		
		Location down = new Location(_rightBottom.getWorld(), _rightBottom.getX() - ((_rightBottom.getX() - _leftTop.getX()) / 2), _rightBottom.getY(), _rightBottom.getZ() - radius);
		down.setY(down.getWorld().getHighestBlockYAt(down));
		
		Location left = new Location(_rightBottom.getWorld(), _leftTop.getX() + radius, _leftTop.getY(), _rightBottom.getZ() - ((_rightBottom.getZ() - _leftTop.getZ()) / 2));
		left.setY(left.getWorld().getHighestBlockYAt(left));
		
		list.add(top);
		list.add(right);
		list.add(left);
		list.add(down);		
		
		return list;
	}
		
	@Override 
	public String toString() {
		return _leftTop.getX() + "," + _leftTop.getZ() + "," + 
			   _rightBottom.getX() + "," + _rightBottom.getZ() + "," + 
			   _rightBottom.getY() + "," + _leftTop.getY();
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

