package ca.qc.icerealm.bukkit.plugins.common;

import org.bukkit.entity.*;

public class EntityUtilities 
{	
	public static final int Player = 0;
	public static final int Sheep = 91;
	public static final int Cow = 92;
	public static final int Pig = 90;
	public static final int Creeper = 50;
	public static final int PigZombie = 57;
	public static final int Skeleton = 51;
	public static final int Spider = 52;
	public static final int Squid = 94;
	public static final int Zombie = 54;
	public static final int Ghast = 56;
	public static final int Slime = 55;
	public static final int Giant = 53;
	public static final int Blaze = 61;
	public static final int CaveSpider = 59;
	public static final int Chicken = 93;
	public static final int Enderman = 58;
	public static final int MagmaCube = 62;
	public static final int MushroomCow = 96;
	public static final int Wolf = 95;
	public static final int Silverfish = 60;
	public static final int EnderDragon = 63;
	public static final int Snowman = 97;
	
	public static int getEntityId(Entity entity)
    {
		// Friendly
		if(entity instanceof Player) return Player;
        if(entity instanceof Sheep) return Sheep;
        if(entity instanceof Pig) return Pig;
        if(entity instanceof Cow) return Cow;
        if(entity instanceof MushroomCow) return MushroomCow;
        if(entity instanceof Chicken) return Chicken;
        if(entity instanceof Squid) return Squid;
        if(entity instanceof Snowman) return Snowman;

        // Neutral
        if(entity instanceof Wolf) return Wolf;
        if(entity instanceof Enderman) return Enderman;
        if(entity instanceof Giant) return Giant;
        if(entity instanceof PigZombie) return PigZombie;

        // Aggressive
        if(entity instanceof Creeper) return Creeper;
        if(entity instanceof Skeleton)return Skeleton;
        if(entity instanceof CaveSpider) return CaveSpider;
        if(entity instanceof Spider) return Spider;
        if(entity instanceof Zombie) return Zombie;
        if(entity instanceof Slime) return Slime;
        if(entity instanceof Ghast) return Ghast;
        if(entity instanceof Blaze) return Blaze;
        if(entity instanceof MagmaCube) return MagmaCube;
        if(entity instanceof Silverfish) return Silverfish;
        if(entity instanceof EnderDragon) return EnderDragon;
        
        return -1;
    }
	
	public static EntityType getEntityType(String s) {
		if (s.equalsIgnoreCase("sheep")) { return EntityType.SHEEP; }
		if (s.equalsIgnoreCase("Cow")) { return EntityType.COW; }
		if (s.equalsIgnoreCase("Pig")) { return EntityType.PIG; }
		if (s.equalsIgnoreCase("Creeper")) { return EntityType.CREEPER; }
		if (s.equalsIgnoreCase("PigZombie")) { return EntityType.PIG_ZOMBIE; }
		if (s.equalsIgnoreCase("Skeleton")) { return EntityType.SKELETON; }
		if (s.equalsIgnoreCase("Spider")) { return EntityType.SPIDER; }
		if (s.equalsIgnoreCase("Squid")) { return EntityType.SQUID; }
		if (s.equalsIgnoreCase("Zombie")) { return EntityType.ZOMBIE; }
		if (s.equalsIgnoreCase("Ghast")) { return EntityType.GHAST; }
		if (s.equalsIgnoreCase("Slime")) { return EntityType.SLIME; }
		if (s.equalsIgnoreCase("Giant")) { return EntityType.GIANT; }
		if (s.equalsIgnoreCase("Blaze")) { return EntityType.BLAZE; }
		if (s.equalsIgnoreCase("CaveSpider")) { return EntityType.CAVE_SPIDER; }
		if (s.equalsIgnoreCase("Chicken")) { return EntityType.CHICKEN; }
		if (s.equalsIgnoreCase("Enderman")) { return EntityType.ENDERMAN; }
		if (s.equalsIgnoreCase("MagmaCube")) { return EntityType.MAGMA_CUBE; }
		if (s.equalsIgnoreCase("MushroomCow")) { return EntityType.MUSHROOM_COW; }
		if (s.equalsIgnoreCase("Wolf")) { return EntityType.WOLF; }
		if (s.equalsIgnoreCase("Silverfish")) { return EntityType.SILVERFISH; }
		if (s.equalsIgnoreCase("EnderDragon")) { return EntityType.ENDER_DRAGON; }
		if (s.equalsIgnoreCase("Snowman")) { return EntityType.SNOWMAN; }
		return EntityType.VILLAGER;
	}
	
	
	public static CreatureType getEntityCreatureType(Entity entity)
	{
		// Friendly
        if(entity instanceof Sheep) return CreatureType.SHEEP;
        if(entity instanceof Cow) return CreatureType.COW;
        if(entity instanceof Pig) return CreatureType.PIG;
        if(entity instanceof MushroomCow) return CreatureType.MUSHROOM_COW;
        if(entity instanceof Chicken) return CreatureType.CHICKEN;
        if(entity instanceof Squid) return CreatureType.SQUID;
        if(entity instanceof Snowman) return CreatureType.SNOWMAN;
        
        // Neutral
        if(entity instanceof Wolf) return CreatureType.WOLF;
        if(entity instanceof Enderman) return CreatureType.ENDERMAN;
        if(entity instanceof Giant) return CreatureType.GIANT;
        if(entity instanceof PigZombie) return CreatureType.PIG_ZOMBIE;

        // Aggressive
        if(entity instanceof Creeper) return CreatureType.CREEPER;
        if(entity instanceof Skeleton)return CreatureType.SKELETON;
        if(entity instanceof CaveSpider) return CreatureType.CAVE_SPIDER;
        if(entity instanceof Spider) return CreatureType.SPIDER;
        if(entity instanceof Zombie) return CreatureType.ZOMBIE;
        if(entity instanceof Slime) return CreatureType.SLIME;
        if(entity instanceof Ghast) return CreatureType.GHAST;
        if(entity instanceof Blaze) return CreatureType.BLAZE;
        if(entity instanceof MagmaCube) return CreatureType.MAGMA_CUBE;
        if(entity instanceof Silverfish) return CreatureType.SILVERFISH;
        if(entity instanceof EnderDragon) return CreatureType.ENDER_DRAGON;

        return null;
	}


	public static CreatureType getCreatureType(String s) {
		if (s.equalsIgnoreCase("sheep")) { return CreatureType.SHEEP; }
		if (s.equalsIgnoreCase("Cow")) { return CreatureType.COW; }
		if (s.equalsIgnoreCase("Pig")) { return CreatureType.PIG; }
		if (s.equalsIgnoreCase("Creeper")) { return CreatureType.CREEPER; }
		if (s.equalsIgnoreCase("PigZombie")) { return CreatureType.PIG_ZOMBIE; }
		if (s.equalsIgnoreCase("Skeleton")) { return CreatureType.SKELETON; }
		if (s.equalsIgnoreCase("Spider")) { return CreatureType.SPIDER; }
		if (s.equalsIgnoreCase("Squid")) { return CreatureType.SQUID; }
		if (s.equalsIgnoreCase("Zombie")) { return CreatureType.ZOMBIE; }
		if (s.equalsIgnoreCase("Ghast")) { return CreatureType.GHAST; }
		if (s.equalsIgnoreCase("Slime")) { return CreatureType.SLIME; }
		if (s.equalsIgnoreCase("Giant")) { return CreatureType.GIANT; }
		if (s.equalsIgnoreCase("Blaze")) { return CreatureType.BLAZE; }
		if (s.equalsIgnoreCase("CaveSpider")) { return CreatureType.CAVE_SPIDER; }
		if (s.equalsIgnoreCase("Chicken")) { return CreatureType.CHICKEN; }
		if (s.equalsIgnoreCase("Enderman")) { return CreatureType.ENDERMAN; }
		if (s.equalsIgnoreCase("MagmaCube")) { return CreatureType.MAGMA_CUBE; }
		if (s.equalsIgnoreCase("MushroomCow")) { return CreatureType.MUSHROOM_COW; }
		if (s.equalsIgnoreCase("Wolf")) { return CreatureType.WOLF; }
		if (s.equalsIgnoreCase("Silverfish")) { return CreatureType.SILVERFISH; }
		if (s.equalsIgnoreCase("EnderDragon")) { return CreatureType.ENDER_DRAGON; }
		if (s.equalsIgnoreCase("Snowman")) { return CreatureType.SNOWMAN; }
		return CreatureType.VILLAGER;
	}
	
	public static String getEntityName(Entity entity)
    {
        if(entity instanceof Player) return "Player";

        // Friendly
        if(entity instanceof Sheep) return "Sheep";
        if(entity instanceof Cow) return "Cow";
        if(entity instanceof Pig) return "Pig";
        if(entity instanceof MushroomCow) return "Mushroom Cow";
        if(entity instanceof Chicken) return "Chicken";
        if(entity instanceof Squid) return "Squid";
        if(entity instanceof Snowman) return "Snowman";

        // Neutral
        if(entity instanceof Wolf) return "Wolf";
        if(entity instanceof Enderman) return "Enderman";
        if(entity instanceof Giant) return "Giant";
        if(entity instanceof PigZombie) return "Pig Zombie";

        // Aggressive
        if(entity instanceof Creeper) return "Creeper";
        if(entity instanceof Skeleton)return "Skeleton";
        if(entity instanceof CaveSpider) return "Cave Spider";
        if(entity instanceof Spider) return "Spider";
        if(entity instanceof Zombie) return "Zombie";
        if(entity instanceof Slime) return "Slime";
        if(entity instanceof Ghast) return "Ghast";
        if(entity instanceof Blaze) return "Blaze";
        if(entity instanceof MagmaCube) return "Magma Cube";
        
        if(entity instanceof Silverfish) return "Silverfish";
        if(entity instanceof EnderDragon) return "Ender Dragon";

        return null;
    }

	public static String getEntityName(int id)
    {
        if(id == Player) return "Player";

        // Friendly
        if(id == Sheep) return "Sheep";
        if(id == Cow) return "Cow";
        if(id == Pig) return "Pig";
        if(id == MushroomCow) return "Mushroom Cow";
        if(id == Chicken) return "Chicken";
        if(id == Squid) return "Squid";
        if(id == Snowman) return "Snowman";

        // Neutral
        if(id == Wolf) return "Wolf";
        if(id == Enderman) return "Enderman";
        if(id == Giant) return "Giant";
        if(id == PigZombie) return "Pig Zombie";

        // Aggressive
        if(id == Creeper) return "Creeper";
        if(id == Skeleton)return "Skeleton";
        if(id == Spider) return "Spider";
        if(id == Zombie) return "Zombie";
        if(id == Slime) return "Slime";
        if(id == Ghast) return "Ghast";
        if(id == Blaze) return "Blaze";
        if(id == MagmaCube) return "MagmaCube";
        if(id == CaveSpider) return "Cave Spider";
        if(id == Silverfish) return "Silverfish";
        if(id == EnderDragon) return "Ender Dragon";

        return null;
    }

	@Deprecated
	public static String getMaterialFriendName(String name) {
		return name.replace('_', ' ').toLowerCase();		
	}
}
