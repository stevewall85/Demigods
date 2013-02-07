/*
	Copyright (c) 2013 The Demigods Team
	
	Demigods License v1
	
	This plugin is provided "as is" and without any warranty.  Any express or
	implied warranties, including, but not limited to, the implied warranties
	of merchantability and fitness for a particular purpose are disclaimed.
	In no event shall the authors be liable to any party for any direct,
	indirect, incidental, special, exemplary, or consequential damages arising
	in any way out of the use or misuse of this plugin.
	
	Definitions
	
	 1. This Plugin is defined as all of the files within any archive
	    file or any group of files released in conjunction by the Demigods Team,
	    the Demigods Team, or a derived or modified work based on such files.
	
	 2. A Modification, or a Mod, is defined as this Plugin or a derivative of
	    it with one or more Modification applied to it, or as any program that
	    depends on this Plugin.
	
	 3. Distribution is defined as allowing one or more other people to in
	    any way download or receive a copy of this Plugin, a Modified
	    Plugin, or a derivative of this Plugin.
	
	 4. The Software is defined as an installed copy of this Plugin, a
	    Modified Plugin, or a derivative of this Plugin.
	
	 5. The Demigods Team is defined as Alexander Chauncey and Alex Bennett
	    of http://www.clashnia.com/.
	
	Agreement
	
	 1. Permission is hereby granted to use, copy, modify and/or
	    distribute this Plugin, provided that:
	
	    a. All copyright notices within source files and as generated by
	       the Software as output are retained, unchanged.
	
	    b. Any Distribution of this Plugin, whether as a Modified Plugin
	       or not, includes this license and is released under the terms
	       of this Agreement. This clause is not dependant upon any
	       measure of changes made to this Plugin.
	
	    c. This Plugin, Modified Plugins, and derivative works may not
	       be sold or released under any paid license without explicit 
	       permission from the Demigods Team. Copying fees for the 
	       transport of this Plugin, support fees for installation or
	       other services, and hosting fees for hosting the Software may,
	       however, be imposed.
	
	    d. Any Distribution of this Plugin, whether as a Modified
	       Plugin or not, requires express written consent from the
	       Demigods Team.
	
	 2. You may make Modifications to this Plugin or a derivative of it,
	    and distribute your Modifications in a form that is separate from
	    the Plugin. The following restrictions apply to this type of
	    Modification:
	
	    a. A Modification must not alter or remove any copyright notices
	       in the Software or Plugin, generated or otherwise.
	
	    b. When a Modification to the Plugin is released, a
	       non-exclusive royalty-free right is granted to the Demigods Team
	       to distribute the Modification in future versions of the
	       Plugin provided such versions remain available under the
	       terms of this Agreement in addition to any other license(s) of
	       the initial developer.
	
	    c. Any Distribution of a Modified Plugin or derivative requires
	       express written consent from the Demigods Team.
	
	 3. Permission is hereby also granted to distribute programs which
	    depend on this Plugin, provided that you do not distribute any
	    Modified Plugin without express written consent.
	
	 4. The Demigods Team reserves the right to change the terms of this
	    Agreement at any time, although those changes are not retroactive
	    to past releases, unless redefining the Demigods Team. Failure to
	    receive notification of a change does not make those changes invalid.
	    A current copy of this Agreement can be found included with the Plugin.
	
	 5. This Agreement will terminate automatically if you fail to comply
	    with the limitations described herein. Upon termination, you must
	    destroy all copies of this Plugin, the Software, and any
	    derivatives within 48 hours.
 */

package com.legit2.Demigods.Libraries;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.FireworkMeta;

import com.legit2.Demigods.Utilities.DCharUtil;
import com.legit2.Demigods.Utilities.DConfigUtil;
import com.legit2.Demigods.Utilities.DDataUtil;
import com.legit2.Demigods.Utilities.DDeityUtil;
import com.legit2.Demigods.Utilities.DObjUtil;
import com.legit2.Demigods.Utilities.DPlayerUtil;

public class DCharacter implements Serializable
{
	private static final long serialVersionUID = 8201132625259394712L;

	protected OfflinePlayer player;
	protected String charName, charDeity, charAlliance;
	protected int playerID, charID, charLevel, charHealth, charMaxHealth, charFavor, charMaxFavor, charDevotion, charAscensions;
	protected float charExp;
	protected Location charLoc;
	protected boolean charActive, charImmortal;
	protected Inventory charInv;
	
	public DCharacter(OfflinePlayer player, int charID, String charName, String charDeity)
	{
		// Create object using variables given and obtained
		this.playerID = DPlayerUtil.getPlayerID(player);
		this.player = player;
		this.charID = charID;
		this.charName = DObjUtil.capitalize(charName.toLowerCase());
		this.charDeity = DObjUtil.capitalize(charDeity.toLowerCase());
		this.charAlliance = DDeityUtil.getDeityAlliance(charDeity);
		if(player.isOnline()) this.charHealth = ((Player) player).getHealth();
		if(player.isOnline()) this.charMaxHealth = ((Player) player).getMaxHealth();
		if(player.isOnline()) this.charExp = ((Player) player).getExp();
		if(player.isOnline()) this.charLoc = ((Player) player).getLocation();
		this.charFavor = DConfigUtil.getSettingInt("default_favor");
		this.charMaxFavor = DConfigUtil.getSettingInt("default_max_favor");
		this.charDevotion = DConfigUtil.getSettingInt("default_devotion");
		this.charAscensions = DConfigUtil.getSettingInt("default_ascensions");
		this.charActive = true;
		this.charImmortal = true;
	
		// Save the character
		DDataUtil.addChar(charID);
		save();
	}
	
	public void save()
	{
		DDataUtil.saveCharData(charID, "char_owner", playerID);
		DDataUtil.saveCharData(charID, "char_object", this);
	}
	
	/* ----------------------------------------
	 * Favor-specific Methods
	 * ---------------------------------------- 
	 */
	public void setFavor(int amount)
	{
		this.charFavor = amount;
		save();
	}
	
	public void giveFavor(int amount)
	{
		if(this.charFavor + amount > charMaxFavor)
		{
			charFavor = getMaxFavor();
		}
		else this.charFavor += amount;
		save();
	}
	
	public void subtractFavor(int amount)
	{
		if(this.charFavor - amount < 0)
		{
			this.charFavor = 0;
		}
		else this.charFavor -= amount;
		save();
	}
	
	public void setMaxFavor(int amount)
	{
		this.charMaxFavor = amount;
		save();
	}
	
	public void addMaxFavor(int amount)
	{
		if((this.charMaxFavor + amount) > DConfigUtil.getSettingInt("global_max_favor"))
		{
			this.charMaxFavor = DConfigUtil.getSettingInt("global_max_favor");
		}
		else this.charMaxFavor += amount;
		save();
	}
	
	public ChatColor getFavorColor()
	{
		int favor = this.charFavor;
		int maxFavor = this.charMaxFavor;
		ChatColor color = ChatColor.RESET;
		
		// Set favor color dynamically
		if(favor < Math.ceil(0.33 * maxFavor)) color = ChatColor.RED;
		else if(favor < Math.ceil(0.66 * maxFavor) && favor > Math.ceil(0.33 * maxFavor)) color = ChatColor.YELLOW;
		if(favor > Math.ceil(0.66 * maxFavor)) color = ChatColor.GREEN;
		
		return color;
	}

	/* ----------------------------------------
	 * Devotion-specific Methods
	 * ---------------------------------------- 
	 */
	public int getDevotionGoal()
	{
		return (int) Math.ceil(500 * Math.pow(charAscensions + 1, 2.02));
	}
	
	public void setDevotion(int amount)
	{
		this.charFavor = amount;
		save();
	}
	
	public void giveDevotion(int amount)
	{
		int devotionBefore = this.charDevotion;
		int devotionGoal = getDevotionGoal();
		this.charDevotion += amount;
		int devotionAfter = this.charDevotion;
		
		if(devotionAfter > devotionBefore && devotionAfter > devotionGoal)
		{
			Player player = DCharUtil.getOwner(charID).getPlayer();
			
			// Player leveled up!
			charAscensions += 1;
			charDevotion = devotionAfter - devotionGoal;
			
			// Spawn a pretty firework!
			Firework firework = (Firework) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
			FireworkMeta fireworkmeta = firework.getFireworkMeta();
	        Type type = Type.BALL;       
	        FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.AQUA).withFade(Color.FUCHSIA).with(type).trail(true).build();
	        fireworkmeta.addEffect(effect);
	        fireworkmeta.setPower(1);
	        firework.setFireworkMeta(fireworkmeta);      
			
			// Let 'em know!
			player.sendMessage(ChatColor.GREEN + "You leveled up!" + ChatColor.GRAY + " (Devotion until next Ascension: " + ChatColor.YELLOW + (getDevotionGoal() - devotionAfter) + ChatColor.GRAY + ")");
		}
	}
	
	public void subtractDevotion(int amount)
	{
		if(this.charDevotion - amount < 0)
		{
			this.charDevotion = 0;
		}
		else this.charDevotion -= amount;
		save();
	}
	
	/* ----------------------------------------
	 * Ascension-specific Methods
	 * ---------------------------------------- 
	 */
	public void setAscensions(int amount)
	{
		this.charAscensions = amount;
		save();
	}
	
	public void giveAscensions(int amount)
	{
		this.charAscensions += amount;
		save();
	}
	
	public void subtractAscensions(int amount)
	{
		if(this.charAscensions - amount < 0)
		{
			this.charAscensions = 0;
		}
		else this.charAscensions -= amount;
		save();
	}
	
	/* ----------------------------------------
	 * Miscellaneous Methods
	 * ----------------------------------------
	 */
	public int getPower()
	{
		int power = (int) Math.ceil(Math.pow(getAscensions() * 250, 1.2));
		
		// TODO: Add power manipulation based on active character effects
		// if(DDataUtil.hasCharData(charID, "active_effects"));
		
		return power;
	}
	
	public void setHealth(int amount)
	{
		this.charHealth = amount;
		save();
	}
	
	public ChatColor getHealthColor()
	{
		int hp = this.charHealth;
		int maxHP = ((Player) player).getMaxHealth();
		ChatColor color = ChatColor.RESET;
		
		// Set favor color dynamically
		if(hp < Math.ceil(0.33 * maxHP)) color = ChatColor.RED;
		else if(hp < Math.ceil(0.66 * maxHP) && hp > Math.ceil(0.33 * maxHP)) color = ChatColor.YELLOW;
		if(hp > Math.ceil(0.66 * maxHP)) color = ChatColor.GREEN;
		
		return color;
	}
	
	public void setExp(float amount)
	{
		this.charExp = amount;
		save();
	}
	
	public void setLocation(Location location)
	{
		this.charLoc = location;
		save();
	}
	
	public void setAlliance(String alliance)
	{
		this.charAlliance = alliance.toLowerCase();
		save();
	}
	
	public boolean hasDeity(String deity)
	{
		if(this.charDeity.toLowerCase().equalsIgnoreCase(deity)) return true;
		else return false;
	}
	
	public void toggleActive(boolean option)
	{
		this.charActive = option;
		save();
	}
	
	public void toggleImmortal(boolean option)
	{
		this.charImmortal = option;
		save();
	}
	
	public boolean isEnabledAbility(String ability)
	{
		if(DDataUtil.hasCharData(this.charID, "boolean_" + ability.toLowerCase()))
		{
			return DObjUtil.toBoolean(DDataUtil.getCharData(this.charID, "boolean_" + ability.toLowerCase()));
		}
		return false;
	}
	
	public void toggleAbility(String ability, boolean option)
	{
		DDataUtil.saveCharData(this.charID,  "boolean_" + ability.toLowerCase(), option);
	}
	
	public boolean isBound(Material material)
	{
		if(getBindings() != null && getBindings().contains(material)) return true;
		else return false;
	}
	
	public Material getBind(String ability)
	{
		if(DDataUtil.getCharData(this.charID, ability + "_bind") != null)
		{
			Material material = (Material) DDataUtil.getCharData(this.charID, ability + "_bind");
			return material;
		}
		else return null;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Material> getBindings()
	{				
		if(DDataUtil.hasCharData(this.charID, "bindings"))
		{
			return (ArrayList<Material>) DDataUtil.getCharData(charID, "bindings");
		}
		else return new ArrayList<Material>();
	}
	
	public boolean setBound(String ability, Material material)
	{			
		if(DDataUtil.getCharData(this.charID, ability + "_bind") == null)
		{
			if(((Player) player).getItemInHand().getType() == Material.AIR)
			{
				((Player) player).sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
			}
			else
			{
				if(isBound(material))
				{
					((Player) player).sendMessage(ChatColor.YELLOW + "That item is already bound to a skill.");
					return false;
				}
				else if(material == Material.AIR)
				{
					((Player) player).sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
					return false;
				}
				else
				{			
					if(DDataUtil.hasCharData(this.charID, "bindings"))
					{
						ArrayList<Material> bindings = getBindings();
						if(!bindings.contains(material)) bindings.add(material);
						DDataUtil.saveCharData(this.charID, "bindings", bindings);
					}
					else
					{
						ArrayList<Material> bindings = new ArrayList<Material>();
						bindings.add(material);
						DDataUtil.saveCharData(this.charID, "bindings", bindings);
					}

					save();
					
					DDataUtil.saveCharData(this.charID, ability + "_bind", material);
					((Player) player).sendMessage(ChatColor.YELLOW + ability + " is now bound to: " + material.name().toUpperCase());
					return true;
				}
			}
		}
		else
		{
			removeBind(ability, ((Material) DDataUtil.getCharData(this.charID, ability + "_bind")));
			((Player) player).sendMessage(ChatColor.YELLOW + ability + "'s bind has been removed.");
		}
		return false;
	}

	public boolean removeBind(String ability, Material material)
	{
		ArrayList<Material> bindings = null;

		if(DDataUtil.hasCharData(this.charID, "bindings"))
		{
			bindings = getBindings();
			
			if(bindings != null && bindings.contains(material)) bindings.remove(material);
		}
		
		DDataUtil.saveCharData(this.charID, "bindings", bindings);
		DDataUtil.removeCharData(this.charID, ability + "_bind");
		
		save();
		return true;
	}

	public int getID()
	{
		return this.charID;
	}
	
	public Player getOwner() 
	{
		return Bukkit.getPlayer(player.getName());
	}
	
	public int getOwnerID() 
	{ 
		return this.playerID; 
	}
	
	public String getName() 
	{ 
		return this.charName; 
	}
	
	public String getDeity()
	{ 
		return this.charDeity; 
	}
	
	public String getAlliance() 
	{ 
		return this.charAlliance; 
	}
	public boolean isImmortal() 
	{ 
		return this.charImmortal; 
	}
	
	public boolean isActive() 
	{	
		return this.charActive; 
	}
	
	public Location getLastLocation() 
	{ 
		return this.charLoc; 
	}
	
	public int getHealth()
	{
		return this.charHealth;
	}
	
	public int getMaxHealth()
	{
		return this.charMaxHealth;
	}
	
	public float getExp()
	{
		return this.charExp;
	}
	
	public int getFavor() 
	{
		return this.charFavor; 
	}
	
	public int getMaxFavor() 
	{ 
		return this.charMaxFavor;	
	}
	
	public int getDevotion() 
	{ 
		return this.charDevotion;	
	}
	
	public int getAscensions() 
	{ 
		return this.charAscensions;	
	}
}