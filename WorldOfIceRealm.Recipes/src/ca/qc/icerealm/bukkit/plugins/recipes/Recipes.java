package ca.qc.icerealm.bukkit.plugins.recipes;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class Recipes extends JavaPlugin {

    public Server server;
    
    //private ArrayList<ShapedRecipe> customRecipes;
    
    public void onEnable() {

        server = getServer();

        // Nether Brick 
        ShapedRecipe netherBrickTL = new ShapedRecipe( new ItemStack( Material.NETHER_BRICK, 2 ) );
        netherBrickTL.shape("NN ","NN ","   ");
        netherBrickTL.setIngredient('N', Material.NETHERRACK);
        server.addRecipe(netherBrickTL);

        ShapedRecipe netherBrickTR = new ShapedRecipe( new ItemStack( Material.NETHER_BRICK, 2 ) );
        netherBrickTR.shape(" NN"," NN","   ");
        netherBrickTR.setIngredient('N', Material.NETHERRACK);
        server.addRecipe(netherBrickTR);

        ShapedRecipe netherBrickBL = new ShapedRecipe( new ItemStack( Material.NETHER_BRICK, 2 ) );
        netherBrickBL.shape("   ","NN ","NN ");
        netherBrickBL.setIngredient('N', Material.NETHERRACK);
        server.addRecipe(netherBrickBL);

        ShapedRecipe netherBrickBR = new ShapedRecipe( new ItemStack( Material.NETHER_BRICK, 2 ) );
        netherBrickBR.shape("   "," NN"," NN");
        netherBrickBR.setIngredient('N', Material.NETHERRACK);
        server.addRecipe(netherBrickBR);
        
        // Nether Brick Stairs
        ShapedRecipe netherBrickStairsL = new ShapedRecipe( new ItemStack( Material.NETHER_BRICK_STAIRS, 4 ) );
        netherBrickStairsL.shape("N  ","NN ","NNN");
        netherBrickStairsL.setIngredient('N', Material.NETHER_BRICK);
        server.addRecipe(netherBrickStairsL);

        ShapedRecipe netherBrickStairsR = new ShapedRecipe( new ItemStack( Material.NETHER_BRICK_STAIRS, 4 ) );
        netherBrickStairsR.shape("  N"," NN","NNN");
        netherBrickStairsR.setIngredient('N', Material.NETHER_BRICK);
        server.addRecipe(netherBrickStairsR);

        // Nether Brick Fences
        ShapedRecipe netherBrickFenceT = new ShapedRecipe( new ItemStack( Material.NETHER_FENCE, 4 ) );
        netherBrickFenceT.shape("NNN","NNN","   ");
        netherBrickFenceT.setIngredient('N', Material.NETHER_BRICK);
        server.addRecipe(netherBrickFenceT);

        ShapedRecipe netherBrickFenceB = new ShapedRecipe( new ItemStack( Material.NETHER_FENCE, 4 ) );
        netherBrickFenceB.shape("   ","NNN","NNN");
        netherBrickFenceB.setIngredient('N', Material.NETHER_BRICK);
        server.addRecipe(netherBrickFenceB);

        //
        // Mycel Block
        ShapelessRecipe mycelBlock = new ShapelessRecipe( new ItemStack( Material.MYCEL, 1 ) );
        mycelBlock.addIngredient(1, Material.DIRT);
        mycelBlock.addIngredient(1, Material.BROWN_MUSHROOM);
        mycelBlock.addIngredient(1, Material.RED_MUSHROOM);
        server.addRecipe(mycelBlock);

        //TODO: Add recipes to list of custom additions
        
    }

    public void onDisable() {
    	
    	//TODO: Loop through list of custom additions to delete them.
    	
    }

}
