package ca.qc.icerealm.bukkit.plugins.recipes;

import java.lang.Character;
import java.lang.Object;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.CraftingManager;

public class Recipes extends JavaPlugin {

    //Logger log = Logger.getLogger("Minecraft");
    CraftingManager manager = CraftingManager.getInstance();

    private ArrayList _addedRecipes = new ArrayList();

    public void onEnable() {

        // this.registerShapedRecipe(new ItemStack(Block.TNT, 1), new Object[] { "X#X", "#X#", "X#X", Character.valueOf('X'), Item.SULPHUR, Character.valueOf('#'), Block.SAND});
        // this.registerShapelessRecipe(new ItemStack(Item.EYE_OF_ENDER, 1), new Object[] { Item.ENDER_PEARL, Item.BLAZE_POWDER});
        
        ItemStack _output = new ItemStack(Material.CLAY, 1);

        manager.registerShapedRecipe(_output, new Object[]{"XXX", "XXX", "XXX", Character.valueOf('X'), Item.SEEDS });

    }

    public void onDisable() {


    }

}
