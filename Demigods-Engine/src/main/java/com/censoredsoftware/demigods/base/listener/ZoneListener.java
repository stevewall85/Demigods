package com.censoredsoftware.demigods.base.listener;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.data.serializable.DPlayer;
import com.censoredsoftware.demigods.engine.event.DemigodsChatEvent;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.Sets;

public class ZoneListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDemigodsChat(DemigodsChatEvent event)
	{
		Set<Player> modified = Sets.newHashSet(event.getRecipients());
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

		// Leaving a disabled world
		if(Zones.isNoDemigodsWorld(event.getFrom()) && !Zones.isNoDemigodsWorld(player.getWorld()))
		{
			if(playerSave.getCurrent() != null)
			{
				playerSave.saveMortalInventory(player);
				playerSave.getCurrent().applyToPlayer(player);
			}
			else Demigods.BOARD.getTeam("Mortal").addPlayer(player);
			player.sendMessage(ChatColor.YELLOW + "Demigods is enabled in this world.");
		}
		// Entering a disabled world
		else if(!Zones.isNoDemigodsWorld(event.getFrom()) && Zones.isNoDemigodsWorld(player.getWorld()))
		{
			if(playerSave.getCurrent() != null) playerSave.setToMortal();
			Demigods.BOARD.getTeam("Mortal").removePlayer(player);
			player.sendMessage(ChatColor.GRAY + "Demigods is disabled in this world.");
		}
	}
}
