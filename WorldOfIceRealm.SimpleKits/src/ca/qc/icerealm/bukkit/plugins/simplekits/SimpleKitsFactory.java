package ca.qc.icerealm.bukkit.plugins.simplekits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class SimpleKitsFactory {
	private static SimpleKitsFactory instance;
	
	public static SimpleKitsFactory getInstance() {
		if (instance == null) {
			instance = new SimpleKitsFactory();
		}
		
		return instance;
	}	
	
	public List<ItemStack> createStarterKit() {
		
		List<ItemStack> items = new ArrayList<ItemStack>();
		
		items.add(new ItemStack(Material.TORCH, 16));
		items.add(new ItemStack(Material.STONE_PICKAXE, 1));
		items.add(new ItemStack(Material.STONE_SWORD, 1));
		items.add(new ItemStack(Material.STONE_SPADE, 1));
		items.add(new ItemStack(Material.STONE_AXE, 1));
		items.add(new ItemStack(Material.RAW_BEEF, 4));
		items.add(new ItemStack(Material.RAW_CHICKEN, 4));
		items.add(new ItemStack(Material.LOG, 20));
		items.add(new ItemStack(Material.COAL, 12));
		items.add(new ItemStack(Material.COBBLESTONE, 64));
		items.add(new ItemStack(Material.COMPASS, 1));
		items.add(new ItemStack(Material.WATCH, 1));
		
		
		return items;
	}

	public List<ItemStack> createAverageKit() {
		
		List<ItemStack> items = new ArrayList<ItemStack>();
		
		items.add(new ItemStack(Material.TORCH, 64));
		items.add(new ItemStack(Material.IRON_PICKAXE, 1));
		items.add(new ItemStack(Material.IRON_SWORD, 1));
		items.add(new ItemStack(Material.BOW, 1));
		items.add(new ItemStack(Material.IRON_SPADE, 1));
		items.add(new ItemStack(Material.IRON_AXE, 1));
		items.add(new ItemStack(Material.COOKED_BEEF, 8));
		items.add(new ItemStack(Material.BREAD, 30));
		items.add(new ItemStack(Material.GOLDEN_APPLE, 5));
		items.add(new ItemStack(Material.WOOD, 64));
		items.add(new ItemStack(Material.COAL, 64));
		items.add(new ItemStack(Material.COBBLESTONE, 64));
		items.add(new ItemStack(Material.IRON_HELMET, 1));
		items.add(new ItemStack(Material.IRON_CHESTPLATE, 1));
		items.add(new ItemStack(Material.IRON_LEGGINGS, 1));
		items.add(new ItemStack(Material.IRON_BOOTS, 1));
		items.add(new ItemStack(Material.ARROW, 64));
		items.add(new ItemStack(Material.COMPASS, 1));
		items.add(new ItemStack(Material.WATCH, 1));
		
		return items;
	}

	public List<ItemStack> createAdvancedKit() {
		
		List<ItemStack> items = new ArrayList<ItemStack>();
		
		items.add(new ItemStack(Material.TORCH, 64));
		items.add(new ItemStack(Material.DIAMOND_PICKAXE, 1));
		items.add(new ItemStack(Material.DIAMOND_SWORD, 1));
		items.add(new ItemStack(Material.BOW, 1));
		items.add(new ItemStack(Material.DIAMOND_SPADE, 1));
		items.add(new ItemStack(Material.DIAMOND_AXE, 1));
		items.add(new ItemStack(Material.COOKED_BEEF, 64));
		items.add(new ItemStack(Material.BREAD, 64));
		items.add(new ItemStack(Material.GOLDEN_APPLE, 64));
		items.add(new ItemStack(Material.WOOD, 64));
		items.add(new ItemStack(Material.COAL, 64));
		items.add(new ItemStack(Material.COBBLESTONE, 64));
		items.add(new ItemStack(Material.DIAMOND_HELMET, 1));
		items.add(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
		items.add(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
		items.add(new ItemStack(Material.DIAMOND_BOOTS, 1));
		items.add(new ItemStack(Material.ARROW, 64));
		items.add(new ItemStack(Material.COMPASS, 1));
		items.add(new ItemStack(Material.WATCH, 1));
		
		return items;
	}
}
