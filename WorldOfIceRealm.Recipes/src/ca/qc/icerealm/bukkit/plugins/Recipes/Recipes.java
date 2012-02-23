package ca.qc.icerealm.bukkit.plugins.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.CraftingManager;

import java.lang.ArrayIndexOutOfBoundsException;

public class Recipes extends JavaPlugin {

    Logger log = Logger.getLogger("Minecraft");
    CraftingManager manager = CraftingManager.getInstance();

    private ArrayList _addedRecipes = new ArrayList();

    public void onEnable() {

        _initialRecipeCount = manager.b().size();

        // this.registerShapedRecipe(new ItemStack(Block.TNT, 1), new Object[] { "X#X", "#X#", "X#X", Character.valueOf('X'), Item.SULPHUR, Character.valueOf('#'), Block.SAND});
        // this.registerShapelessRecipe(new ItemStack(Item.EYE_OF_ENDER, 1), new Object[] { Item.ENDER_PEARL, Item.BLAZE_POWDER});
        
        ItemStack _output = new ItemStack(Material.CLAY, 1);

        manager.registerShapedRecipe(_output, new Object[] { "XXX", "XXX", "XXX", Character.valueOf('X'), Item.SEEDS });
        //manager.registerShapelessRecipe(reward, new Object[]{ ItemStack.SEEDS, 2 });

    }

    public void onDisable() {

        //manager.b().;

    }

}
