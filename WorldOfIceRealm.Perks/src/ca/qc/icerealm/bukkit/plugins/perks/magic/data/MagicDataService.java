package ca.qc.icerealm.bukkit.plugins.perks.magic.data;

import java.util.Hashtable;
import java.util.logging.Logger;
import org.bukkit.entity.Player;

import ca.qc.icerealm.bukkit.plugins.perks.PerkService;
import ca.qc.icerealm.bukkit.plugins.perks.magic.MagicPerk;


public class MagicDataService 
{
	public static final int MAX_FOOD_LEVEL = 20;
	public static final int MINIMUM_FOOD_LEVEL = 2;
	
    private static final MagicDataService instance = new MagicDataService();
    private Hashtable<String, PlayerMagicData> magicData = new Hashtable<String, PlayerMagicData>();
    public final Logger logger = Logger.getLogger(("Minecraft"));
    
    private MagicDataService() 
    {

    }
   
    public static synchronized MagicDataService getInstance() 
    {
        return instance;
    }

	public Hashtable<String, PlayerMagicData> getMagicData() 
	{
		return magicData;
	}
	
	public PlayerMagicData getMagicData(String playerName) 
	{
		PlayerMagicData data = magicData.get(playerName);
		
		if (data == null)
		{
			magicData.put(playerName, new PlayerMagicData());
		}
			
		return magicData.get(playerName);
	}
	
	public MagicPerk getCurrentMagicPerk(String playerName, int school) 
	{
		PlayerMagicData data = getMagicData(playerName);
		return data.getCurrentMagic(school);
	}
	
	public void toggle(Player player, int school)
	{
		PlayerMagicData data = getMagicData(player.getName());
		
		// Retrieve current magic
		MagicPerk currentPerk =  getCurrentMagicPerk(player.getName(), school);
		// Get next magic and test it
		MagicPerk nextMagicPerk = MagicUtils.getMagicInstance(currentPerk.getNextTogglingMagicId());
		
		// If next magic isn't available (not learned), stick with first magic for this school
		if (!PerkService.getInstance().playerHasPerk(player, nextMagicPerk.getPerkId()))
		{
			nextMagicPerk = MagicUtils.getFirstMagicInstance(school);
		}

		data.setCurrentMagic(school, nextMagicPerk);
	}

	public void resetMagicData(Player player) 
	{
		magicData.remove(player.getName());
	}
}