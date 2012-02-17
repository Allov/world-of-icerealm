package ca.qc.icerealm.bukkit.plugins.raredrops.enchantment;

public class EnchantmentsOdds 
{
	public static final double[] AVERAGE_ODDS = new double[]{25.00, 12.00, 5.00};
	public static final double[] LOW_ODDS = new double[]{15.00, 10.00, 5.00};
	public static final double[] HIGH_ODDS = new double[]{25.00, 18.00, 8.00, 4.00};
	public static final double[] BOSS_HIGH_ODDS = new double[]{18.00, 18.00, 25.00, 12.00};
	
	public EnchantmentsOdds()
	{
		
	}
			
	public EnchantmentsOdds(double[] percentageEnchantments)
	{
		this.percentageEnchantments = percentageEnchantments;
	}
	
	private double[] percentageEnchantments = null;
	
	public double[] getPercentageEnchantments() 
	{
		return percentageEnchantments;
	}
	
	/*
	 * Chances d'obtenir un enchantement pour chaque level a partir du level 1. Le nombre de levels est optionnel.
	 * 
	 * EX: new int[]{20.00, 10.00, 5.00};
	 * 
	 * 20% de chance d'avoir n'importe quel enchantement level 1, 10% pour le level 2, 5% pour le level 3.
	 */
	public void setPercentageEnchantments(double[] percentageEnchantments) 
	{
		this.percentageEnchantments = percentageEnchantments;
	}
}
