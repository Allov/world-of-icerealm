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
        if(entity instanceof Player) return Player;
        if(entity instanceof Sheep) return Sheep;
        if(entity instanceof Cow) return Cow;
        if(entity instanceof Pig) return Pig;
        if(entity instanceof Creeper) return Creeper;
        if(entity instanceof PigZombie) return PigZombie;
        if(entity instanceof Skeleton)return Skeleton;
        if(entity instanceof Spider) return Spider;
        if(entity instanceof Squid) return Squid;
        if(entity instanceof Zombie) return Zombie;
        if(entity instanceof Ghast) return Ghast;
        if(entity instanceof Slime) return Slime;
        if(entity instanceof Giant) return Giant;
        if(entity instanceof Blaze) return Blaze;
        if(entity instanceof CaveSpider) return CaveSpider;
        if(entity instanceof Chicken) return Chicken;
        if(entity instanceof Enderman) return Enderman;
        if(entity instanceof MagmaCube) return MagmaCube;
        if(entity instanceof MushroomCow) return MushroomCow;
        if(entity instanceof Wolf) return Wolf;
        if(entity instanceof Silverfish) return Silverfish;
        if(entity instanceof EnderDragon) return EnderDragon;
        if(entity instanceof Snowman) return Snowman;
        return -1;
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
}
