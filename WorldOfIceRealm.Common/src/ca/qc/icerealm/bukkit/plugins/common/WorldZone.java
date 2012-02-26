package ca.qc.icerealm.bukkit.plugins.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
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
	private int _nbBlocks;
	private World _world;
	private Location _relativeRightBottom;
	
	public WorldZone(Location l, double radius) {
		_leftTop = new Location(l.getWorld(), l.getX() - radius, l.getY(), l.getZ() - radius);
		_rightBottom = new Location(l.getWorld(), l.getX() + radius, l.getY(), l.getZ() + radius);
		_leftTop.setY(l.getY() + radius);
		_rightBottom.setY(l.getY() - radius);
		_world = l.getWorld();
		setNBlock();		
		_relativeRightBottom = getRelativeBottomRight();
	}
	
	public WorldZone(Location lt, Location rb) {
		_leftTop = lt;
		_rightBottom = rb;
		_leftTop.setY(128);
		_rightBottom.setY(0);
		_world = _leftTop.getWorld();
		setNBlock();
		_relativeRightBottom = getRelativeBottomRight();
	}
	
	public WorldZone(Location lt, Location rb, double min, double max) {
		_leftTop = lt;
		_rightBottom = rb;
		_leftTop.setY(max);
		_rightBottom.setY(min);
		_world = _leftTop.getWorld();
		setNBlock();
		_relativeRightBottom = getRelativeBottomRight();
	}
	
	public WorldZone(World w, String data) {
		buildFromString(w, data);
		_world = w;
		setNBlock();
		_relativeRightBottom = getRelativeBottomRight();
	}
	
	public World getWorld() {
		return _world;
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
		double tlX = RandomUtil.getRandomDouble(0.0, _relativeRightBottom.getX());
		double tlZ = RandomUtil.getRandomDouble(0.0, _relativeRightBottom.getZ());
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
		List<Material> exclude = new ArrayList<Material>();
		exclude.add(org.bukkit.Material.WATER);
		exclude.add(org.bukkit.Material.STATIONARY_WATER);
		return getRandomHighestLocation(w, new ArrayList<Location>(), exclude);
	}
	
	
	
	private Location getRandomHighestLocation(World w, List<Location> list, List<Material> exclude) {
		Location l = null;
		if (list.size() < _nbBlocks) {
			double tlX = RandomUtil.getRandomDouble(0.0, _relativeRightBottom.getX());
			double tlZ = RandomUtil.getRandomDouble(0.0, _relativeRightBottom.getZ());
			tlX += getTopLeft().getX();
			tlZ += getTopLeft().getZ();
			
			Location loc = new Location(_world, tlX, _leftTop.getY(), tlZ);
			
			if (!isLocationPresent(loc, list)) {
				l = loc.clone();
				Block b = _world.getHighestBlockAt(l);
				double newY = loc.getY();
				while (b.getType() == org.bukkit.Material.AIR && newY > _rightBottom.getY()) {
					newY = newY - 1;
					l = new Location(_world, loc.getX(), newY, loc.getZ());
					b = _world.getBlockAt(l);
					//this.logger.info("(while) found a block at:" + l.getX() + "," + l.getY() + "," + l.getZ() + " on " + w.getBlockAt(l).getType().toString());
				}

				if (exclude.contains(_world.getBlockAt(l).getType())) {
					list.add(l);
					l = getRandomHighestLocation(_world, list, exclude);
				}
			}
			else {
				l = getRandomHighestLocation(_world, list, exclude);
			}
		}
		//this.logger.info("highest- found a block at:" + l.getX() + "," + l.getY() + "," + l.getZ() + " on " + w.getBlockAt(l).getType().toString());
		return l;
	}

	public Location getRandomLowestLocation(World w) {
		double tlX = RandomUtil.getRandomDouble(0.0, _relativeRightBottom.getX());
		double tlZ = RandomUtil.getRandomDouble(0.0, _relativeRightBottom.getZ());
		tlX += getTopLeft().getX();
		tlZ += getTopLeft().getZ();
		
		Location loc = new Location(w, tlX, _rightBottom.getY(), tlZ);
		Location l = loc.clone();
		Block b = w.getBlockAt(loc);
		double newY = loc.getY();
		while (b.getType() != org.bukkit.Material.AIR && newY < _leftTop.getY()) {
			
			newY = newY + 1;
			
			l = new Location(loc.getWorld(), loc.getX(), newY, loc.getZ());
			b = w.getBlockAt(l);
			//this.logger.info("(while) found a block at:" + l.getX() + "," + l.getY() + "," + l.getZ() + " on " + w.getBlockAt(l).getType().toString());
		}
		
		if (w.getBlockAt(l).getType() == org.bukkit.Material.WATER) {
			l = getRandomLowestLocation(w);
		}
		//this.logger.info("lowest - found a block at:" + l.getX() + "," + l.getY() + "," + l.getZ() + " on " + w.getBlockAt(l).getType().toString());
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
	
	private void setNBlock() {
		Double width = _rightBottom.getX() - _leftTop.getX();
		Double height = _rightBottom.getZ() - _leftTop.getZ();
		_nbBlocks = width.intValue() * height.intValue();
	}
	
	private boolean isLocationPresent(Location l, List<Location> list) {
		for (Location loc : list) {
			if (loc.getX() == l.getX() && loc.getZ() == l.getZ()) {
				return true;
			}
		}
		return false;
	}
	
	
}

