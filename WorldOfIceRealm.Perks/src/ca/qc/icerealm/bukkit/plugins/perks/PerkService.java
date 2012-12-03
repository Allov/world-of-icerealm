package ca.qc.icerealm.bukkit.plugins.perks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ca.qc.icerealm.bukkit.plugins.data.DataPersistenceService;
import ca.qc.icerealm.bukkit.plugins.data.DataSerializationService;

public class PerkService implements Listener {
	private HashMap<String, ArrayList<Perk>> playerPerks = new HashMap<String, ArrayList<Perk>>();
	private ArrayList<Perk> perks = new ArrayList<Perk>();
	
	private static PerkService instance;
	public static PerkService getInstance() {
		if (instance == null) {
			instance = new PerkService();
		}
		
		return instance;
	}
	
	private PerkService() {
		for(Perk perk : SettlerPerks.Perks) {
			perks.add(perk);
		}

		for(Perk perk : AdventurerPerks.Perks) {
			perks.add(perk);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		DataPersistenceService data = new DataSerializationService();
		List<String> perkIds = (ArrayList<String>)data.load("perks", evt.getPlayer().getName());
		
		if (perkIds == null)
			return;

		ArrayList<Perk> pp = new ArrayList<Perk>();
		for(String perkId : perkIds) {
			pp.add(findPerk(perkId));
		}
		playerPerks.put(evt.getPlayer().getName(), pp);
	}
	
	public void clearPerks(Player player) {
		ArrayList<Perk> pp = playerPerks.get(player.getName());
		pp.clear();
		
		savePerks(player);
	}
	
	public void savePerks(Player player) {
		DataPersistenceService data = new DataSerializationService();
		
		List<String> pp = new ArrayList<String>();
		for(Perk perk : playerPerks.get(player.getName())) {
			pp.add(perk.getId());
		}
		
		data.save("perks", player.getName(), pp);
	}

	public Perk purchasePerk(Player player, String perkId) throws Exception {
		ArrayList<Perk> purchasedPerks;
		Perk perk = findPerk(perkId);
		
		if (perk == null) {
			throw new PerkNotFoundException(perkId);
		}
		
		if (player.getLevel() < perk.getCost()) {
			throw new NotEnoughLevelExpcetion(perkId, perk.getCost());
		}
		
		if (!playerPerks.containsKey(player.getName())) {
			purchasedPerks = new ArrayList<Perk>();
			playerPerks.put(player.getName(), purchasedPerks);
		} else {
			purchasedPerks = playerPerks.get(player.getName());
		}
		
		if (!purchasedPerks.contains(perk)) {
			
			if (perk.getRequires() != null) {
				boolean found = false;
				for(String pId : perk.getRequires()) {
					if (playerHasPerk(player, pId)) {
						found = true;
						break;
					}
				}
				
				if (!found) {
					throw new RequirePerkException(perkId, perk.getRequires());
				}
			}
			
			if (purchasedPerks.contains(findPerk(perk.getNemesis()))) {
				throw new CannotPurchaseNemesisPerkException(perkId, perk.getNemesis());
			}
			
			purchasedPerks.add(perk);
			player.setLevel(player.getLevel() - perk.getCost());
			savePerks(player);
			
		} else {
			throw new AlreadyPurchasedPerkException(perkId);
		}
		
		return perk;
	}

	public boolean playerHasPerk(Player player, String perkId) {
		if (!playerPerks.containsKey(player.getName()))
			return false;
		
		ArrayList<Perk> pp = playerPerks.get(player.getName());
		
		if (pp != null) {
			for(Perk perk : pp) {
				if (perk.getId().equals(perkId)) {
					return true;
				}
			}
		}
		
		return false;
	}

	private Perk findPerk(String perkId) {
		for (Perk perk : getPerks()) {
			if (perk.getId().equals(perkId)) {
				return perk;
			}
		}
		
		return null;
	}

	public ArrayList<Perk> getPerks() {
		return perks;
	}

	public ArrayList<Perk> getPlayerPerks(Player player) {
		return playerPerks.get(player.getName());		
	}
}