package ca.qc.icerealm.bukkit.plugins.perks.magic.data;

import java.util.Hashtable;

import ca.qc.icerealm.bukkit.plugins.perks.magic.MagicPerk;

public class MagicTogglingData 
{
	private Hashtable<Integer, MagicPerk> magicData = new Hashtable<Integer, MagicPerk>();

	public Hashtable<Integer, MagicPerk> getCurrentMagic() 
	{
		return magicData;
	}
	
	public MagicPerk getCurrentMagic(int school) 
	{
		MagicPerk magicPerk = magicData.get(school);
		
		if (magicPerk == null)
		{
			magicData.put(school, MagicData.getFirstMagicInstance(school));
		}
		
		return magicData.get(school);
	}

	public void setCurrentMagic(Hashtable<Integer, MagicPerk> magicData) 
	{
		this.magicData = magicData;
	}
	
	public void setCurrentMagic(int school, MagicPerk magicPerk) 
	{
		this.magicData.put(school, magicPerk);
	}
}
