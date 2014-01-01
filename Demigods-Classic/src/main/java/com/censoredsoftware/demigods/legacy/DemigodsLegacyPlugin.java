package com.censoredsoftware.demigods.legacy;

import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.demigods.engine.mythos.*;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.legacy.listener.LegacyListener;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.ServicePriority;

public class DemigodsLegacyPlugin extends MythosPlugin
{
	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		getServer().getServicesManager().register(Mythos.class, this, this, ServicePriority.Highest);

		LegacyLoader.convertLegacyData();

		Messages.info("Successfully enabled.");
	}

	/**
	 * The Bukkit disable method.
	 */
	@Override
	public void onDisable()
	{
		getLogger().info("Successfully disabled.");
	}

	@Override
	public String getTitle()
	{
		return "Greek";
	}

	@Override
	public String getTagline()
	{
		return "Demigods 2 compatability layer for Demigods 3.";
	}

	@Override
	public String getAuthor()
	{
		return "_Alex, HmmmQuestionMark, and marinating";
	}

	@Override
	public Boolean isPrimary()
	{
		return null;
	}

	@Override
	public Boolean allowSecondary()
	{
		return null;
	}

	@Override
	public String[] getIncompatible()
	{
		return new String[0];
	}

	@Override
	public Boolean useBaseGame()
	{
		return null;
	}

	@Override
	public ImmutableCollection<DivineItem> getDivineItems()
	{
		return null;
	}

	@Override
	public DivineItem getDivineItem(String itemName)
	{
		return null;
	}

	@Override
	public DivineItem getDivineItem(ItemStack itemStack)
	{
		return null;
	}

	@Override
	public boolean itemHasFlag(ItemStack itemStack, DivineItem.Flag flag)
	{
		return false;
	}

	@Override
	public ImmutableCollection<Alliance> getAlliances()
	{
		return ImmutableSet.of();
	}

	@Override
	public Alliance getAlliance(String allianceName)
	{
		return null;
	}

	@Override
	public ImmutableCollection<Deity> getDeities()
	{
		return ImmutableSet.of();
	}

	@Override
	public Deity getDeity(String deityName)
	{
		return null;
	}

	@Override
	public ImmutableCollection<Structure> getStructures()
	{
		return ImmutableSet.of();
	}

	@Override
	public Structure getStructure(String structureName)
	{
		return null;
	}

	@Override
	public Boolean levelSeperateSkills()
	{
		return false;
	}

	// Default Greek Listeners
	@Override
	public ImmutableCollection<Listener> getListeners()
	{
		return ImmutableSet.copyOf(Collections2.transform(Sets.newHashSet(LegacyListener.values()), new Function<LegacyListener, Listener>()
		{
			@Override
			public Listener apply(LegacyListener listedListener)
			{
				return listedListener.getListener();
			}
		}));
	}

	// Default Greek Permissions
	@Override
	public ImmutableCollection<Permission> getPermissions()
	{
		return ImmutableSet.of();
	}

	// Default Greek Triggers
	@Override
	public ImmutableCollection<Trigger> getTriggers()
	{
		return ImmutableSet.of();
	}

	@Override
	public void setSecondary()
	{

	}

	@Override
	public void lock()
	{

	}
}