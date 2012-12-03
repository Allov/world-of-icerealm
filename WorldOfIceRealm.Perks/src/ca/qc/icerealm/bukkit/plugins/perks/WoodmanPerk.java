package ca.qc.icerealm.bukkit.plugins.perks;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class WoodmanPerk implements Listener {

	private final PerkService perkService = PerkService.getInstance();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent evt) {
		
		if (evt.getBlock().getType().equals(Material.LOG) && perkService.playerHasPerk(evt.getPlayer(), SettlerPerks.WoodmanId)) {
			
			Block block = evt.getBlock();
			Block below = block.getWorld().getBlockAt(new Location(block.getWorld(), block.getX(), block.getY() - 1, block.getZ()));
			
			if (below.getType().equals(Material.DIRT) || below.getType().equals(Material.GRASS)) {
				ArrayList<Block> treeBlocks = new ArrayList<Block>();
				detectTree(block, treeBlocks, block.getX(), block.getZ());
				
				int leavesCount = 0;
				int logCount = 0;
				for(Block b : treeBlocks) {
					if (b.getType().equals(Material.LEAVES)) {
						leavesCount++;
					} else if (b.getType().equals(Material.LOG)) {
						logCount++;
					}
				}
				
				if (leavesCount > 5) {
					for(Block b : treeBlocks) {
						b.breakNaturally();
					}
					
					if (logCount <= 10) {
						evt.getPlayer().setFoodLevel(evt.getPlayer().getFoodLevel() - 2);
					} else if (logCount > 10 && logCount <= 20) {
						evt.getPlayer().setFoodLevel(evt.getPlayer().getFoodLevel() - 5);
					} else {
						evt.getPlayer().setFoodLevel(evt.getPlayer().getFoodLevel() - 10);
					}
				}
			}			
		}
	}

	private void detectTree(Block block, ArrayList<Block> treeBlocks, int originX, int originZ) {
		
		if (!treeBlocks.contains(block) &&
			Math.abs(block.getX() - originX) < 3 &&
			Math.abs(block.getZ() - originZ) < 3 &&
			(block.getType().equals(Material.LOG) || block.getType().equals(Material.LEAVES))) {
			treeBlocks.add(block);
			
			Block[] surrounding = new Block[8];
			
			// top
			Block top = block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ());
			
			// n
			surrounding[0] = block.getWorld().getBlockAt(block.getX() + 1, block.getY(), block.getZ());
			// s
			surrounding[1] = block.getWorld().getBlockAt(block.getX() - 1, block.getY(), block.getZ());
			// e
			surrounding[2] = block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() + 1);
			// w
			surrounding[3] = block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() - 1);
			// ne
			surrounding[4] = block.getWorld().getBlockAt(block.getX() + 1, block.getY(), block.getZ() + 1);
			// nw
			surrounding[5] = block.getWorld().getBlockAt(block.getX() + 1, block.getY(), block.getZ() - 1);
			// se
			surrounding[6] = block.getWorld().getBlockAt(block.getX() - 1, block.getY(), block.getZ() + 1);
			// sw
			surrounding[7] = block.getWorld().getBlockAt(block.getX() - 1, block.getY(), block.getZ() - 1);
			
			for(Block b : surrounding) {
				if (b.getType().equals(Material.LOG) || b.getType().equals(Material.LEAVES)) {
					detectTree(b, treeBlocks, originX, originZ);
				}
			}
			
			detectTree(top, treeBlocks, originX, originZ);
		}
	}
}
