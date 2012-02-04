package ca.qc.icerealm.bukkit.plugins.raredrops;

import java.util.ArrayList;

import org.bukkit.entity.Monster;

public class RareDropsOdds 
{
	private Monster monster = null;
	
	public Monster getMonster() 
	{
		return monster;
	}
	
	public void setMonster(Monster monster) 
	{
		this.monster = monster;
	}
	
	private ArrayList<RareDropsOddsItem> items = new ArrayList<RareDropsOddsItem>();
	
	public ArrayList<RareDropsOddsItem> getOddsItems()
	{
		return items;
	}
	
	public void setOddsItems(ArrayList<RareDropsOddsItem> oddsitems)
	{
		items = oddsitems;
	}
	
	public void addOddsItem(RareDropsOddsItem oddsitem)
	{
		items.add(oddsitem);
	}
}
