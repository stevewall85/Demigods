package com.censoredsoftware.Demigods.PlayerCharacter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.API.PlayerAPI;
import com.censoredsoftware.Demigods.Tracked.TrackedItemStack;
import com.censoredsoftware.Modules.Data.DataStubModule;
import com.censoredsoftware.Modules.Data.IntegerDataModule;

// TODO Figure out how this file is going to save.

public class PlayerCharacterInventory implements DataStubModule
{
	private int charID;
	private int size;
	private TrackedItemStack helmet = new TrackedItemStack(new ItemStack(Material.AIR), null);
	private TrackedItemStack chestPlate = new TrackedItemStack(new ItemStack(Material.AIR), null);
	private TrackedItemStack leggings = new TrackedItemStack(new ItemStack(Material.AIR), null);
	private TrackedItemStack boots = new TrackedItemStack(new ItemStack(Material.AIR), null);
	private IntegerDataModule items;

	public PlayerCharacterInventory(int charID, int size, TrackedItemStack helmet, TrackedItemStack chestPlate, TrackedItemStack leggings, TrackedItemStack boots, IntegerDataModule items)
	{
		this.charID = charID;
		this.size = size;
		this.helmet = helmet;
		this.chestPlate = chestPlate;
		this.leggings = leggings;
		this.boots = boots;
		this.items = items;
	}

	public PlayerCharacterInventory(Inventory inventory)
	{
		items = new IntegerDataModule();

		if(inventory != null)
		{
			this.charID = (PlayerAPI.getCurrentChar((OfflinePlayer) inventory.getHolder())).getID();
			this.size = inventory.getSize();

			if(getOwner().isOnline())
			{
				Player player = getOwner().getPlayer();
				if(player.getInventory().getHelmet() != null) this.helmet = new TrackedItemStack(player.getInventory().getHelmet().clone(), null);
				if(player.getInventory().getChestplate() != null) this.chestPlate = new TrackedItemStack(player.getInventory().getChestplate().clone(), null);
				if(player.getInventory().getLeggings() != null) this.leggings = new TrackedItemStack(player.getInventory().getLeggings().clone(), null);
				if(player.getInventory().getBoots() != null) this.boots = new TrackedItemStack(player.getInventory().getBoots().clone(), null);
			}

			for(int i = 0; i < this.size; i++)
			{
				ItemStack item = inventory.getItem(i);
				if(item != null)
				{
					items.saveData(i, new TrackedItemStack(item, null));
				}
			}
		}
	}

	/*
	 * toInventory() : Converts back into a standard inventory.
	 */
	public Inventory toInventory(Plugin instance)
	{
		Inventory inv = instance.getServer().createInventory((InventoryHolder) getOwner(), this.size);

		for(int slot : items.listKeys())
		{
			try
			{
				ItemStack item = ((TrackedItemStack) items.getDataObject(slot)).toItemStack();
				inv.setItem(slot, item);
			}
			catch(Exception ignored)
			{}
		}

		return inv;
	}

	/*
	 * setToPlayer() : Sets the inventory to a player.
	 */
	public synchronized void setToPlayer(OfflinePlayer entity)
	{
		Player player;

		if(!entity.isOnline()) return;
		else
		{
			player = entity.getPlayer();
		}

		if(this.getHelmet() != null) player.getInventory().setHelmet(this.getHelmet().toItemStack());
		if(this.getChestPlate() != null) player.getInventory().setChestplate(this.getChestPlate().toItemStack());
		if(this.getLeggings() != null) player.getInventory().setLeggings(this.getLeggings().toItemStack());
		if(this.getBoots() != null) player.getInventory().setBoots(this.getBoots().toItemStack());

		for(Entry<Integer, ItemStack> slot : this.getItems().entrySet())
		{
			player.getInventory().setItem(slot.getKey(), slot.getValue());
		}
	}

	/*
	 * getOwner() : Returns the Player who owns this inventory.
	 */
	public OfflinePlayer getOwner()
	{
		return CharacterAPI.getChar(charID).getOwner();
	}

	/*
	 * getItems() : Returns the items.
	 */
	public HashMap<Integer, ItemStack> getItems()
	{
		HashMap<Integer, ItemStack> temp = new HashMap<Integer, ItemStack>();

		for(int slot : items.listKeys())
		{
			try
			{
				ItemStack item = ((TrackedItemStack) items.getDataObject(slot)).toItemStack();
				temp.put(slot, item);
			}
			catch(Exception ignored)
			{}
		}

		return temp;
	}

	/*
	 * getHelmet() : Returns the helmet.
	 */
	public TrackedItemStack getHelmet()
	{
		if(this.helmet != null) return this.helmet;
		else return null;
	}

	/*
	 * getChestPlate() : Returns the chestPlate.
	 */
	public TrackedItemStack getChestPlate()
	{
		if(this.chestPlate != null) return this.chestPlate;
		else return null;
	}

	/*
	 * getLeggings() : Returns the leggings.
	 */
	public TrackedItemStack getLeggings()
	{
		if(this.leggings != null) return this.leggings;
		else return null;
	}

	/*
	 * getBoots() : Returns the boots.
	 */
	public TrackedItemStack getBoots()
	{
		if(this.boots != null) return this.boots;
		else return null;
	}

	@Override
	public boolean containsKey(String key)
	{
		return false; // TODO
	}

	@Override
	public Object getData(String key)
	{
		return null; // TODO
	}

	@Override
	public void saveData(String key, Object data)
	{
		// TODO
	}

	@Override
	public void removeData(String key)
	{
		// TODO
	}

	@Override
	public int getID()
	{
		return charID;
	}

	@Override
	public Map getMap() // TODO Make this into an actual DataStubModule instead of it holding a generic data module.
	{
		return items.getMap();
	}

	@Override
	public void setMap(Map map) // TODO Make this into an actual DataStubModule instead of it holding a generic data module.
	{
		items.setMap(map);
	}
}
