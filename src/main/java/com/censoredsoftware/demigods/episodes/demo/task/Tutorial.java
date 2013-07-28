package com.censoredsoftware.demigods.episodes.demo.task;

import java.util.ArrayList;

import com.censoredsoftware.demigods.engine.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.censoredsoftware.demigods.engine.element.Task;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.episodes.demo.item.Book;

public class Tutorial extends Task.List
{
	private static String name = "Tutorial", permission = "demigods.tutorial";
	private static java.util.List about = new ArrayList<String>(1)
	{
		{
			add("About."); // TODO This.
		}
	}, accepted = new ArrayList<String>(1)
	{
		{
			add("Accepted.");
		}
	}, complete = new ArrayList<String>(1)
	{
		{
			add("Complete.");
		}
	}, failed = new ArrayList<String>(1)
	{
		{
			add("Failed.");
		}
	};
	private static Type type = Type.TUTORIAL;
	private static java.util.List tasks = new ArrayList<Task>()
	{
		{
			add(new TutorialTask(name, permission, about, accepted, complete, failed, type));
		}
	};

	public Tutorial()
	{
		super(name, permission, about, accepted, complete, failed, type, tasks);
	}
}

class TutorialTask extends Task
{
	private static String name = "Welcome to demigods!";
	private static int order = 0;
	private static double reward = 50.0, penalty = 0;
	private static Listener listener = new Listener()
	{
		@EventHandler(priority = EventPriority.MONITOR)
		private void onPlayerJoin(PlayerJoinEvent event)
		{
			Player player = event.getPlayer();

			DCharacter character = DPlayer.Util.getPlayer(player).getCurrent();
			if(character == null || character.getMeta().isFinishedTask(name)) return;

			Messages.tagged(player, "Welcome to demigods, " + character.getDeity().getInfo().getColor() + character.getName() + ChatColor.RESET + "!");

			player.getInventory().setItem(player.getInventory().firstEmpty(), Book.FIRST_JOIN.getBook());

			character.getMeta().finishTask(name, true);
		}
	};

	public TutorialTask(String quest, String permission, java.util.List about, java.util.List accepted, java.util.List complete, java.util.List failed, List.Type type)
	{
		super(new Info(name, quest, permission, order, reward, penalty, about, accepted, complete, failed, type, Info.Subtype.QUEST), listener);
	}
}
