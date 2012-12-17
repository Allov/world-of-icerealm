package ca.qc.icerealm.bukkit.plugins.perks.magic;

import org.bukkit.entity.Player;

public interface MagicPerk 
{
	public String getDisplayName();
	public int getMagicSchool();
	public int getMagicId();
	public double getHungerCost();
	public int getNextTogglingMagicId();
	public void executeMagic(Player player);
}