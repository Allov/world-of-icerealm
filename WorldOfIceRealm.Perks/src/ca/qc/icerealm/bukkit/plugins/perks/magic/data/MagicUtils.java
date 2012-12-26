package ca.qc.icerealm.bukkit.plugins.perks.magic.data;

import ca.qc.icerealm.bukkit.plugins.perks.magic.MagicPerk;
import ca.qc.icerealm.bukkit.plugins.perks.magic.fire.*;

public class MagicUtils 
{
	public static final int SCHOOL_OF_FIRE = 1;
	public static final int SCHOOL_OF_DARKNESS = 2;
	public static final int SCHOOL_OF_LIGHT = 3;
	public static final int SCHOOL_OF_WATER = 4;
	
	public static final int FIREBALL = 1;
	public static final int BIGFIREBALL = 2;
	
	public static MagicPerk getFirstMagicInstance(int school)
	{
		if (school == SCHOOL_OF_FIRE)
		{
			return new FireballMagic();
		}
		else if (school == SCHOOL_OF_DARKNESS)
		{
			return new FireballMagic();
		}
		else if (school == SCHOOL_OF_LIGHT)
		{
			return new FireballMagic();
		}
		else if (school == SCHOOL_OF_WATER)
		{
			return new FireballMagic();
		}
		
		return null;
	}
	
	public static MagicPerk getMagicInstance(int magicPerk)
	{
		if (magicPerk == FIREBALL)
		{
			return new FireballMagic();
		}
		else if (magicPerk == BIGFIREBALL)
		{
			return new BigFireballMagic();
		}
		
		return null;
	}
}
