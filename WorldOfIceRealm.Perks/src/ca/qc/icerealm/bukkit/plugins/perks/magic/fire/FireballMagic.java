package ca.qc.icerealm.bukkit.plugins.perks.magic.fire;

import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

import ca.qc.icerealm.bukkit.plugins.perks.magic.MagicPerk;
import ca.qc.icerealm.bukkit.plugins.perks.magic.data.MagicUtils;

public class FireballMagic implements MagicPerk
{
	public void executeMagic(Player player)
	{
		SmallFireball fb = player.launchProjectile(SmallFireball.class);
		fb.setIsIncendiary(true);
		fb.setBounce(false);
		fb.setShooter(player);
		//fb.setVelocity(new Vector(1.0, 0, 0));
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
		return MagicUtils.FIREBALL;
	}

	@Override
	public int getFoodCost() 
	{
		return 1;
	}

	@Override
	public int getNextTogglingMagicId() 
	{
		return MagicUtils.BIGFIREBALL;
	}
	
	@Override
	public String getDisplayName() 
	{
		return "Fireball";
	}
	
	@Override
	public String getPerkId() 
	{
		return FireTree.FireballId;
	}
	
	@Override
	public long getCoolDown() 
	{
		return 1000;
	}
}
