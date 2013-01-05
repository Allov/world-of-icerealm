package ca.qc.icerealm.bukkit.plugins.scenarios.tools;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropResult;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOdds;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOddsItem;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.data.EnchantmentsOdds;
import ca.qc.icerealm.bukkit.plugins.raredrops.randomizer.AbsoluteSingleRareDropRandomizer;
import ca.qc.icerealm.bukkit.plugins.raredrops.randomizer.MultipleRareDropsRandomizer;
import ca.qc.icerealm.bukkit.plugins.raredrops.randomizer.RareDropsRandomizer;

public class LootGenerator {
	
	
	public static Loot getApocalypseLoot(double modifier) {
		Random numberRandom = new Random();
		Loot loot = new Loot();
		
		if (modifier < 1) modifier = 1;
		double[] enchantPercentile = new double[] { 5 * modifier, 5 * modifier, 40 * modifier,  40 * modifier, 20 * modifier };
		EnchantmentsOdds enchantOdds = new EnchantmentsOdds();
		enchantOdds.setPercentageEnchantments(enchantPercentile);
			
		RareDropsOdds oddsDiamond = new RareDropsOdds();
		oddsDiamond.addOddsItem(new RareDropsOddsItem(90 * modifier, Material.DIAMOND_SWORD, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.DIAMOND_HELMET, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.DIAMOND_BOOTS, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.DIAMOND_CHESTPLATE, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.DIAMOND_LEGGINGS, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(100 * modifier, Material.DIAMOND, enchantOdds)); // il y a du diamant certain
		RareDropsRandomizer random = new MultipleRareDropsRandomizer(oddsDiamond);
		List<RareDropResult> result = random.randomize();
		
		RareDropsOdds oddsGold = new RareDropsOdds();
		oddsGold.addOddsItem(new RareDropsOddsItem(7 * modifier, Material.GOLD_SWORD, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(7 * modifier, Material.GOLD_HELMET, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(7 * modifier, Material.GOLD_BOOTS, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(7 * modifier, Material.GOLD_CHESTPLATE, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(7 * modifier, Material.GOLD_LEGGINGS, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.GOLD_INGOT, enchantOdds)); // de l'or surment
		random = new MultipleRareDropsRandomizer(oddsGold);
		result.addAll(random.randomize());
		
		RareDropsOdds oddsIron = new RareDropsOdds();
		oddsIron.addOddsItem(new RareDropsOddsItem(15 * modifier, Material.IRON_SWORD, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.IRON_HELMET, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.IRON_BOOTS, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.IRON_CHESTPLATE, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.IRON_LEGGINGS, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(80 * modifier, Material.IRON_INGOT, enchantOdds)); // du iron aussi
		random = new MultipleRareDropsRandomizer(oddsIron);
		result.addAll(random.randomize());
		
		RareDropsOdds oddsLeather = new RareDropsOdds();
		oddsLeather.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.LEATHER_BOOTS, enchantOdds));
		oddsLeather.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.LEATHER_HELMET, enchantOdds));
		oddsLeather.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.LEATHER_CHESTPLATE, enchantOdds));
		oddsLeather.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.LEATHER_LEGGINGS, enchantOdds));
		oddsLeather.addOddsItem(new RareDropsOddsItem(80 * modifier, Material.LEATHER, enchantOdds));
		random = new MultipleRareDropsRandomizer(oddsLeather);
		result.addAll(random.randomize());
		
		RareDropsOdds oddsOther = new RareDropsOdds();
		oddsOther.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.ARROW));
		oddsOther.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.APPLE));
		oddsOther.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.BREAD));
		oddsOther.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.BOW, enchantOdds));
		oddsOther.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.BLAZE_POWDER));
		oddsOther.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.ENDER_PEARL));
		oddsOther.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.NETHER_WARTS));
		oddsOther.addOddsItem(new RareDropsOddsItem(10 * modifier, Material.SKULL_ITEM));
		random = new MultipleRareDropsRandomizer(oddsOther);
		result.addAll(random.randomize());
		
		for (RareDropResult drop : result) {

			if (drop.getItemStack().getType() == Material.IRON_INGOT || 
				drop.getItemStack().getType() == Material.GOLD_INGOT ||
				drop.getItemStack().getType() == Material.DIAMOND ||
				drop.getItemStack().getType() == Material.APPLE ||
				drop.getItemStack().getType() == Material.BREAD) {
				
				int nb = numberRandom.nextInt(5 * (int)modifier);
				drop.getItemStack().setAmount(nb > 0 ? nb : 1);
			}
			
			if (drop.getItemStack().getType() == Material.DIAMOND) {
				int nb = numberRandom.nextInt(10 * (int)modifier);
				drop.getItemStack().setAmount(nb > 0 ? nb : 3);
			}
			
			if (drop.getItemStack().getType() == Material.ARROW) {
				int nb = numberRandom.nextInt(50 * (int)modifier);
				drop.getItemStack().setAmount(nb > 15 ? nb : 15);
			}
			
			loot.addItemStack(drop.getItemStack());
		}
		
		return loot;
	}
	
	public static Loot getFightingRandomLoot(double modifier) {
		Random numberRandom = new Random();
		Loot loot = new Loot();
		
		if (modifier < 1) modifier = 1;
		double[] enchantPercentile = new double[] { 40 * modifier, 20 * modifier, 10 * modifier,  5 * modifier, 5 * modifier };
		EnchantmentsOdds enchantOdds = new EnchantmentsOdds();
		enchantOdds.setPercentageEnchantments(enchantPercentile);
		
		RareDropsOdds oddsDiamond = new RareDropsOdds();
		oddsDiamond.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.DIAMOND_SWORD, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.DIAMOND_HELMET, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.DIAMOND_BOOTS, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.DIAMOND_CHESTPLATE, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.DIAMOND_LEGGINGS, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(100 * modifier, Material.DIAMOND, enchantOdds)); // il y a du diamant certain
		RareDropsRandomizer random = new MultipleRareDropsRandomizer(oddsDiamond);
		List<RareDropResult> result = random.randomize();
		
		RareDropsOdds oddsGold = new RareDropsOdds();
		oddsGold.addOddsItem(new RareDropsOddsItem(10 * modifier, Material.GOLD_SWORD, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(10 * modifier, Material.GOLD_HELMET, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(10 * modifier, Material.GOLD_BOOTS, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(10 * modifier, Material.GOLD_CHESTPLATE, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(10 * modifier, Material.GOLD_LEGGINGS, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.GOLD_INGOT, enchantOdds)); // de l'or surment
		random = new MultipleRareDropsRandomizer(oddsGold);
		result.addAll(random.randomize());
		
		RareDropsOdds oddsIron = new RareDropsOdds();
		oddsIron.addOddsItem(new RareDropsOddsItem(15 * modifier, Material.IRON_SWORD, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.IRON_HELMET, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.IRON_BOOTS, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.IRON_CHESTPLATE, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.IRON_LEGGINGS, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(80 * modifier, Material.IRON_INGOT, enchantOdds)); // du iron aussi
		random = new MultipleRareDropsRandomizer(oddsIron);
		result.addAll(random.randomize());
		
		RareDropsOdds oddsLeather = new RareDropsOdds();
		oddsIron.addOddsItem(new RareDropsOddsItem(15 * modifier, Material.LEATHER_BOOTS, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.LEATHER_HELMET, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.LEATHER_CHESTPLATE, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.LEATHER_LEGGINGS, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(80 * modifier, Material.LEATHER, enchantOdds)); // du iron aussi
		random = new MultipleRareDropsRandomizer(oddsLeather);
		result.addAll(random.randomize());
		
		RareDropsOdds oddsOther = new RareDropsOdds();
		oddsOther.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.ARROW));
		oddsOther.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.APPLE));
		oddsOther.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.BREAD));
		oddsOther.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.BOW, enchantOdds));
		oddsOther.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.BLAZE_POWDER));
		oddsOther.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.ENDER_PEARL));
		oddsOther.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.NETHER_WARTS));
		oddsOther.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.SKULL));
		oddsOther.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.SKULL_ITEM));
		random = new MultipleRareDropsRandomizer(oddsOther);
		result.addAll(random.randomize());
		
		for (RareDropResult drop : result) {

			if (drop.getItemStack().getType() == Material.IRON_INGOT || 
				drop.getItemStack().getType() == Material.GOLD_INGOT ||
				drop.getItemStack().getType() == Material.DIAMOND ||
				drop.getItemStack().getType() == Material.APPLE ||
				drop.getItemStack().getType() == Material.BREAD) {
				
				int nb = numberRandom.nextInt(5 * (int)modifier);
				drop.getItemStack().setAmount(nb > 0 ? nb : 1);
			}
			
			if (drop.getItemStack().getType() == Material.ARROW) {
				int nb = numberRandom.nextInt(20 * (int)modifier);
				drop.getItemStack().setAmount(nb > 5 ? nb : 5);
			}
			
			loot.addItemStack(drop.getItemStack());
		}
		
		return loot;
	}
	
	public static Loot generateTreasureLoot(double modifier) {
		Random numberRandom = new Random();
		Loot loot = new Loot();
		
		if (modifier < 1) modifier = 1;
		double[] enchantPercentile = new double[] { 20 * modifier, 10 * modifier, 5 * modifier,  2 * modifier, 2 * modifier };
		EnchantmentsOdds enchantOdds = new EnchantmentsOdds();
		enchantOdds.setPercentageEnchantments(enchantPercentile);
		
		RareDropsOdds oddsDiamond = new RareDropsOdds();
		oddsDiamond.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.DIAMOND_SWORD, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.DIAMOND_HELMET, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.DIAMOND_BOOTS, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.DIAMOND_CHESTPLATE, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.DIAMOND_LEGGINGS, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(100 * modifier, Material.DIAMOND, enchantOdds)); // il y a du diamant certain
		RareDropsRandomizer random = new MultipleRareDropsRandomizer(oddsDiamond);
		List<RareDropResult> result = random.randomize();
		
		RareDropsOdds oddsGold = new RareDropsOdds();
		oddsGold.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.GOLD_SWORD, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.GOLD_HELMET, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.GOLD_BOOTS, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.GOLD_CHESTPLATE, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.GOLD_LEGGINGS, enchantOdds));
		oddsGold.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.GOLD_INGOT, enchantOdds)); // de l'or surment
		random = new MultipleRareDropsRandomizer(oddsGold);
		result.addAll(random.randomize());
		
		RareDropsOdds oddsIron = new RareDropsOdds();
		oddsIron.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.IRON_SWORD, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.IRON_HELMET, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.IRON_BOOTS, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.IRON_CHESTPLATE, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.IRON_LEGGINGS, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(80 * modifier, Material.IRON_INGOT, enchantOdds)); // du iron aussi
		random = new MultipleRareDropsRandomizer(oddsIron);
		result.addAll(random.randomize());
		
		RareDropsOdds oddsLeather = new RareDropsOdds();
		oddsIron.addOddsItem(new RareDropsOddsItem(2 * modifier, Material.LEATHER_BOOTS, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(2 * modifier, Material.LEATHER_HELMET, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(2 * modifier, Material.LEATHER_CHESTPLATE, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(2 * modifier, Material.LEATHER_LEGGINGS, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(80 * modifier, Material.LEATHER, enchantOdds)); // du iron aussi
		random = new MultipleRareDropsRandomizer(oddsLeather);
		result.addAll(random.randomize());
		
		RareDropsOdds oddsOther = new RareDropsOdds();
		oddsOther.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.ARROW));
		oddsOther.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.APPLE));
		oddsOther.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.BREAD));
		oddsOther.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.BOW, enchantOdds));
		oddsOther.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.BLAZE_POWDER));
		oddsOther.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.GLOWSTONE_DUST));
		oddsOther.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.ENDER_PEARL));
		oddsOther.addOddsItem(new RareDropsOddsItem(10 * modifier, Material.NETHER_WARTS));
		oddsOther.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.SKULL));
		oddsOther.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.SKULL_ITEM));
		random = new MultipleRareDropsRandomizer(oddsOther);
		result.addAll(random.randomize());
		
		for (RareDropResult drop : result) {

			if (drop.getItemStack().getType() == Material.IRON_INGOT || 
				drop.getItemStack().getType() == Material.GOLD_INGOT ||
				drop.getItemStack().getType() == Material.DIAMOND ||
				drop.getItemStack().getType() == Material.APPLE ||
				drop.getItemStack().getType() == Material.BREAD) {
				
				int nb = numberRandom.nextInt(5 * (int)modifier);
				drop.getItemStack().setAmount(nb > 0 ? nb : 1);
			}
			
			if (drop.getItemStack().getType() == Material.ARROW) {
				int nb = numberRandom.nextInt(20 * (int)modifier);
				drop.getItemStack().setAmount(nb > 5 ? nb : 5);
			}
			
			loot.addItemStack(drop.getItemStack());
		}
		
		return loot;
	}
	
	public static Loot generateRescueSurvivorLoot(double modifier, double distance) {
		Random numberRandom = new Random();
		Loot loot = new Loot();
		double weaponModifier = modifier;
		if (weaponModifier < 1) weaponModifier = 1;
		weaponModifier += distance;
		double[] enchantPercentile = new double[] { 20 * weaponModifier, 50 * weaponModifier, 65 * weaponModifier,  75 * weaponModifier, 75 * weaponModifier };
		EnchantmentsOdds enchantOdds = new EnchantmentsOdds();
		enchantOdds.setPercentageEnchantments(enchantPercentile);
		
		RareDropsOdds oddsWeapons = new RareDropsOdds();
		oddsWeapons.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.DIAMOND_SWORD, enchantOdds));
		oddsWeapons.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.DIAMOND_HELMET, enchantOdds));
		oddsWeapons.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.DIAMOND_BOOTS, enchantOdds));
		oddsWeapons.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.DIAMOND_CHESTPLATE, enchantOdds));
		oddsWeapons.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.DIAMOND_LEGGINGS, enchantOdds));
		oddsWeapons.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.IRON_SWORD, enchantOdds));
		oddsWeapons.addOddsItem(new RareDropsOddsItem(2 * modifier, Material.IRON_HELMET, enchantOdds));
		oddsWeapons.addOddsItem(new RareDropsOddsItem(2 * modifier, Material.IRON_BOOTS, enchantOdds));
		oddsWeapons.addOddsItem(new RareDropsOddsItem(2 * modifier, Material.IRON_CHESTPLATE, enchantOdds));
		oddsWeapons.addOddsItem(new RareDropsOddsItem(2 * modifier, Material.IRON_LEGGINGS, enchantOdds));
		
		RareDropsRandomizer random = new AbsoluteSingleRareDropRandomizer(oddsWeapons);
		List<RareDropResult> result = random.randomize();
	
		
		RareDropsOdds oddsOre = new RareDropsOdds();
		oddsOre.addOddsItem(new RareDropsOddsItem(15 * modifier, Material.IRON_INGOT, enchantOdds)); // du iron aussi
		oddsOre.addOddsItem(new RareDropsOddsItem(10 * modifier, Material.DIAMOND, enchantOdds));
		oddsOre.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.GOLD_INGOT, enchantOdds));
		random = new MultipleRareDropsRandomizer(oddsOre);
		result.addAll(random.randomize());
		
		RareDropsOdds oddsOther = new RareDropsOdds();
		oddsOther.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.APPLE));
		oddsOther.addOddsItem(new RareDropsOddsItem(35 * modifier, Material.LEATHER));
		oddsOther.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.CARROT_ITEM));
		oddsOther.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.MELON));
		oddsOther.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.BREAD));
		oddsOther.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.BLAZE_POWDER));
		oddsOther.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.GLOWSTONE_DUST));
		oddsOther.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.ENDER_PEARL));
		oddsOther.addOddsItem(new RareDropsOddsItem(10 * modifier, Material.NETHER_WARTS));
		oddsOther.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.SKULL_ITEM));
		oddsOther.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.BOOK));
		oddsOther.addOddsItem(new RareDropsOddsItem(25 * modifier, Material.CLAY_BALL));
		oddsOther.addOddsItem(new RareDropsOddsItem(3 * modifier, Material.RECORD_10));
		oddsOther.addOddsItem(new RareDropsOddsItem(3 * modifier, Material.RECORD_9));
		oddsOther.addOddsItem(new RareDropsOddsItem(3 * modifier, Material.RECORD_8));
		oddsOther.addOddsItem(new RareDropsOddsItem(3 * modifier, Material.RECORD_7));
		oddsOther.addOddsItem(new RareDropsOddsItem(3 * modifier, Material.RECORD_6));
		oddsOther.addOddsItem(new RareDropsOddsItem(3 * modifier, Material.RECORD_5));
		oddsOther.addOddsItem(new RareDropsOddsItem(3 * modifier, Material.RECORD_4));
		oddsOther.addOddsItem(new RareDropsOddsItem(3 * modifier, Material.RECORD_3));
		oddsOther.addOddsItem(new RareDropsOddsItem(3 * modifier, Material.RECORD_11));
		oddsOther.addOddsItem(new RareDropsOddsItem(3 * modifier, Material.RECORD_12));
		oddsOther.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.EMERALD));
		oddsOther.addOddsItem(new RareDropsOddsItem(3 * modifier, Material.OBSIDIAN));
		random = new MultipleRareDropsRandomizer(oddsOther);
		result.addAll(random.randomize());
		
		for (RareDropResult drop : result) {

			if (drop.getItemStack().getType() == Material.IRON_INGOT || 
				drop.getItemStack().getType() == Material.GOLD_INGOT ||
				drop.getItemStack().getType() == Material.DIAMOND ||
				drop.getItemStack().getType() == Material.APPLE ||
				drop.getItemStack().getType() == Material.BREAD ||
				drop.getItemStack().getType() == Material.CARROT_ITEM ||
				drop.getItemStack().getType() == Material.MELON ||
				drop.getItemStack().getType() == Material.EMERALD || 
				drop.getItemStack().getType() == Material.OBSIDIAN) {
				
				int nb = numberRandom.nextInt(5 * (int)modifier);
				drop.getItemStack().setAmount(nb > 0 ? nb : 1);
			}
			
			if (drop.getItemStack().getType() == Material.ARROW || 
				drop.getItemStack().getType() == Material.BOOK || 
				drop.getItemStack().getType() == Material.CLAY_BALL ||
				drop.getItemStack().getType() == Material.LEATHER) {
				int nb = numberRandom.nextInt(20 * (int)modifier);
				drop.getItemStack().setAmount(nb > 5 ? nb : 5);
			}
			
			loot.addItemStack(drop.getItemStack());
		}
		
		return loot;
	}
}
