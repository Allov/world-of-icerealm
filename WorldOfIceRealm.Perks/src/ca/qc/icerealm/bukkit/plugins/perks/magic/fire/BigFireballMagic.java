package ca.qc.icerealm.bukkit.plugins.perks.magic.fire;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.perks.magic.MagicPerk;
import ca.qc.icerealm.bukkit.plugins.perks.magic.data.MagicData;

public class BigFireballMagic implements MagicPerk
{
	public void executeMagic(Player player)
	{
		Fireball fb = player.launchProjectile(Fireball.class);
		fb.setIsIncendiary(true);
		fb.setBounce(false);
		fb.setShooter(player);
	//	fb.setVelocity(new Vector(1.0,0,0));
	}
	
	@Override
	public int getMagicSchool() 
	{
		return 0;
	}

	@Override
	public int getMagicId() 
	{
		return MagicData.BIGFIREBALL;
	}

	@Override
	public int getFoodCost() 
	{
		return 2;
	}

	@Override
	public int getNextTogglingMagicId() 
	{
		return MagicData.FIREBALL;
	}

	@Override
	public String getDisplayName() 
	{
		return "Big Fireball";
	}

	@Override
	public String getPerkId() 
	{
		return FireTree.BigFireballId;
	}
}
