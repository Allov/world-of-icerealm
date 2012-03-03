package ca.qc.icerealm.bukkit.plugins.specialitems;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;


public class SpecialItemsPlugin extends JavaPlugin implements Listener {

	private final short IronChestplateAugmentedDurability = (short)(Material.IRON_CHESTPLATE.getMaxDurability() / 2);
	private final short IronLeggingsAugmentedDurability = (short)(Material.IRON_LEGGINGS.getMaxDurability() / 2);
	private final short IronBootsAugmentedDurability = (short)(Material.IRON_BOOTS.getMaxDurability() / 2);
	private final short IronHelmetAugmentedDurability = (short)(Material.IRON_HELMET.getMaxDurability() / 2);
	
	private Map<Player, Boolean> playerEating = new HashMap<Player, Boolean>();
	
	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);

		setGoldDurability();
		addDurabilityRecipe();
	}

	private void addDurabilityRecipe() {
		ItemStack chestStack = new ItemStack(Material.IRON_CHESTPLATE, 1);
		chestStack.setDurability((short)-IronChestplateAugmentedDurability);
		
		ShapedRecipe chestRecipe = new ShapedRecipe( chestStack );
        chestRecipe.shape("LLL","LIL","LLL");
        chestRecipe.setIngredient('L', Material.LEATHER);
        chestRecipe.setIngredient('I', Material.IRON_CHESTPLATE);
        getServer().addRecipe(chestRecipe);

        ItemStack leggingsStack = new ItemStack(Material.IRON_LEGGINGS, 1);
		chestStack.setDurability((short)-IronLeggingsAugmentedDurability);

		ShapedRecipe leggingsRecipe = new ShapedRecipe( leggingsStack );
		leggingsRecipe.shape("LLL","LIL","LLL");
		leggingsRecipe.setIngredient('L', Material.LEATHER);
		leggingsRecipe.setIngredient('I', Material.IRON_LEGGINGS);
        getServer().addRecipe(leggingsRecipe);

        ItemStack bootsStack = new ItemStack(Material.IRON_BOOTS, 1);
		chestStack.setDurability((short)-IronBootsAugmentedDurability);

		ShapedRecipe bootsRecipe = new ShapedRecipe( bootsStack );
		bootsRecipe.shape("LLL","LIL","LLL");
		bootsRecipe.setIngredient('L', Material.LEATHER);
		bootsRecipe.setIngredient('I', Material.IRON_BOOTS);
        getServer().addRecipe(bootsRecipe);

        ItemStack helmetStack = new ItemStack(Material.IRON_HELMET, 1);
		chestStack.setDurability((short)-IronHelmetAugmentedDurability);

		ShapedRecipe helmetRecipe = new ShapedRecipe( helmetStack );
		helmetRecipe.shape("LLL","LIL","LLL");
		helmetRecipe.setIngredient('L', Material.LEATHER);
		helmetRecipe.setIngredient('I', Material.IRON_HELMET);
        getServer().addRecipe(helmetRecipe);
	}

	private void setGoldDurability() {
		setDurability(Material.GOLD_CHESTPLATE, Material.IRON_CHESTPLATE.getMaxDurability());
		setDurability(Material.GOLD_LEGGINGS, Material.IRON_LEGGINGS.getMaxDurability());
		setDurability(Material.GOLD_BOOTS, Material.IRON_BOOTS.getMaxDurability());
		setDurability(Material.GOLD_HELMET, Material.IRON_HELMET.getMaxDurability());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRightClick(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		
		if (item != null && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			net.minecraft.server.Item nmsItem = net.minecraft.server.Item.byId[item.getType().getId()];
			
			int maxDurability = nmsItem.getMaxDurability();
			if (maxDurability > 0) {
				short currentDurability = item.getDurability();
				currentDurability = (short)(maxDurability - currentDurability);
				
				event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Item durability: " + 
											  ChatColor.DARK_GREEN + getChatColorFromDur(currentDurability, maxDurability) + currentDurability);
			}
		}		
	}
	
	private ChatColor getChatColorFromDur(short durability, int maxDurability) {
		double percentage = (double)durability / (double)maxDurability;
		
		if (percentage >= 0.8) {
			return ChatColor.GREEN;
		}
		
		if (percentage >= 0.5) {
			return ChatColor.YELLOW;
		}

		if (percentage >= 0.2) {
			return ChatColor.GOLD;
		}

		return ChatColor.RED;
	}

	void setDurability(Material mat, int newDurability)
	{
	    if(!mat.isBlock())
	    {
	        net.minecraft.server.Item nmsItem = net.minecraft.server.Item.byId[mat.getId()];
	        Field field;
			try {
				field = net.minecraft.server.Item.class.getDeclaredField("durability");
		        field.setAccessible(true);
		        field.setInt(nmsItem, newDurability);
			} catch (Exception e) {
				Logger.getLogger("Minecraft").info(e.toString());
			}
	    }
	}
}
