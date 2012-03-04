package ca.qc.icerealm.bukkit.plugins.raredrops.randomizer;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.inventory.ItemStack;

import ca.qc.icerealm.bukkit.plugins.common.MaterialUtil;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropResult;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOdds;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsOddsItem;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.EnchantmentsRandomizer;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.data.EnchantmentResult;

public interface RareDropsRandomizer 
{
	public ArrayList<RareDropResult> randomize();
}
