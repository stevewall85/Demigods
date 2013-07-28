package com.censoredsoftware.demigods.engine.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.player.DPlayer;

public abstract class DCommand implements TabExecutor
{
	public abstract Set<String> getCommands();

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, final String[] args)
	{
		return new ArrayList<String>()
		{
			{
				for(Player online : Bukkit.getOnlinePlayers())
				{
					DPlayer wrapper = DPlayer.Util.getPlayer(online);
					if(wrapper.canUseCurrent() && wrapper.getCurrent() != null && wrapper.getCurrent().getName().toLowerCase().startsWith(args[0].toLowerCase())) add(wrapper.getCurrent().getName());
					else if(online.getName().toLowerCase().startsWith(args[0].toLowerCase())) add(online.getName());
				}
			}
		};
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return process(sender, command, args);
	}

	public abstract boolean process(CommandSender sender, Command command, final String[] args);

	public static class Util
	{
		public static void registerCommand(DCommand dCommand)
		{
			for(String command : dCommand.getCommands())
			{
				Demigods.plugin.getCommand(command).setExecutor(dCommand);
				Demigods.plugin.getCommand(command).setTabCompleter(dCommand);
			}
		}
	}
}