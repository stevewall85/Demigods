package com.censoredsoftware.demigods.deity.titan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.demigods.Elements;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.ability.passive.Swim;
import com.censoredsoftware.demigods.deity.Deity;
import com.censoredsoftware.demigods.util.Strings;
import com.censoredsoftware.demigods.util.Unicodes;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Oceanus implements Deity
{
	private final static String name = "Oceanus", alliance = "Titan", permission = "demigods.titan.oceanus";
	private final static ChatColor color = ChatColor.DARK_AQUA;
	private final static Set<Material> claimItems = Sets.newHashSet(Material.RAW_FISH);
	private final static Map<Material, Integer> forsakeItems = Maps.newHashMap(ImmutableMap.of(Material.COOKED_FISH, 4, Material.FISHING_ROD, 1));
	private final static List<String> lore = new ArrayList<String>(5 + claimItems.size())
	{
		{
			add(" ");
			add(ChatColor.RED + " Demigods > " + ChatColor.RESET + color + name);
			add(ChatColor.RESET + "-----------------------------------------------------");
			add(" ");
			add(ChatColor.YELLOW + " Claim Items:");
			add(" ");
			for(Material item : claimItems)
				add(ChatColor.GRAY + "   " + Unicodes.rightwardArrow() + " " + ChatColor.WHITE + Strings.beautify(item.name()));
			add(" ");
			add(ChatColor.YELLOW + " Abilities:");
			add(" ");
		}
	};
	private final static Type type = Type.TIER1;
	private final static Set<Ability> abilities = Sets.newHashSet((Ability) new Swim(name, permission));

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Elements.ListedDeity getListedDeity()
	{
		return Elements.Deities.OCEANUS;
	}

	@Override
	public String getAlliance()
	{
		return alliance;
	}

	@Override
	public String getPermission()
	{
		return permission;
	}

	@Override
	public ChatColor getColor()
	{
		return color;
	}

	@Override
	public Set<Material> getClaimItems()
	{
		return claimItems;
	}

	@Override
	public Map<Material, Integer> getForsakeItems()
	{
		return forsakeItems;
	}

	@Override
	public List<String> getLore()
	{
		return lore;
	}

	@Override
	public Type getType()
	{
		return type;
	}

	@Override
	public Set<Ability> getAbilities()
	{
		return abilities;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
