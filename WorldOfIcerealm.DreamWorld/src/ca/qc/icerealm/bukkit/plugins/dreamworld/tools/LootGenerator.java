package ca.qc.icerealm.bukkit.plugins.dreamworld.tools;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;

import ca.qc.icerealm.bukkit.plugins.common.RandomUtil;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropResult;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOdds;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOddsItem;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.data.EnchantmentsOdds;
import ca.qc.icerealm.bukkit.plugins.raredrops.randomizer.AbsoluteSingleRareDropRandomizer;
import ca.qc.icerealm.bukkit.plugins.raredrops.randomizer.MultipleRareDropsRandomizer;
import ca.qc.icerealm.bukkit.plugins.raredrops.randomizer.RareDropsRandomizer;

public class LootGenerator {

	public static Loot getRandomLoot(double modifier) {
		Random numberRandom = new Random();
		Loot loot = new Loot();
		
		EnchantmentsOdds enchantOdds = new EnchantmentsOdds();
		enchantOdds.setPercentageEnchantments(EnchantmentsOdds.SURE_AVERAGE_ODDS);
		
		RareDropsOdds oddsDiamond = new RareDropsOdds();
		oddsDiamond.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.DIAMOND_SWORD, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.DIAMOND_HELMET, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.DIAMOND_BOOTS, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.DIAMOND_CHESTPLATE, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.DIAMOND_LEGGINGS, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(100 * modifier, Material.DIAMOND, enchantOdds));
		RareDropsRandomizer random = new MultipleRareDropsRandomizer(oddsDiamond);
		List<RareDropResult> result = random.randomize();
		
		RareDropsOdds oddsGold = new RareDropsOdds();
		oddsDiamond.addOddsItem(new RareDropsOddsItem(7 * modifier, Material.GOLD_SWORD, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(7 * modifier, Material.GOLD_HELMET, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(7 * modifier, Material.GOLD_BOOTS, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(7 * modifier, Material.GOLD_CHESTPLATE, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(7 * modifier, Material.GOLD_LEGGINGS, enchantOdds));
		oddsDiamond.addOddsItem(new RareDropsOddsItem(120 * modifier, Material.GOLD_INGOT, enchantOdds));
		random = new MultipleRareDropsRandomizer(oddsGold);
		result.addAll(random.randomize());
		
		RareDropsOdds oddsIron = new RareDropsOdds();
		oddsIron.addOddsItem(new RareDropsOddsItem(15 * modifier, Material.IRON_SWORD, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.IRON_HELMET, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.IRON_BOOTS, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.IRON_CHESTPLATE, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(20 * modifier, Material.IRON_LEGGINGS, enchantOdds));
		oddsIron.addOddsItem(new RareDropsOddsItem(300 * modifier, Material.IRON_INGOT, enchantOdds));
		random = new MultipleRareDropsRandomizer(oddsIron);
		result.addAll(random.randomize());
		
		RareDropsOdds oddsOther = new RareDropsOdds();
		oddsIron.addOddsItem(new RareDropsOddsItem(40 * modifier, Material.APPLE));
		oddsIron.addOddsItem(new RareDropsOddsItem(40 * modifier, Material.BREAD));
		oddsIron.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.BLAZE_POWDER));
		oddsIron.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.ENDER_PEARL));
		oddsIron.addOddsItem(new RareDropsOddsItem(5 * modifier, Material.NETHER_WARTS));
		oddsIron.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.SKULL));
		oddsIron.addOddsItem(new RareDropsOddsItem(1 * modifier, Material.SKULL_ITEM));
		random = new MultipleRareDropsRandomizer(oddsOther);
		result.addAll(random.randomize());
		
		for (RareDropResult drop : result) {
			int nb = 1;
			if (drop.getItemStack().getType() == Material.IRON_INGOT || 
				drop.getItemStack().getType() == Material.GOLD_INGOT ||
				drop.getItemStack().getType() == Material.DIAMOND) {
				
				nb = numberRandom.nextInt(3 * (int)modifier);
				if (nb > 1) {
					drop.getItemStack().setAmount(nb);
				}
				else {
					nb = 1;
				}
				
			}
			
			loot.addItemStack(drop.getItemStack().getType(), nb);	
		}
		
		return loot;
	}
}
