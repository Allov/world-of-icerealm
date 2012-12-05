package ca.qc.icerealm.bukkit.plugins.raredrops.data;

import org.bukkit.Material;

public class RareDropsMultipliers 
{
	private Double lowValueMultiplier = 1.00;
	private Double mediumValueMultiplier = 1.00;
	private Double highValueMultiplier = 1.00;
	
	public Double getLowValueMultiplier() {
		return lowValueMultiplier;
	}

	public void setLowValueMultiplier(Double lowValueMultiplier) {
		this.lowValueMultiplier = lowValueMultiplier;
	}

	public Double getMediumValueMultiplier() {
		return mediumValueMultiplier;
	}

	public void setMediumValueMultiplier(Double mediumValueMultiplier) {
		this.mediumValueMultiplier = mediumValueMultiplier;
	}

	public Double getHighValueMultiplier() {
		return highValueMultiplier;
	}

	public void setHighValueMultiplier(Double highValueMultiplier) {
		this.highValueMultiplier = highValueMultiplier;
	}
	
	public RareDropsMultipliers()
	{
		
	}
	
	public RareDropsMultipliers(Double generalMultiplier)
	{
		this.lowValueMultiplier = lowValueMultiplier * generalMultiplier;
		this.mediumValueMultiplier = mediumValueMultiplier * generalMultiplier;
		this.highValueMultiplier = highValueMultiplier * generalMultiplier;	
	}
	
	public RareDropsMultipliers(Double lowValueMultiplier, Double mediumValueMultiplier, Double highValueMultiplier)
	{
		this.lowValueMultiplier = lowValueMultiplier;
		this.mediumValueMultiplier = mediumValueMultiplier;
		this.highValueMultiplier = highValueMultiplier;	
	}
	
	public static boolean isHighValue(Material material)
	{
		return material.equals(Material.DIAMOND) ||
				material.equals(Material.DIAMOND_AXE) ||
				material.equals(Material.DIAMOND_BOOTS) ||
				material.equals(Material.DIAMOND_CHESTPLATE) ||
				material.equals(Material.DIAMOND_HELMET) ||
				material.equals(Material.DIAMOND_HOE) ||
				material.equals(Material.DIAMOND_LEGGINGS) ||
				material.equals(Material.DIAMOND_ORE) ||
				material.equals(Material.DIAMOND_PICKAXE) ||
				material.equals(Material.DIAMOND_SPADE) ||
				material.equals(Material.DIAMOND_SWORD) ||
				material.equals(Material.ENDER_PEARL);		
	}
	
	public static boolean isMediumValue(Material material)
	{
		return material.equals(Material.IRON_AXE) ||
				material.equals(Material.IRON_BOOTS) ||
				material.equals(Material.IRON_CHESTPLATE) ||
				material.equals(Material.IRON_HELMET) ||
				material.equals(Material.IRON_HOE) ||
				material.equals(Material.IRON_LEGGINGS) ||
			    material.equals(Material.IRON_PICKAXE) ||
			    material.equals(Material.IRON_SPADE) ||
			    material.equals(Material.IRON_SWORD) ||
			    material.equals(Material.EMERALD) ||
			    material.equals(Material.ENDER_PEARL) ||
			    material.equals(Material.RECORD_10) ||
			    material.equals(Material.RECORD_9) ||
			    material.equals(Material.RECORD_8) ||
			    material.equals(Material.RECORD_7) ||
			    material.equals(Material.RECORD_6) ||
			    material.equals(Material.RECORD_5) ||
			    material.equals(Material.RECORD_4) ||
			    material.equals(Material.RECORD_3) ||
			    material.equals(Material.RECORD_11) ||
			    material.equals(Material.RECORD_12) ||
				material.equals(Material.BLAZE_ROD);		
	}
	
	public static boolean isLowValue(Material material)
	{
		return !(isHighValue(material) || isMediumValue(material));
	}
}
