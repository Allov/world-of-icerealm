package ca.qc.icerealm.bukkit.plugins.raredrops;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;

import ca.qc.icerealm.bukkit.plugins.common.MapWrapper;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.EnchantmentsOdds;

public class RareDropsFactory 
{
	public final Logger logger = Logger.getLogger(("RareDropsFactory"));
	private double oddsMultiplier = 1;
	List<MapWrapper> materials = null;
		
	public double getOddsMultiplier() 
	{
		return oddsMultiplier;
	}

	public void setOddsMultiplier(int oddsMultiplier) 
	{
		this.oddsMultiplier = oddsMultiplier;
	}
	
	public RareDropsFactory(List<MapWrapper> materials)
	{
		this.materials = materials;
	}
	
	public RareDropsFactory(List<MapWrapper> materials, double _oddsMultiplier)
	{
		this.materials = materials;
		oddsMultiplier = _oddsMultiplier;
	}
	
	public RareDropsOdds createOdds()
	{
		RareDropsOdds odds = new RareDropsOdds();	
		
		//List<MapWrapper> materials = configWrapper.getMapList("mobs." + entityName + ".materials", new ArrayList<MapWrapper>());

		for (MapWrapper material : materials) 
		{
			RareDropsOddsItem item = new RareDropsOddsItem();
			
			// Make sure the material exists
			if (Material.getMaterial(material.getString("name", null)) == null)
			{
				logger.warning("Invalid material name, ignoring odds for item: " + material.getString("name", null));
			}
			else
			{			
				item.setItem(Material.getMaterial(material.getString("name", null)));		
				item.setCustomName(material.getString("custom name", null));
				item.setPercentage(material.getDouble("odds", 0.00) * oddsMultiplier);
				item.setEnchantmentsAs(Material.getMaterial(material.getString("enchantments as", null)));
	
				String enchantments = material.getString("enchantments", null);
	
				if (enchantments != null)
				{
					enchantments = enchantments.trim();
					// Custom enchantments
					if (enchantments.contains(","))
					{
						try
						{
							String[] tempEnch = enchantments.split(",");
							
							double[] customEnch = new double[tempEnch.length];
							
							for(int i = 0; i < customEnch.length; i++)
							{
								customEnch[i] = Double.parseDouble(tempEnch[i].trim());
							}
							
							item.setEnchantmentsOdds(new EnchantmentsOdds(customEnch));
						}
						catch (Exception e)
						{
							logger.warning("Invalid custom enchantements, ignoring enchantments odds: " + enchantments);
						}
					}
					else // Use presets
					{
						if (enchantments.equalsIgnoreCase("low odds"))
						{
							item.setEnchantmentsOdds(new EnchantmentsOdds(EnchantmentsOdds.LOW_ODDS));
						}
						else if (enchantments.equalsIgnoreCase("average odds"))
						{
							item.setEnchantmentsOdds(new EnchantmentsOdds(EnchantmentsOdds.AVERAGE_ODDS));
						}
						else if (enchantments.equalsIgnoreCase("high odds"))
						{
							item.setEnchantmentsOdds(new EnchantmentsOdds(EnchantmentsOdds.HIGH_ODDS));
						}
						else if (enchantments.equalsIgnoreCase("boss high odds"))
						{
							item.setEnchantmentsOdds(new EnchantmentsOdds(EnchantmentsOdds.BOSS_HIGH_ODDS));
						}
						else if (enchantments.equalsIgnoreCase("sure low odds"))
						{
							item.setEnchantmentsOdds(new EnchantmentsOdds(EnchantmentsOdds.SURE_LOW_ODDS));
						}
						else if (enchantments.equalsIgnoreCase("sure average odds"))
						{
							item.setEnchantmentsOdds(new EnchantmentsOdds(EnchantmentsOdds.SURE_AVERAGE_ODDS));
						}
						else if (enchantments.equalsIgnoreCase("sure high odds"))
						{
							item.setEnchantmentsOdds(new EnchantmentsOdds(EnchantmentsOdds.SURE_HIGH_ODDS));
						}
						else
						{
							logger.warning("Unknown enchantments preset, ignoring enchantments odds: " + enchantments);
						}
					}
				}
				
				odds.addOddsItem(item);
			}
		}

		/*switch (entityId)
		{
			case EntityUtilities.Zombie:
			{
				//this.logger.info("Killed Zombie");
				odds.addOddsItem(new RareDropsOddsItem(5.00 * oddsMultiplier, Material.FEATHER));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.LEATHER));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_PICKAXE, new EnchantmentsOdds (EnchantmentsOdds.LOW_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_AXE, new EnchantmentsOdds (EnchantmentsOdds.LOW_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.25 * oddsMultiplier, Material.IRON_SWORD, new EnchantmentsOdds (EnchantmentsOdds.LOW_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_HOE));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_SPADE, new EnchantmentsOdds (EnchantmentsOdds.LOW_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.FLINT));
				odds.addOddsItem(new RareDropsOddsItem(5.00 * oddsMultiplier, Material.APPLE));
				odds.addOddsItem(new RareDropsOddsItem(4.00 * oddsMultiplier, Material.RAW_BEEF));
				odds.addOddsItem(new RareDropsOddsItem(4.00 * oddsMultiplier, Material.RAW_CHICKEN));
				odds.addOddsItem(new RareDropsOddsItem(4.00 * oddsMultiplier, Material.RAW_FISH));
				odds.addOddsItem(new RareDropsOddsItem(0.10 * oddsMultiplier, Material.IRON_BOOTS, new EnchantmentsOdds (EnchantmentsOdds.LOW_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.10 * oddsMultiplier, Material.IRON_CHESTPLATE, new EnchantmentsOdds (EnchantmentsOdds.LOW_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.10 * oddsMultiplier, Material.IRON_HELMET, new EnchantmentsOdds (EnchantmentsOdds.LOW_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.10 * oddsMultiplier, Material.IRON_LEGGINGS, new EnchantmentsOdds (EnchantmentsOdds.LOW_ODDS)));			
				break;
			}
			case EntityUtilities.Spider:
			{
				//this.logger.info("Killed Spider");
				odds.addOddsItem(new RareDropsOddsItem(7.00 * oddsMultiplier, Material.BREAD));
				odds.addOddsItem(new RareDropsOddsItem(5.00 * oddsMultiplier, Material.COOKIE));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.POTION));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.INK_SACK));
				break;
			}
			case EntityUtilities.Skeleton:
			{
				//this.logger.info("Killed Skeleton");
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_PICKAXE, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_AXE, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_SWORD, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_HOE));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_SPADE, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));	
				odds.addOddsItem(new RareDropsOddsItem(0.15 * oddsMultiplier, Material.DIAMOND_PICKAXE, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.15 * oddsMultiplier, Material.DIAMOND_SPADE, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.15 * oddsMultiplier, Material.DIAMOND_AXE, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.15 * oddsMultiplier, Material.DIAMOND_HOE));
				odds.addOddsItem(new RareDropsOddsItem(0.15 * oddsMultiplier, Material.DIAMOND_SWORD, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(1.50 * oddsMultiplier, Material.BOW, new EnchantmentsOdds (EnchantmentsOdds.LOW_ODDS)));	
				odds.addOddsItem(new RareDropsOddsItem(0.15 * oddsMultiplier, Material.IRON_BOOTS, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.15 * oddsMultiplier, Material.IRON_CHESTPLATE, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.15 * oddsMultiplier, Material.IRON_HELMET, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.15 * oddsMultiplier, Material.IRON_LEGGINGS, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));	
				break;
			}
			case EntityUtilities.Creeper:
			{
				//this.logger.info("Killed Creeper");
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.CAKE));
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.FLINT_AND_STEEL));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.REDSTONE_ORE));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.BLAZE_POWDER));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.GOLDEN_APPLE));
				odds.addOddsItem(new RareDropsOddsItem(0.75 * oddsMultiplier, Material.IRON_BOOTS, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.75 * oddsMultiplier, Material.IRON_CHESTPLATE, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.75 * oddsMultiplier, Material.IRON_HELMET, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.75 * oddsMultiplier, Material.IRON_LEGGINGS, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.03 * oddsMultiplier, Material.DIAMOND_CHESTPLATE,  new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.03 * oddsMultiplier, Material.DIAMOND_HELMET,  new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.03 * oddsMultiplier, Material.DIAMOND_LEGGINGS,  new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.03 * oddsMultiplier, Material.DIAMOND_BOOTS,  new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
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
				//this.logger.info("Killed Enderman");
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.DIRT));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.STONE));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.BLAZE_POWDER));
				odds.addOddsItem(new RareDropsOddsItem(5.00 * oddsMultiplier, Material.GOLDEN_APPLE));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_BOOTS, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_CHESTPLATE, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_HELMET, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.IRON_LEGGINGS, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.05 * oddsMultiplier, Material.DIAMOND_CHESTPLATE, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.05 * oddsMultiplier, Material.DIAMOND_HELMET, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.05 * oddsMultiplier, Material.DIAMOND_LEGGINGS, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.05 * oddsMultiplier, Material.DIAMOND_BOOTS, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.25 * oddsMultiplier, Material.DIAMOND_SWORD, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(10.00 * oddsMultiplier, Material.MELON));
				break;	
			}
			case EntityUtilities.Slime:
			{
				//this.logger.info("Killed Slime");
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.CAKE));
				odds.addOddsItem(new RareDropsOddsItem(5.00 * oddsMultiplier, Material.CLAY));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.MAP));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.GOLDEN_APPLE));
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
				//this.logger.info("Killed Cave Spider");
				odds.addOddsItem(new RareDropsOddsItem(7.00 * oddsMultiplier, Material.BREAD));
				odds.addOddsItem(new RareDropsOddsItem(4.00 * oddsMultiplier, Material.COOKIE));
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.LAPIS_ORE));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.INK_SACK));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.RAILS));
				break;
			}
			case EntityUtilities.PigZombie:
			{
				//this.logger.info("Killed Pig Zombie");
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.FLINT));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.LAVA_BUCKET));
				odds.addOddsItem(new RareDropsOddsItem(0.03 * oddsMultiplier, Material.DIAMOND_BOOTS, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.03 * oddsMultiplier, Material.DIAMOND_CHESTPLATE, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.03 * oddsMultiplier, Material.DIAMOND_HELMET, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.03 * oddsMultiplier, Material.DIAMOND_LEGGINGS, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));	
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.GOLD_BOOTS, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.GOLD_CHESTPLATE, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.GOLD_HELMET, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.50 * oddsMultiplier, Material.GOLD_LEGGINGS, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));	
				odds.addOddsItem(new RareDropsOddsItem(0.10 * oddsMultiplier, Material.DIAMOND_SWORD, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.GOLD_SWORD, new EnchantmentsOdds (EnchantmentsOdds.HIGH_ODDS)));
				break;
			}
			case EntityUtilities.Ghast:
			{
				//this.logger.info("Killed Pig Ghast");
				odds.addOddsItem(new RareDropsOddsItem(5.00 * oddsMultiplier, Material.GLOWSTONE_DUST));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.FEATHER));
				odds.addOddsItem(new RareDropsOddsItem(5.00 * oddsMultiplier, Material.POTION));
				break;
			}
			case EntityUtilities.MagmaCube:
			{
				//this.logger.info("Killed Pig Ghast");
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.LAPIS_ORE));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.LAVA_BUCKET));
				odds.addOddsItem(new RareDropsOddsItem(2.00 * oddsMultiplier, Material.MAGMA_CREAM));
				break;
			}
			case EntityUtilities.Blaze:
			{
				//this.logger.info("Killed Pig Blaze");
				odds.addOddsItem(new RareDropsOddsItem(5.00 * oddsMultiplier, Material.GOLD_INGOT));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.GOLDEN_APPLE));
				odds.addOddsItem(new RareDropsOddsItem(0.06 * oddsMultiplier, Material.DIAMOND_BOOTS, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.06 * oddsMultiplier, Material.DIAMOND_CHESTPLATE, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.06 * oddsMultiplier, Material.DIAMOND_HELMET, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(0.06 * oddsMultiplier, Material.DIAMOND_LEGGINGS, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));	
				odds.addOddsItem(new RareDropsOddsItem(0.20 * oddsMultiplier, Material.DIAMOND_SWORD, new EnchantmentsOdds (EnchantmentsOdds.AVERAGE_ODDS)));
				break;
			}
			case EntityUtilities.EnderDragon:
			{
				//this.logger.info("Killed Enderdragon");
				odds.addOddsItem(new RareDropsOddsItem(20.00 * oddsMultiplier, Material.GOLDEN_APPLE));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_BOOTS, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_CHESTPLATE, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_HELMET, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_LEGGINGS, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.DIAMOND_CHESTPLATE, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.DIAMOND_HELMET, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.DIAMOND_LEGGINGS, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(1.00 * oddsMultiplier, Material.DIAMOND_BOOTS, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.DIAMOND_PICKAXE, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.DIAMOND_SPADE, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.DIAMOND_AXE, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.DIAMOND_HOE));
				odds.addOddsItem(new RareDropsOddsItem(3.00 * oddsMultiplier, Material.DIAMOND_SWORD, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_PICKAXE, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_AXE, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_SWORD, new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_HOE));
				odds.addOddsItem(new RareDropsOddsItem(7.50 * oddsMultiplier, Material.IRON_SPADE,new EnchantmentsOdds (EnchantmentsOdds.BOSS_HIGH_ODDS)));
				break;
			}
			default:
			{
				break;
			}
		}*/

		return odds;
	}
}
