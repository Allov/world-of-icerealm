package ca.qc.icerealm.bukkit.plugins.trading;

import net.minecraft.server.v1_4_6.EntityVillager;
import net.minecraft.server.v1_4_6.MerchantRecipeList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftVillager;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
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
	public void onBlockPlaced(BlockPlaceEvent evt) {
		Block block = evt.getBlock();
		if (block.getType().equals(Material.EMERALD_BLOCK) || block.getType().equals(Material.DIAMOND_BLOCK) || block.getType().equals(Material.IRON_BLOCK)) {
			World world = block.getWorld();
			
			Block below = world.getBlockAt(new Location(world, block.getX(), block.getY() - 1, block.getZ()));
			Block below2 = world.getBlockAt(new Location(world, block.getX(), block.getY() - 2, block.getZ()));
			
			if (below != null && below.getType().equals(Material.OBSIDIAN) && below2.getType().equals(Material.OBSIDIAN)) {
				
				VillagerTradeOffer[] offers = null;
				
				Profession profession = Profession.FARMER;
				

				if (block.getType().equals(Material.DIAMOND_BLOCK)) { 
					offers = new VillagerTradeOffer[] {
							new VillagerTradeOffer(new ItemStack(Material.EMERALD, 24), new ItemStack(Material.DIAMOND_SWORD, 1)),
							new VillagerTradeOffer(new ItemStack(Material.EMERALD, 64), new ItemStack(Material.DIAMOND_LEGGINGS, 1)),
							new VillagerTradeOffer(new ItemStack(Material.EMERALD, 64), new ItemStack(Material.DIAMOND_CHESTPLATE, 1)),
							new VillagerTradeOffer(new ItemStack(Material.EMERALD, 32), new ItemStack(Material.DIAMOND_HELMET, 1)),					
							new VillagerTradeOffer(new ItemStack(Material.EMERALD, 32), new ItemStack(Material.DIAMOND_BOOTS, 1)),					
							new VillagerTradeOffer(new ItemStack(Material.EMERALD, 24), new ItemStack(Material.DIAMOND_PICKAXE, 1))
					};

					profession = Profession.BLACKSMITH;
				} else if (block.getType().equals(Material.EMERALD_BLOCK)) {
					offers = new VillagerTradeOffer[] {
							new VillagerTradeOffer(new ItemStack(Material.BONE, 64), new ItemStack(Material.EMERALD, 1)),
							new VillagerTradeOffer(new ItemStack(Material.ROTTEN_FLESH, 64), new ItemStack(Material.EMERALD, 1)),
							new VillagerTradeOffer(new ItemStack(Material.SPIDER_EYE, 64), new ItemStack(Material.EMERALD, 1)),
							new VillagerTradeOffer(new ItemStack(Material.STRING, 64), new ItemStack(Material.EMERALD, 1)),
					};
					profession = Profession.FARMER;
				}
				
				if (offers != null) {
					block.setType(Material.AIR);
					below.setType(Material.AIR);
					below2.setType(Material.AIR);

					Villager villager = world.spawn(block.getLocation(), Villager.class);
					villager.setAdult();
					villager.setBreed(false);
					villager.setProfession(profession);					

					EntityVillager entityVillager = ((CraftVillager)villager).getHandle();
					MerchantRecipeList recipes = entityVillager.getOffers(((CraftPlayer)evt.getPlayer()).getHandle());
					recipes.clear();
					
					for(VillagerTradeOffer offer : offers) {
						recipes.add(offer.getMerchantRecipie());
					}
				}
			}
		}
	}
}
