package ca.qc.icerealm.bukkit.plugins.dreamworld.tools;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropResult;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOdds;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOddsItem;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.data.EnchantmentsOdds;
import ca.qc.icerealm.bukkit.plugins.raredrops.randomizer.MultipleRareDropsRandomizer;
import ca.qc.icerealm.bukkit.plugins.raredrops.randomizer.RareDropsRandomizer;

public class LootGenerator {

	public static Loot getRandomLoot(double modifier) {
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
		
		RareDropsOdds oddsOther = new RareDropsOdds();
		oddsOther.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.APPLE));
		oddsOther.addOddsItem(new RareDropsOddsItem(50 * modifier, Material.BREAD));
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
			
			loot.addItemStack(drop.getItemStack());
		}
		
		return loot;
	}
}
