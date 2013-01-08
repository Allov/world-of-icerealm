package ca.qc.icerealm.bukkit.plugins.simplemobs;

public class UnknownEntityException extends Exception
{
	private String entityType;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownEntityException(String entityType)
	{
		this.entityType = entityType;
	}
	
	@Override
	public String getMessage() 
	{
		return entityType + " is not a valid EntityType";
	}

	public String getEntityType() 
	{
		return entityType;
	}

	public void setEntityType(String entityType) 
	{
		this.entityType = entityType;
	}
}
