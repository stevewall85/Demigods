package com.censoredsoftware.demigods.item;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.demigods.helper.ColoredStringBuilder;
import com.censoredsoftware.demigods.util.Items;
import com.google.common.collect.Lists;

public enum Book
{
	/**
	 * Instructions
	 */
	FIRST_JOIN(Items.createBook(ChatColor.DARK_AQUA + "Read me!", "?????", new ArrayList<String>(2)
	{
		{
			add(new ColoredStringBuilder().black("Welcome, adventurer, to the world of ").bold().purple("Demigods RPG").removeBold().black("!").build());
			add("Allan please add details");
		}
	}, Lists.newArrayList("Important information."))),

	PRAYER_BOOK(Items.create(Material.BOOK, ChatColor.AQUA + "Book of Prayer", null, null));

	private final ItemStack value;

	private Book(ItemStack value)
	{
		this.value = value;
	}

	public ItemStack getBook()
	{
		return this.value;
	}
}
