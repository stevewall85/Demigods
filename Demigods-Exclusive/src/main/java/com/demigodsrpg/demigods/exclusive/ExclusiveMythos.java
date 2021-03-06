package com.demigodsrpg.demigods.exclusive;

import com.censoredsoftware.library.helper.CommandManager;
import com.censoredsoftware.library.trigger.Trigger;
import com.demigodsrpg.demigods.engine.deity.Alliance;
import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.engine.item.DivineItem;
import com.demigodsrpg.demigods.engine.mythos.Mythos;
import com.demigodsrpg.demigods.engine.mythos.MythosPlugin;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.demigodsrpg.demigods.exclusive.command.WorldCommands;
import com.demigodsrpg.demigods.exclusive.deity.Hestia;
import com.demigodsrpg.demigods.exclusive.listener.DistrictListener;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

public class ExclusiveMythos extends MythosPlugin
{
	private static ExclusiveMythos inst;

	public static ExclusiveMythos inst()
	{
		return inst;
	}

	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		inst = this;
		Exclusive.init();
	}

	/**
	 * The Bukkit disable method.
	 */
	@Override
	public void onDisable()
	{}

	@Override
	public String getTitle()
	{
		return "Exclusive";
	}

	@Override
	public String getTagline()
	{
		return "Exclusive enhancements for the official Demigods RPG server.";
	}

	@Override
	public String getAuthor()
	{
		return "_Alex & HmmmQuestionMark";
	}

	@Override
	public Boolean isPrimary()
	{
		return false;
	}

	@Override
	public Boolean allowSecondary()
	{
		return true;
	}

	@Override
	public String[] getIncompatible()
	{
		return new String[] {};
	}

	@Override
	public Boolean useBaseGame()
	{
		return true;
	}

	@Override
	public ImmutableCollection<DivineItem> getDivineItems()
	{
		return ImmutableSet.of();
	}

	@Override
	public DivineItem getDivineItem(String itemName)
	{
		return Mythos.Util.getDivineItem(this, itemName);
	}

	@Override
	public DivineItem getDivineItem(final ItemStack itemStack)
	{
		return Mythos.Util.getDivineItem(this, itemStack);
	}

	@Override
	public boolean itemHasFlag(ItemStack itemStack, DivineItem.Flag flag)
	{
		return Mythos.Util.itemHasFlag(this, itemStack, flag);
	}

	@Override
	public ImmutableCollection<Alliance> getAlliances()
	{
		return ImmutableSet.of();
	}

	@Override
	public Alliance getAlliance(final String allianceName)
	{
		return Mythos.Util.getAlliance(this, allianceName);
	}

	@Override
	public ImmutableCollection<Deity> getDeities()
	{
		return ImmutableSet.of(Hestia.inst());
	}

	@Override
	public Deity getDeity(final String deityName)
	{
		return Mythos.Util.getDeity(this, deityName);
	}

	@Override
	public ImmutableCollection<DemigodsStructureType> getStructures()
	{
		return ImmutableSet.of();
	}

	@Override
	public DemigodsStructureType getStructure(final String structureName)
	{
		return Mythos.Util.getStructure(this, structureName);
	}

	public Boolean levelSeperateSkills()
	{
		return true;
	}

	public ImmutableCollection<Listener> getListeners()
	{
		return ImmutableSet.of((Listener) new DistrictListener());
	}

	public ImmutableCollection<Permission> getPermissions()
	{
		return ImmutableSet.of();
	}

	@Override
	public ImmutableCollection<CommandManager> getCommands()
	{
		return ImmutableSet.of((CommandManager) WorldCommands.inst());
	}

	public ImmutableCollection<Trigger> getTriggers()
	{
		return ImmutableSet.of();
	}

	@Override
	public void setSecondary()
	{}

	@Override
	public void lock()
	{}
}
