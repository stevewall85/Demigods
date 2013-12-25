package com.censoredsoftware.demigods.greek.item.weapon;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import com.censoredsoftware.censoredlib.util.Items;
import com.censoredsoftware.demigods.engine.item.DivineItem;
import com.censoredsoftware.demigods.engine.util.Zones;

public class BowOfThree
{
	public final static String name = "Bow of Tría";
	public final static String description = "Take your target out 3 times faster!";
	public final static DivineItem.Category category = DivineItem.Category.WEAPON;
	public final static ItemStack item = Items.create(Material.BOW, ChatColor.DARK_RED + "" + ChatColor.BOLD + name, new ArrayList<String>()
	{
		{
			add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + description);
		}
	}, null);
	public final static Recipe recipe = new ShapedRecipe(item)
	{
		{
			shape("ABB", " AB", "C A");
			setIngredient('A', Material.BOW);
			setIngredient('B', Material.ARROW);
			setIngredient('C', Material.DIAMOND);
		}
	};
	public final static Listener listener = new Listener()
	{
		@EventHandler(priority = EventPriority.HIGH)
		private void onEntityShootBow(EntityShootBowEvent event)
		{
			// Return for disabled world
			if(Zones.inNoDemigodsZone(event.getEntity().getLocation())) return;

			// If they right clicked a block with the item in hand, do stuff
			if(Items.areEqual(event.getBow(), item))
			{
				PlayerInventory inventory = null;
				Arrow startArrow = (Arrow) event.getProjectile();
				startArrow.setVelocity(startArrow.getVelocity().multiply(.8));

				if(event.getEntity() instanceof Player) inventory = ((Player) event.getEntity()).getInventory();

				for(int i = 1; i < 3; i++)
				{
					if(inventory != null)
					{
						if(!inventory.contains(Material.ARROW, 1)) break;
						inventory.remove(new ItemStack(Material.ARROW, 1));
					}

					Arrow spawnedArrow = (Arrow) event.getEntity().getWorld().spawnEntity(startArrow.getLocation(), EntityType.ARROW);
					spawnedArrow.setShooter(event.getEntity());
					spawnedArrow.setVelocity(startArrow.getVelocity().multiply(.9 / i));
				}
			}
		}
	};
}