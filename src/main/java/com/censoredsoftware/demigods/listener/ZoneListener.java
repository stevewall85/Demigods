package com.censoredsoftware.demigods.listener;

import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.util.Zones;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.Set;

public class ZoneListener implements Listener
{
	// TODO Special listener to prevent interactions between Demigods worlds and non-Demigods worlds.

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDemigodsChat(DemigodsChatEvent event)
	{
		Set<Player> modified = event.getRecipients();
		for(Player player : event.getRecipients())
			if(Zones.inNoDemigodsZone(player.getLocation())) modified.remove(player);
		if(modified.size() < 1) event.setCancelled(true);
		event.setRecipients(modified);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldSwitch(PlayerChangedWorldEvent event)
	{
		// Only continue if the player is a character
		Player player = event.getPlayer();
		DPlayer playerSave = DPlayer.Util.getPlayer(player);

		if(playerSave.getCurrent() == null) return;

		// Leaving a disabled world
		if(Zones.isNoDemigodsWorld(event.getFrom()) && !Zones.isNoDemigodsWorld(player.getWorld()))
		{
			playerSave.saveMortalInventory(player.getInventory());
			playerSave.getCurrent().applyToPlayer(player);
			player.sendMessage(ChatColor.YELLOW + "Demigods is enabled in this world.");
		}
		// Entering a disabled world
		else if(!Zones.isNoDemigodsWorld(event.getFrom()) && Zones.isNoDemigodsWorld(player.getWorld()))
		{
			playerSave.setToMortal();
			player.sendMessage(ChatColor.GRAY + "Demigods is disabled in this world.");
		}
	}
}