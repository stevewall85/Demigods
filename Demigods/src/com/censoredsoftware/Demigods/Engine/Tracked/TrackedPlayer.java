package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import redis.clients.johm.Attribute;
import redis.clients.johm.Id;
import redis.clients.johm.Indexed;
import redis.clients.johm.Model;

import com.censoredsoftware.Demigods.Engine.Demigods;
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

	public static TrackedPlayer load(Long id)
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

	public OfflinePlayer getOfflinePlayer()
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
		Player player = getOfflinePlayer().getPlayer();

		if(!newChar.getOfflinePlayer().equals(getOfflinePlayer()))
		{
			player.sendMessage(ChatColor.RED + "You can't do that.");
			return;
		}

		// Update the current character
		PlayerCharacter currChar = getCurrent();
		if(currChar != null)
		{
			// Set the values
			currChar.setHealth(player.getHealth());
			currChar.setHunger(player.getFoodLevel());
			currChar.setLevel(player.getLevel());
			currChar.setExperience(player.getExp());
			currChar.setLocation(player.getLocation());
			currChar.saveInventory();

			// Set to inactive and update previous
			currChar.setActive(false);
			this.previous = currChar.getId();

			// Save it
			PlayerCharacter.save(currChar);
		}

		// Update their inventory
		if(getChars(player).size() == 1) newChar.saveInventory();
		newChar.getInventory().setToPlayer(player);

		// Update health and experience
		player.setHealth(newChar.getHealth());
		player.setFoodLevel(newChar.getHunger());
		player.setExp(newChar.getExperience());
		player.setLevel(newChar.getLevel());

		// Disable prayer, re-enabled movement, etc. just to be safe
		togglePraying(player, false);
		togglePlayerChat(player, true);
		togglePlayerMovement(player, true);

		// Set new character to active
		newChar.setActive(true);
		this.current = newChar.getId();

		// Teleport them
		try
		{
			player.teleport(newChar.getLocation());
		}
		catch(Exception e)
		{
			Demigods.message.severe("There was a problem while teleporting a player to their character.");
		}

		// Save instances
		TrackedPlayer.save(this);
		PlayerCharacter.save(newChar);
	}

	public PlayerCharacter getCurrent()
	{
		return PlayerCharacter.getChar(this.current);
	}

	public PlayerCharacter getPrevious()
	{
		return PlayerCharacter.getChar(this.previous);
	}

	public List<PlayerCharacter> getCharacters()
	{
		return DemigodsData.jOhm.find(PlayerCharacter.class, "player", this.player);
	}

	/**
	 * Returns the current alliance for <code>player</code>.
	 * 
	 * @param player the player to check.
	 * @return String
	 */
	public static String getCurrentAlliance(OfflinePlayer player)
	{
		PlayerCharacter character = TrackedPlayer.getTracked(player).getCurrent();
		if(character == null || !character.isImmortal()) return "Mortal";
		return character.getAlliance();
	}

	/**
	 * Returns a List of all of <code>player</code>'s characters.
	 * 
	 * @param player the player to check.
	 * @return List the list of all character IDs.
	 */
	public static List<PlayerCharacter> getChars(OfflinePlayer player)
	{
		return TrackedPlayer.getTracked(player).getCharacters();
	}

	/**
	 * Returns true if the <code>player</code> is currently immortal.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean isImmortal(OfflinePlayer player)
	{
		PlayerCharacter character = TrackedPlayer.getTracked(player).getCurrent();
		return character != null && character.isImmortal();
	}

	/**
	 * Returns true if <code>player</code> has a character with the id <code>charID</code>.
	 * 
	 * @param player the player to check.
	 * @param charID the charID to check with.
	 * @return boolean
	 */
	public static boolean hasCharID(OfflinePlayer player, int charID)
	{
		return getChars(player) != null && getChars(player).contains(charID);
	}

	/**
	 * Returns true if <code>player</code> has a character with the name <code>charName</code>.
	 * 
	 * @param player the player to check.
	 * @param charName the charName to check with.
	 * @return boolean
	 */
	public static boolean hasCharName(OfflinePlayer player, String charName)
	{
		final List<PlayerCharacter> characters = getChars(player);

		for(PlayerCharacter character : characters)
		{
			if(character == null) continue;
			if(character.getName().equalsIgnoreCase(charName)) return true;
		}
		return false;
	}

	/**
	 * Returns true if the <code>player</code> is currently praying.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean isPraying(OfflinePlayer player)
	{
		try
		{
			return Boolean.parseBoolean(DemigodsData.getValueTemp(player.getName(), "temp_praying").toString());
		}
		catch(Exception ignored)
		{}
		return false;
	}

	/**
	 * Returns an ArrayList of all online admins.
	 * 
	 * @return ArrayList
	 */
	public static ArrayList<Player> getOnlineAdmins() // TODO Does this belong here?
	{
		ArrayList<Player> toReturn = new ArrayList<Player>();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(Demigods.permission.hasPermissionOrOP(player, "demigods.admin")) toReturn.add(player);
		}
		return toReturn;
	}

	/**
	 * Returns an ArrayList of all online players.
	 * 
	 * @return ArrayList
	 */
	public static Set<Player> getOnlinePlayers() // TODO Is this even needed?
	{
		Set<Player> toReturn = new HashSet<Player>();
		Collections.addAll(toReturn, Bukkit.getOnlinePlayers());
		return toReturn;
	}

	/**
	 * Returns an ArrayList of all offline players.
	 * 
	 * @return ArrayList
	 */
	public static Set<OfflinePlayer> getOfflinePlayers() // TODO Is this even needed?
	{
		Set<OfflinePlayer> toReturn = getAllPlayers();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			toReturn.remove(player);
		}
		return toReturn;
	}

	/**
	 * Returns an ArrayList of all players, offline and online.
	 * 
	 * @return ArrayList
	 */
	public static Set<OfflinePlayer> getAllPlayers()
	{
		Set<OfflinePlayer> toReturn = new HashSet<OfflinePlayer>();
		for(TrackedPlayer player : TrackedPlayer.loadAll())
		{
			toReturn.add(player.getOfflinePlayer());
		}
		return toReturn;
	}

	/**
	 * Changes prayer status for <code>player</code> to <code>option</code>.
	 * 
	 * @param player the player the manipulate.
	 * @param option the boolean to set to.
	 */
	public static void togglePraying(OfflinePlayer player, boolean option)
	{
		if(option)
		{
			togglePlayerChat(player, false);
			togglePlayerMovement(player, false);
			DemigodsData.saveTemp(player.getName(), "temp_praying", option);
		}
		else
		{
			togglePlayerChat(player, true);
			togglePlayerMovement(player, true);
			DemigodsData.removeTemp(player.getName(), "temp_praying");
		}
	}

	/**
	 * Enables or disables player movement for <code>player</code> based on <code>option</code>.
	 * 
	 * @param player the player to manipulate.
	 * @param option the boolean to set to.
	 */
	public static void togglePlayerMovement(OfflinePlayer player, boolean option)
	{
		if(DemigodsData.hasKeyTemp(player.getName(), "temp_player_hold") && option) DemigodsData.removeTemp(player.getName(), "temp_player_hold");
		else DemigodsData.saveTemp(player.getName(), "temp_player_hold", true);
	}

	/**
	 * Enables or disables player chat for <code>player</code> based on <code>option</code>.
	 * 
	 * @param player the player to manipulate.
	 * @param option the boolean to set to.
	 */
	public static void togglePlayerChat(OfflinePlayer player, boolean option)
	{
		if(DemigodsData.hasKeyTemp(player.getName(), "temp_no_chat") && option) DemigodsData.removeTemp(player.getName(), "temp_no_chat");
		else DemigodsData.saveTemp(player.getName(), "temp_no_chat", true);
	}
}