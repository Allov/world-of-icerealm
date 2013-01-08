package ca.qc.icerealm.bukkit.plugins.simplemobs.data;

import java.io.Serializable;

import org.bukkit.entity.EntityType;

public class SimpleMob implements Serializable
{
	private static final long serialVersionUID = -1386404185914251878L;
	private int amount;
	private EntityType entityType;
	
	public int getAmount() 
	{
		return amount;
	}
	
	public void setAmount(int amount) 
	{
		this.amount = amount;
	}
	
	public EntityType getEntityType() 
	{
		return entityType;
	}
	
	public void setEntityType(EntityType entityType) 
	{
		this.entityType = entityType;
	}
}
