package com.censoredsoftware.demigods;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.data.ThreadManager;
import com.censoredsoftware.demigods.player.Character;
import com.censoredsoftware.demigods.player.PlayerSave;
import com.censoredsoftware.demigods.util.Messages;

/**
 * Class for all plugins of demigods.
 */
public class DemigodsPlugin extends JavaPlugin
{
	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		// Load the game engine.
		Demigods.load();

		// Handle online characters
		for(Character character : Character.Util.loadAll())
			character.getMeta().cleanSkills();

		// Print success!
		Messages.info("Successfully enabled.");
	}

	/**
	 * The Bukkit disable method.
	 */
	@Override
	public void onDisable()
	{
		// Save all the data.
		DataManager.save();

		// Handle online characters
		for(Character character : com.censoredsoftware.demigods.player.Character.Util.getOnlineCharacters())
		{
			// Toggle prayer off and clear the session
			PlayerSave.Util.togglePrayingSilent(character.getOfflinePlayer().getPlayer(), false, false);
			PlayerSave.Util.clearPrayerSession(character.getOfflinePlayer().getPlayer());
		}

		// Cancel all threads, Event calls, and connections.
		ThreadManager.stopThreads();
		HandlerList.unregisterAll(this);

		Messages.info("Successfully disabled.");
	}
}
