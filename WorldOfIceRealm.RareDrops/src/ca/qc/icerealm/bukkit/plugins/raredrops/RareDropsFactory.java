package ca.qc.icerealm.bukkit.plugins.raredrops;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.Monster;
import ca.qc.icerealm.bukkit.plugins.common.EntityUtilities;

public class RareDropsFactory 
{
	public final Logger logger = Logger.getLogger(("Minecraft"));
	private int oddsMultiplier = 1;
		
	public int getOddsMultiplier() 
	{
		return oddsMultiplier;
	}

	public void setOddsMultiplier(int oddsMultiplier) 
	{
		this.oddsMultiplier = oddsMultiplier;
	}
	
	public RareDropsFactory()
	{
		
	}
	
	public RareDropsFactory(int _oddsMultiplier)
	{
		oddsMultiplier = _oddsMultiplier;
	}
	
	public RareDropsOdds createEntityOdds(Monster monster)
	{
		RareDropsOdds odds = new RareDropsOdds();	
		odds.setMonster(monster);		
		int entityId = EntityUtilities.getEntityId(monster);
		
		switch (entityId)
		{
			case EntityUtilities.Zombie:
			{
				this.logger.info("Killed Zombie");
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.LEATHER));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_PICKAXE));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_AXE));
				odds.addOddsItem(new RareDropsOddsItem(0.25 * oddsMultiplier, Material.IRON_SWORD));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_HOE));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_SPADE));
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.FLINT));
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.APPLE));
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.RAW_BEEF));
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.RAW_CHICKEN));
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.RAW_FISH));
				odds.addOddsItem(new RareDropsOddsItem(0.10 * oddsMultiplier, Material.IRON_BOOTS));
				odds.addOddsItem(new RareDropsOddsItem(0.10 * oddsMultiplier, Material.IRON_CHESTPLATE));
				odds.addOddsItem(new RareDropsOddsItem(0.10 * oddsMultiplier, Material.IRON_HELMET));
				odds.addOddsItem(new RareDropsOddsItem(0.10 * oddsMultiplier, Material.IRON_LEGGINGS));			
				break;
			}
			case EntityUtilities.Spider:
			{
				this.logger.info("Killed Spider");
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.BREAD));
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.COOKIE));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.POTION));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.INK_SACK));
				break;
			}
			case EntityUtilities.Skeleton:
			{
				this.logger.info("Killed Skeleton");
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_PICKAXE));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_AXE));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_SWORD));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_HOE));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_SPADE));	
				odds.addOddsItem(new RareDropsOddsItem(0.25 * oddsMultiplier, Material.DIAMOND_PICKAXE));
				odds.addOddsItem(new RareDropsOddsItem(0.25 * oddsMultiplier, Material.DIAMOND_SPADE));
				odds.addOddsItem(new RareDropsOddsItem(0.25 * oddsMultiplier, Material.DIAMOND_AXE));
				odds.addOddsItem(new RareDropsOddsItem(0.25 * oddsMultiplier, Material.DIAMOND_HOE));
				odds.addOddsItem(new RareDropsOddsItem(0.10 * oddsMultiplier, Material.DIAMOND_SWORD));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.BOW));	
				odds.addOddsItem(new RareDropsOddsItem(0.15 * oddsMultiplier, Material.IRON_BOOTS));
				odds.addOddsItem(new RareDropsOddsItem(0.15 * oddsMultiplier, Material.IRON_CHESTPLATE));
				odds.addOddsItem(new RareDropsOddsItem(0.15 * oddsMultiplier, Material.IRON_HELMET));
				odds.addOddsItem(new RareDropsOddsItem(0.15 * oddsMultiplier, Material.IRON_LEGGINGS));	
				break;
			}
			case EntityUtilities.Creeper:
			{
				this.logger.info("Killed Creeper");
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.FLINT_AND_STEEL));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.REDSTONE_ORE));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.BLAZE_POWDER));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.GOLDEN_APPLE));
				odds.addOddsItem(new RareDropsOddsItem(0.75 * oddsMultiplier, Material.IRON_BOOTS));
				odds.addOddsItem(new RareDropsOddsItem(0.75 * oddsMultiplier, Material.IRON_CHESTPLATE));
				odds.addOddsItem(new RareDropsOddsItem(0.75 * oddsMultiplier, Material.IRON_HELMET));
				odds.addOddsItem(new RareDropsOddsItem(0.75 * oddsMultiplier, Material.IRON_LEGGINGS));
				odds.addOddsItem(new RareDropsOddsItem(0.03 * oddsMultiplier, Material.DIAMOND_CHESTPLATE));
				odds.addOddsItem(new RareDropsOddsItem(0.03 * oddsMultiplier, Material.DIAMOND_HELMET));
				odds.addOddsItem(new RareDropsOddsItem(0.03 * oddsMultiplier, Material.DIAMOND_LEGGINGS));
				odds.addOddsItem(new RareDropsOddsItem(0.03 * oddsMultiplier, Material.DIAMOND_BOOTS));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_3));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_4));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_5));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_6));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_7));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_8));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_9));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_10));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_11));
				break;	
			}
			case EntityUtilities.Enderman:
			{
				this.logger.info("Killed Enderman");
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.DIRT));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.STONE));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.BLAZE_POWDER));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.GOLDEN_APPLE));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_BOOTS));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_CHESTPLATE));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_HELMET));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_LEGGINGS));
				odds.addOddsItem(new RareDropsOddsItem(0.05 * oddsMultiplier, Material.DIAMOND_CHESTPLATE));
				odds.addOddsItem(new RareDropsOddsItem(0.05 * oddsMultiplier, Material.DIAMOND_HELMET));
				odds.addOddsItem(new RareDropsOddsItem(0.05 * oddsMultiplier, Material.DIAMOND_LEGGINGS));
				odds.addOddsItem(new RareDropsOddsItem(0.05 * oddsMultiplier, Material.DIAMOND_BOOTS));
				odds.addOddsItem(new RareDropsOddsItem(0.25 * oddsMultiplier, Material.DIAMOND_SWORD));
				odds.addOddsItem(new RareDropsOddsItem(5.00 * oddsMultiplier, Material.MELON));
				break;	
			}
			case EntityUtilities.Slime:
			{
				this.logger.info("Killed Slime");
				odds.addOddsItem(new RareDropsOddsItem(0.25 * oddsMultiplier, Material.CAKE));
				odds.addOddsItem(new RareDropsOddsItem(5.00 * oddsMultiplier, Material.CLAY));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.MAP));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.GOLDEN_APPLE));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_3));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_4));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_5));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_6));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_7));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_8));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_9));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_10));
				odds.addOddsItem(new RareDropsOddsItem(0.01 * oddsMultiplier, Material.RECORD_11));	
				break;
			}
			case EntityUtilities.CaveSpider:
			{
				this.logger.info("Killed Cave Spider");
				odds.addOddsItem(new RareDropsOddsItem(6.00 * oddsMultiplier, Material.BREAD));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.COOKIE));
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.LAPIS_ORE));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.INK_SACK));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.RAILS));
				break;
			}
			case EntityUtilities.EnderDragon:
			{
				this.logger.info("Killed Enderdragon");
				odds.addOddsItem(new RareDropsOddsItem(5.00 * oddsMultiplier, Material.GOLDEN_APPLE));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_BOOTS));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_CHESTPLATE));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_HELMET));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_LEGGINGS));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.DIAMOND_CHESTPLATE));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.DIAMOND_HELMET));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.DIAMOND_LEGGINGS));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.DIAMOND_BOOTS));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.DIAMOND_PICKAXE));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.DIAMOND_SPADE));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.DIAMOND_AXE));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.DIAMOND_HOE));
				odds.addOddsItem(new RareDropsOddsItem(1.50 * oddsMultiplier, Material.DIAMOND_SWORD));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_PICKAXE));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_AXE));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_SWORD));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_HOE));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_SPADE));
				break;
			}
			default:
			{
				break;
			}
		}
		
		return odds;
	}
}
