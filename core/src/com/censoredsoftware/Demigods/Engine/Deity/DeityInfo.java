package com.censoredsoftware.Demigods.Engine.Deity;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class DeityInfo
{
	private String name, alliance;
	private ChatColor color;
	private List<Material> claimItems;
	private List<String> lore;
	private Deity.Type type;

	public DeityInfo(String name, String alliance, ChatColor color, List<Material> claimItems, List<String> lore, Deity.Type type)
	{
		this.name = name;
		this.color = color;
		this.alliance = alliance;
		this.claimItems = claimItems;
		this.lore = lore;
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public String getAlliance()
	{
		return alliance;
	}

	public ChatColor getColor()
	{
		return color;
	}

	public List<Material> getClaimItems()
	{
		return claimItems;
	}

	public List<String> getLore()
	{
		return lore;
	}

	public Deity.Type getType()
	{
		return type;
	}
}
