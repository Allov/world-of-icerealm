package ca.qc.icerealm.bukkit.plugins.trading;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TradingPlugin extends JavaPlugin implements Listener {
	
	int id = 0;
	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent evt) {
		
		if (evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (evt.getClickedBlock().getType().equals(Material.EMERALD_BLOCK)) {
				
				VillagerTradeOffer[] offers = new VillagerTradeOffer[] {
						new VillagerTradeOffer(new ItemStack(Material.EMERALD, 24), new ItemStack(Material.DIAMOND_SWORD, 1)),
						new VillagerTradeOffer(new ItemStack(Material.EMERALD, 64), new ItemStack(Material.DIAMOND_LEGGINGS, 1)),
						new VillagerTradeOffer(new ItemStack(Material.EMERALD, 64), new ItemStack(Material.DIAMOND_CHESTPLATE, 1)),
						new VillagerTradeOffer(new ItemStack(Material.EMERALD, 32), new ItemStack(Material.DIAMOND_HELMET, 1)),					
						new VillagerTradeOffer(new ItemStack(Material.EMERALD, 32), new ItemStack(Material.DIAMOND_BOOTS, 1)),					
						new VillagerTradeOffer(new ItemStack(Material.EMERALD, 24), new ItemStack(Material.DIAMOND_PICKAXE, 1)),
						new VillagerTradeOffer(new ItemStack(Material.BONE, 64), new ItemStack(Material.EMERALD, 1)),
						new VillagerTradeOffer(new ItemStack(Material.ROTTEN_FLESH, 64), new ItemStack(Material.EMERALD, 1)),
						new VillagerTradeOffer(new ItemStack(Material.SPIDER_EYE, 64), new ItemStack(Material.EMERALD, 1)),
						new VillagerTradeOffer(new ItemStack(Material.STRING, 64), new ItemStack(Material.EMERALD, 1)),
						};
				
				VillagerTradingLib trader = new VillagerTradingLib(id++);
				try {
					trader.OpenTrade(evt.getPlayer(), offers);
				} catch (NullTradeOfferException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
