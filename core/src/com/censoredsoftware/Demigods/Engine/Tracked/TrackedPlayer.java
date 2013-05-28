package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import redis.clients.johm.Attribute;
import redis.clients.johm.Id;
import redis.clients.johm.Indexed;
import redis.clients.johm.Model;

import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.API.PlayerAPI;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.google.common.collect.Sets;

@Model
public class TrackedPlayer
{
	@Id
	private Long id;
	@Attribute
	@Indexed
	private String player;
	@Attribute
	private long lastLoginTime;
	@Attribute
	private long current;
	@Attribute
	private long previous;

	void setPlayer(String player)
	{
		this.player = player;
		TrackedPlayer.save(this);
	}

	public static void save(TrackedPlayer trackedPlayer)
	{
		DemigodsData.jOhm.save(trackedPlayer);
	}

	public static TrackedPlayer load(Long id) // TODO This belongs somewhere else.
	{
		return DemigodsData.jOhm.get(TrackedPlayer.class, id);
	}

	public static Set<TrackedPlayer> loadAll()
	{
		try
		{
			return DemigodsData.jOhm.getAll(TrackedPlayer.class);
		}
		catch(Exception e)
		{
			return Sets.newHashSet();
		}
	}

	public static TrackedPlayer getTracked(OfflinePlayer player)
	{
		try
		{
			List<TrackedPlayer> tracking = DemigodsData.jOhm.find(TrackedPlayer.class, "player", player.getName());
			return tracking.get(0);
		}
		catch(Exception ignored)
		{}
		return TrackedModelFactory.createTrackedPlayer(player);
	}

	public OfflinePlayer getPlayer()
	{
		return Bukkit.getOfflinePlayer(this.player);
	}

	public void setLastLoginTime(Long time)
	{
		this.lastLoginTime = time;
		TrackedPlayer.save(this);
	}

	public Long getLastLoginTime()
	{
		return this.lastLoginTime;
	}

	public void switchCharacter(PlayerCharacter newChar)
	{
		Player player = getPlayer().getPlayer();

		if(!newChar.getPlayer().equals(getPlayer()))
		{
			player.sendMessage(ChatColor.RED + "You can't do that.");
			return;
		}

		// Update the current character
		PlayerCharacter currChar = getCurrent();
		if(currChar != null)
		{
			currChar.setHealth(player.getHealth());
			currChar.setHunger(player.getFoodLevel());
			currChar.setLevel(player.getLevel());
			currChar.setExperience(player.getExp());
			currChar.setLocation(player.getLocation());
			currChar.saveInventory();
		}

		// Update their inventory
		newChar.getInventory().setToPlayer(player);

		// Update health and experience
		player.setHealth(newChar.getHealth());
		player.setFoodLevel(newChar.getHunger());
		player.setExp(newChar.getExperience());
		player.setLevel(newChar.getLevel());

		// Teleport them
		player.teleport(newChar.getLocation());

		// Disable prayer, re-enabled movement, etc. just to be safe
		PlayerAPI.togglePraying(player, false);
		PlayerAPI.togglePlayerChat(player, true);
		PlayerAPI.togglePlayerMovement(player, true);

		// Update all active statuses and stuff
		currChar.setActive(false);
		this.previous = currChar.getId();
		newChar.setActive(true);
		this.current = newChar.getId();

		// Save instances
		TrackedPlayer.save(this);
		PlayerCharacter.save(currChar);
		PlayerCharacter.save(newChar);

	}

	public PlayerCharacter getCurrent()
	{
		return CharacterAPI.getChar(this.current);
	}

	public PlayerCharacter getPrevious()
	{
		return CharacterAPI.getChar(this.previous);
	}

	public List<PlayerCharacter> getCharacters()
	{
		return DemigodsData.jOhm.find(PlayerCharacter.class, "player", this.player);
	}
}
