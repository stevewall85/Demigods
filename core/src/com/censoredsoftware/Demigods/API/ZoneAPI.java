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
	
	 5. The Demigods Team is defined as Alex Bennett and Alexander Chauncey
	    of http://www.censoredsoftware.com/.
	
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

package com.censoredsoftware.Demigods.API;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.Libraries.Objects.Altar;
import com.censoredsoftware.Demigods.Libraries.Objects.Shrine;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class ZoneAPI
{
	private static final Demigods API = Demigods.INSTANCE;
	public final int SHRINE_RADIUS = 8;
	public final int ALTAR_RADIUS = 16;

	/*
	 * --------------------------------------------
	 * No PVP Zones
	 * --------------------------------------------
	 * 
	 * zoneNoPVP() : Returns true if (Location)location is within a no PVP zone.
	 */
	public boolean zoneNoPVP(Location location)
	{
		if(API.config.getSettingBoolean("allow_skills_anywhere")) return false;
		if(API.config.getSettingBoolean("use_dynamic_pvp_zones"))
		{
			// Currently only supports WorldGuard for dynamic PVP zones
			if(Demigods.WORLDGUARD != null) return !canWorldGuardDynamicPVPAndNotAltar(location);
			else return zoneAltar(location) != null;
		}
		else
		{
			if(Demigods.WORLDGUARD != null && Demigods.FACTIONS != null) return !canWorldGuardAndFactionsPVP(location);
			else if(Demigods.WORLDGUARD != null) return !canWorldGuardFlagPVP(location);
			else return Demigods.FACTIONS != null && !canFactionsPVP(location);
		}
	}

	/*
	 * enterZoneNoPVP() : Returns true if entering a no PVP zone.
	 */
	public boolean enterZoneNoPVP(Location to, Location from)
	{
		return !zoneNoPVP(from) && zoneNoPVP(to);
	}

	/*
	 * exitZoneNoPVP() : Returns true if exiting a no PVP zone.
	 */
	public boolean exitZoneNoPVP(Location to, Location from)
	{
		return enterZoneNoPVP(from, to);
	}

	/*
	 * canWorldGuardAndFactionsPVP() : Returns true if PVP is allowed at (Location)location.
	 */
	public boolean canWorldGuardAndFactionsPVP(Location location)
	{
		return canFactionsPVP(location) && canWorldGuardFlagPVP(location) || canFactionsPVP(location) && canWorldGuardFlagPVP(location);
	}

	/*
	 * canWorldGuardDynamicPVPAndNotAltar() : Returns true if PVP is allowed at (Location)location, and it's not an Altar.
	 */
	public boolean canWorldGuardDynamicPVPAndNotAltar(Location location)
	{
		return (zoneAltar(location) == null) && canWorldGuardDynamicPVP(location) || canWorldGuardDynamicPVP(location) && zoneAltar(location) == null;
	}

	/*
	 * canWorldGuardDynamicPVP() : Returns true if PVP is allowed at (Location)location.
	 */
	public boolean canWorldGuardDynamicPVP(Location location)
	{
		ApplicableRegionSet set = Demigods.WORLDGUARD.getRegionManager(location.getWorld()).getApplicableRegions(location);
		for(ProtectedRegion region : set)
		{
			if(region.getId().toLowerCase().contains("nopvp")) return false;
		}
		return true;
	}

	/*
	 * canWorldGuardFlagPVP() : Returns true if PVP is allowed at (Location)location.
	 */
	public boolean canWorldGuardFlagPVP(Location location)
	{
		ApplicableRegionSet set = Demigods.WORLDGUARD.getRegionManager(location.getWorld()).getApplicableRegions(location);
		return !set.allows(DefaultFlag.PVP);
	}

	/*
	 * canFactionsPVP() : Returns true if PVP is allowed at (Location)location.
	 */
	public boolean canFactionsPVP(Location location)
	{
		Faction faction = Board.getFactionAt(new FLocation(location.getBlock()));
		return !(faction.isPeaceful() || faction.isSafeZone());
	}

	/*
	 * canTarget() : Checks if PVP is allowed in (Location)fallback for (Entity)player.
	 */
	public boolean canTarget(Entity player, Location fallback)
	{
		return !(player instanceof Player) || API.data.hasPlayerData((Player) player, "temp_was_PVP") && API.config.getSettingBoolean("use_dynamic_pvp_zones") || !zoneNoPVP(fallback);
	}

	public boolean canTarget(Entity player)
	{
		Location location = player.getLocation();
		return canTarget(player, location);
	}

	/*
	 * --------------------------------------------
	 * No Build Zones
	 * --------------------------------------------
	 * 
	 * zoneNoBuild() : Returns true if (Location)location is within a no Build zone for (Player)player.
	 */
	public boolean zoneNoBuild(Player player, Location location)
	{
		if(Demigods.WORLDGUARD != null && Demigods.FACTIONS != null) return !canWorldGuardAndFactionsBuild(player, location);
		else if(Demigods.WORLDGUARD != null) return !canWorldGuardBuild(player, location);
		else return Demigods.FACTIONS != null && !canFactionsBuild(player, location);
	}

	/*
	 * enterZoneNoBuild() : Returns true if entering a no Build zone.
	 */
	public boolean enterZoneNoBuild(Player player, Location to, Location from)
	{
		return !zoneNoBuild(player, from) && zoneNoBuild(player, to);
	}

	/*
	 * exitZoneNoBuilt() : Returns true if exiting a no Build zone.
	 */
	public boolean exitZoneNoBuild(Player player, Location to, Location from)
	{
		return enterZoneNoBuild(player, from, to);
	}

	/*
	 * canWorldGuardAndFactionsBuild() : Returns true if (Player)player is allowed to build at (Location)location.
	 */
	public boolean canWorldGuardAndFactionsBuild(Player player, Location location)
	{
		return canFactionsBuild(player, location) && canWorldGuardBuild(player, location) || canFactionsBuild(player, location) && canWorldGuardBuild(player, location);
	}

	/*
	 * canWorldGuardBuild() : Returns true if (Player)player can build at (Location)location.
	 */
	public boolean canWorldGuardBuild(Player player, Location location)
	{
		return Demigods.WORLDGUARD.canBuild(player, location);
	}

	/*
	 * canFactionsBuild() : Returns true if (Player)player can build at (Location)location.
	 */
	public boolean canFactionsBuild(Player player, Location location)
	{
		return Demigods.FACTIONS.isPlayerAllowedToBuildHere(player, location);
	}

	/*
	 * --------------------------------------------
	 * Block Zones
	 * --------------------------------------------
	 * 
	 * zoneShrine() : Returns a Shrine if (Location)location is within a Shrine's zone.
	 */
	public Shrine zoneShrine(Location location)
	{
		if(API.block.getAllShrines() == null) return null;

		for(Shrine shrine : API.block.getAllShrines())
		{
			if(location.getWorld() != shrine.getLocation().getWorld()) continue;
			if(location.distance(shrine.getLocation()) <= SHRINE_RADIUS) return shrine;
		}
		return null;
	}

	/*
	 * zoneShrineOwner() : Returns the owner of a Shrine from (Location)location.
	 */
	public int zoneShrineOwner(Location location)
	{
		if(API.block.getAllShrines() == null) return -1;

		for(Shrine shrine : API.block.getAllShrines())
		{
			if(shrine.getLocation().equals(location)) return shrine.getOwner().getID();
		}
		return -1;
	}

	/*
	 * enterZoneShrine() : Returns true if entering a Shrine zone.
	 */
	public boolean enterZoneShrine(Location to, Location from)
	{
		return (zoneShrine(from) == null) && zoneShrine(to) != null;
	}

	/*
	 * exitZoneShrine() : Returns true if exiting a Shrine zone.
	 */
	public boolean exitZoneShrine(Location to, Location from)
	{
		return enterZoneShrine(from, to);
	}

	/*
	 * zoneAltar() : Returns true if (Location)location is within an Altar's zone.
	 */
	public Altar zoneAltar(Location location)
	{
		if(API.block.getAllAltars() == null) return null;

		for(Altar altar : API.block.getAllAltars())
		{
			if(location.getWorld() != altar.getLocation().getWorld()) continue;
			if(location.distance(altar.getLocation()) <= ALTAR_RADIUS) return altar;
		}
		return null;
	}

	/*
	 * enterZoneAltar() : Returns true if entering an Altar zone.
	 */
	public boolean enterZoneAltar(Location to, Location from)
	{
		return (zoneAltar(from) == null) && zoneAltar(to) != null;
	}

	/*
	 * exitZoneAltar() : Returns true if exiting an Altar zone.
	 */
	public boolean exitZoneAltar(Location to, Location from)
	{
		return enterZoneAltar(from, to);
	}
}
