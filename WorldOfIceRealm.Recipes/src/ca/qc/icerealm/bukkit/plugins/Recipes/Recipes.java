package ca.qc.icerealm.bukkit.plugins.recipes;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class Recipes extends JavaPlugin {

    public Server server;

    public void onEnable() {

        server = getServer();

        ShapedRecipe testOutput = new ShapedRecipe( new ItemStack( Material.GRAVEL, 1 ) );
        testOutput.shape("SSS","SSS","SSS");
        testOutput.setIngredient('S', Material.SEEDS);
        server.addRecipe(testOutput);

    }

    public void onDisable() {


    }

}
