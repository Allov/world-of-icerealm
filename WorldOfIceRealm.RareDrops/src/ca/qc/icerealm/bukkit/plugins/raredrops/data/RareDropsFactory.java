package ca.qc.icerealm.bukkit.plugins.raredrops.data;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;

import ca.qc.icerealm.bukkit.plugins.common.MapWrapper;
import ca.qc.icerealm.bukkit.plugins.raredrops.data.RareDropsMultipliers;
import ca.qc.icerealm.bukkit.plugins.raredrops.enchantment.data.EnchantmentsOdds;

public class RareDropsFactory 
{
	public final Logger logger = Logger.getLogger(("RareDropsFactory"));
	private RareDropsMultipliers oddsMultiplier = null;
	List<MapWrapper> materials = null;
		
	public RareDropsMultipliers getOddsMultiplier() 
	{
		return oddsMultiplier;
	}

	public void setOddsMultiplier(RareDropsMultipliers oddsMultiplier) 
	{
		this.oddsMultiplier = oddsMultiplier;
	}
	
	public RareDropsFactory(List<MapWrapper> materials)
	{
		this.materials = materials;
		oddsMultiplier = new RareDropsMultipliers();
	}
	
	public RareDropsFactory(List<MapWrapper> materials, RareDropsMultipliers _oddsMultiplier)
	{
		this.materials = materials;
		oddsMultiplier = _oddsMultiplier;
	}
	
	public RareDropsOdds createOdds()
	{
		RareDropsOdds odds = new RareDropsOdds();	
		
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
				
				// Now applying multiplier, which can be applied externally (plugins)
				// Item are divided into 3 groups. High value (diamond stuff), Medium value (iron stuff) and low value (everything else)
				double multiplier = oddsMultiplier.getLowValueMultiplier(); // Default multiplier
				
				if (RareDropsMultipliers.isHighValue(item.getItem()))
				{
					multiplier = oddsMultiplier.getHighValueMultiplier();
				}
				else if (RareDropsMultipliers.isMediumValue(item.getItem()))
				{
					multiplier = oddsMultiplier.getMediumValueMultiplier();
				}

				item.setPercentage(material.getDouble("odds", 0.00) * multiplier);
				
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
		
		return odds;
	}
}
