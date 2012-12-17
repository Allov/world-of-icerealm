package ca.qc.icerealm.bukkit.plugins.perks.magic.fire;

import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

import ca.qc.icerealm.bukkit.plugins.perks.magic.MagicPerk;
import ca.qc.icerealm.bukkit.plugins.perks.magic.data.MagicData;

public class FireballMagic implements MagicPerk
{
	public void executeMagic(Player player)
	{
		SmallFireball fb = player.launchProjectile(SmallFireball.class);
		fb.setIsIncendiary(true);
		fb.setBounce(false);
		fb.setShooter(player);
		//fb.setDirection(p.getVelocity().add(p.getLocation().toVector().subtract(this.b.getEntity().getLocation().toVector()).normalize().multiply(Integer.MAX_VALUE)));
	}

	@Override
	public int getMagicSchool() 
	{
		return 0;
	}

	@Override
	public int getMagicId() 
	{
		return MagicData.FIREBALL;
	}

	@Override
	public double getHungerCost() 
	{
		return 0.25;
	}

	@Override
	public int getNextTogglingMagicId() 
	{
		return MagicData.BIGFIREBALL;
	}
	
	@Override
	public String getDisplayName() 
	{
		return "Fireball";
	}
}
