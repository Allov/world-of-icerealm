package ca.qc.icerealm.bukkit.plugins.common;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class MaterialUtil 
{
	public static String getMaterialFriendName(String name) {
		return name.replace('_', ' ').toLowerCase();		
	}
	
	public static boolean isCookable(Material item)
	{
		return item.equals(Material.PORK) ||
				   item.equals(Material.RAW_BEEF) ||
				   item.equals(Material.RAW_CHICKEN) ||
				   item.equals(Material.RAW_FISH);
	}
	
	public static Material getCookableEquivalence(Material item)
	{
		if (item.equals(Material.PORK)) return Material.GRILLED_PORK;
		if (item.equals(Material.RAW_BEEF)) return Material.COOKED_BEEF;
		if (item.equals(Material.RAW_CHICKEN)) return Material.COOKED_CHICKEN;
		if (item.equals(Material.RAW_FISH)) return Material.COOKED_FISH;
		if (item.equals(Material.POTATO_ITEM)) return Material.BAKED_POTATO;
			
		return null;
	}
	
	public static boolean isSword(Material item)
    {
		return item.equals(Material.GOLD_SWORD) ||
			   item.equals(Material.DIAMOND_SWORD) ||
			   item.equals(Material.IRON_SWORD) ||
			   item.equals(Material.WOOD_SWORD) ||
			   item.equals(Material.STONE_SWORD);
    }

	public static boolean isBow(Material item)
    {
		return item.equals(Material.BOW);
    }
	
	public static boolean isArmor(Material item)
    {
		return item.equals(Material.GOLD_HELMET) ||
				   item.equals(Material.GOLD_CHESTPLATE) ||
				   item.equals(Material.GOLD_BOOTS) ||
				   item.equals(Material.GOLD_LEGGINGS) ||
				   item.equals(Material.LEATHER_HELMET) ||
				   item.equals(Material.LEATHER_CHESTPLATE) ||
				   item.equals(Material.LEATHER_BOOTS) ||
				   item.equals(Material.LEATHER_LEGGINGS) ||
				   item.equals(Material.IRON_HELMET) ||
				   item.equals(Material.IRON_CHESTPLATE) ||
				   item.equals(Material.IRON_BOOTS) ||
				   item.equals(Material.IRON_LEGGINGS) ||
				   item.equals(Material.DIAMOND_HELMET) ||
				   item.equals(Material.DIAMOND_CHESTPLATE) ||
				   item.equals(Material.DIAMOND_BOOTS) ||
				   item.equals(Material.DIAMOND_LEGGINGS);
    }
	
	public static boolean isHelmet(Material item)
    {
		return item.equals(Material.GOLD_HELMET) ||
				   item.equals(Material.LEATHER_HELMET) ||
				   item.equals(Material.IRON_HELMET) ||
				   item.equals(Material.DIAMOND_HELMET);
    }
	
	public static boolean isBoots(Material item)
    {
		return item.equals(Material.GOLD_BOOTS) ||
				   item.equals(Material.LEATHER_BOOTS) ||
				   item.equals(Material.IRON_BOOTS) ||
				   item.equals(Material.DIAMOND_BOOTS);
    }
	
	public static boolean isLeggings(Material item)
    {
		return item.equals(Material.GOLD_LEGGINGS) ||
				   item.equals(Material.LEATHER_LEGGINGS) ||
				   item.equals(Material.IRON_LEGGINGS) ||
				   item.equals(Material.DIAMOND_LEGGINGS);
    }
	
	public static boolean isChestplate(Material item)
    {
		return item.equals(Material.GOLD_CHESTPLATE) ||
				   item.equals(Material.LEATHER_CHESTPLATE) ||
				   item.equals(Material.IRON_CHESTPLATE) ||
				   item.equals(Material.DIAMOND_CHESTPLATE);
    }

	public static boolean isTool(Material item)
    {
		return item.equals(Material.GOLD_PICKAXE) ||
				   item.equals(Material.DIAMOND_PICKAXE) ||
				   item.equals(Material.IRON_PICKAXE) ||
				   item.equals(Material.WOOD_PICKAXE) ||
				   item.equals(Material.STONE_PICKAXE) ||
				   item.equals(Material.GOLD_HOE) ||
				   item.equals(Material.DIAMOND_HOE) ||
				   item.equals(Material.IRON_HOE) ||
				   item.equals(Material.WOOD_HOE) ||
				   item.equals(Material.STONE_HOE) || 
				   item.equals(Material.GOLD_AXE) ||
				   item.equals(Material.DIAMOND_AXE) ||
				   item.equals(Material.IRON_AXE) ||
				   item.equals(Material.WOOD_AXE) ||
				   item.equals(Material.STONE_AXE) ||
				   item.equals(Material.GOLD_SPADE) ||
				   item.equals(Material.DIAMOND_SPADE) ||
				   item.equals(Material.IRON_SPADE) ||
				   item.equals(Material.WOOD_SPADE) ||
				   item.equals(Material.STONE_SPADE);
    }
	
	public static boolean isEnchantableTool(Material item)
    {
		return item.equals(Material.GOLD_PICKAXE) ||
				   item.equals(Material.DIAMOND_PICKAXE) ||
				   item.equals(Material.IRON_PICKAXE) ||
				   item.equals(Material.WOOD_PICKAXE) ||
				   item.equals(Material.STONE_PICKAXE) ||
				   item.equals(Material.GOLD_AXE) ||
				   item.equals(Material.DIAMOND_AXE) ||
				   item.equals(Material.IRON_AXE) ||
				   item.equals(Material.WOOD_AXE) ||
				   item.equals(Material.STONE_AXE) ||
				   item.equals(Material.GOLD_SPADE) ||
				   item.equals(Material.DIAMOND_SPADE) ||
				   item.equals(Material.IRON_SPADE) ||
				   item.equals(Material.WOOD_SPADE) ||
				   item.equals(Material.STONE_SPADE);
    }
	
	public static Enchantment[] getArmorProtectionEnchantments()
	{
		Enchantment[] list = new Enchantment[]
				{
					Enchantment.PROTECTION_ENVIRONMENTAL,
					Enchantment.PROTECTION_EXPLOSIONS,
					Enchantment.PROTECTION_FIRE,
					Enchantment.PROTECTION_PROJECTILE
				};
		return list;
	}
	
	public static Enchantment[] getBootsEnchantments()
	{
		Enchantment[] list = new Enchantment[]
				{
					Enchantment.PROTECTION_ENVIRONMENTAL,
					Enchantment.PROTECTION_EXPLOSIONS,
					Enchantment.PROTECTION_FALL,
					Enchantment.PROTECTION_FIRE,
					Enchantment.PROTECTION_PROJECTILE
				};
		return list;
	}
	
	public static Enchantment[] getHelmetOnlyEnchantments()
	{
		Enchantment[] list = new Enchantment[]
				{
					Enchantment.WATER_WORKER,
					Enchantment.OXYGEN
				};
		return list;
	}
	
	
	public static Enchantment[] getBowEnchantments()
	{
		Enchantment[] list = new Enchantment[]
				{
					Enchantment.ARROW_DAMAGE,
					Enchantment.ARROW_FIRE,
					Enchantment.ARROW_INFINITE,
					Enchantment.ARROW_KNOCKBACK
				};
		return list;
	}
	
	public static Enchantment[] getSwordDamageEnchantments()
	{
		Enchantment[] list = new Enchantment[]
				{
					Enchantment.DAMAGE_ALL,
					Enchantment.DAMAGE_ARTHROPODS,
					Enchantment.DAMAGE_UNDEAD,
				};
		return list;
	}
	
	public static Enchantment[] getSwordNonDamageEnchantments()
	{
		Enchantment[] list = new Enchantment[]
				{
					Enchantment.FIRE_ASPECT,
					Enchantment.KNOCKBACK,
					Enchantment.LOOT_BONUS_MOBS
				};
		return list;
	}
	
	public static Enchantment[] getToolsEnchantments()
	{
		Enchantment[] list = new Enchantment[]
				{
					Enchantment.DIG_SPEED,
					Enchantment.DURABILITY,
					Enchantment.SILK_TOUCH,
					Enchantment.LOOT_BONUS_BLOCKS
				};
		return list;
	}
}

