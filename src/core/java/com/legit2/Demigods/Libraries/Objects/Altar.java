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

package com.legit2.Demigods.Libraries.Objects;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import com.legit2.Demigods.Demigods;

public class Altar implements Serializable
{
	private static final Demigods API = Demigods.INSTANCE;
	private static final long serialVersionUID = 1020598192563856384L;

	protected final int id;
	protected boolean active;
	protected final SerialLocation center;
	protected ArrayList<ProtectedBlock> blocks;

	public Altar(Location location)
	{
		this.id = API.object.generateInt(5);
		this.center = new SerialLocation(location);

		// Generate the Altar
		generate();

		save();
	}

	/*
	 * save() : Saves the Altar to a HashMap.
	 */
	private void save()
	{
		API.data.saveBlockData("altars", this.id, this);
	}

	/*
	 * remove() : Removes the Altar.
	 */
	public void remove()
	{
		API.data.removeBlockData("altars", this.id);

		for(ProtectedBlock block : blocks)
		{
			block.remove();
		}
	}

	/*
	 * getID() : Returns the ID for the Altar.
	 */
	public int getID()
	{
		return this.id;
	}

	/*
	 * getLocation() : Returns the location of the center of this Altar.
	 */
	public Location getLocation()
	{
		return this.center.unserialize();
	}

	/*
	 * locationMatches() : Returns true if the location matches a block in the Altar.
	 */
	public boolean locationMatches(Location location)
	{
		for(ProtectedBlock block : blocks)
		{
			if(block.getLocation().equals(location)) return true;
		}
		return false;
	}

	public void generate()
	{
		ArrayList<ProtectedBlock> blocks = new ArrayList<ProtectedBlock>();
		Location location = this.getLocation();

		// Remove the emerald block
		location.getBlock().setTypeId(0);

		// Split the location so we can build off of it
		double locX = location.getX();
		double locY = location.getY();
		double locZ = location.getZ();
		World locWorld = location.getWorld();

		// Create the enchantment table
		blocks.add(new ProtectedBlock(new Location(locWorld, locX, locY + 2, locZ), "altar", Material.ENCHANTMENT_TABLE));

		// Create magical table stand
		blocks.add(new ProtectedBlock(new Location(locWorld, locX, locY + 1, locZ), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 2, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 2, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 2, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 2, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 2, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 2, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 2, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 2, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX, locY + 6, locZ), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 1, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 1, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 1, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 1, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 1, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 1, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 3, locY, locZ + 3), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 3, locY, locZ - 3), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 3, locY, locZ - 3), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 3, locY, locZ + 3), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 2, locY + 3, locZ + 2), "altar", Material.getMaterial(44), (byte) 13));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 2, locY + 3, locZ - 2), "altar", Material.getMaterial(44), (byte) 13));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 2, locY + 3, locZ - 2), "altar", Material.getMaterial(44), (byte) 13));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 2, locY + 3, locZ + 2), "altar", Material.getMaterial(44), (byte) 13));

		// Left beam
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 1, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX, locY + 4, locZ - 2), "altar", Material.getMaterial(98), (byte) 3));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 1, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 1, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 1, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));

		// Right beam
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 1, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX, locY + 4, locZ + 2), "altar", Material.getMaterial(98), (byte) 3));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 1, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 1, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 1, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));

		// Top beam
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 2, locY + 4, locZ + 1), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 2, locY + 4, locZ), "altar", Material.getMaterial(98), (byte) 3));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 2, locY + 4, locZ - 1), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 2, locY + 5, locZ + 1), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 2, locY + 5, locZ), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX + 2, locY + 5, locZ - 1), "altar", Material.getMaterial(126), (byte) 1));

		// Bottom beam
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 2, locY + 4, locZ + 1), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 2, locY + 4, locZ), "altar", Material.getMaterial(98), (byte) 3));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 2, locY + 4, locZ - 1), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 2, locY + 5, locZ + 1), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 2, locY + 5, locZ), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(new Location(locWorld, locX - 2, locY + 5, locZ - 1), "altar", Material.getMaterial(126), (byte) 1));

		// Set locations to use for building
		Location topLeft = new Location(locWorld, locX + 2, locY + 1, locZ - 2);
		Location topRight = new Location(locWorld, locX + 2, locY + 1, locZ + 2);
		Location botLeft = new Location(locWorld, locX - 2, locY + 1, locZ - 2);
		Location botRight = new Location(locWorld, locX - 2, locY + 1, locZ + 2);

		// Top left of platform
		blocks.add(new ProtectedBlock(topLeft, "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(topLeft.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(topLeft.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(topLeft.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));

		// Top right of platform
		blocks.add(new ProtectedBlock(topRight, "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(topRight.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(topRight.subtract(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(topRight.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));

		// Bottom left of platform
		blocks.add(new ProtectedBlock(botLeft, "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(botLeft.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(botLeft.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(botLeft.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));

		// Bottom right of platform
		blocks.add(new ProtectedBlock(botRight, "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(botRight.subtract(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(botRight.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(botRight.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));

		// Create central structure of platform
		for(int i = 1; i < 3; i++)
			blocks.add(new ProtectedBlock(new Location(locWorld, locX, locY + 1, locZ + i), "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 3; i++)
			blocks.add(new ProtectedBlock(new Location(locWorld, locX, locY + 1, locZ - i), "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 3; i++)
			blocks.add(new ProtectedBlock(new Location(locWorld, locX - i, locY + 1, locZ), "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 3; i++)
			blocks.add(new ProtectedBlock(new Location(locWorld, locX + i, locY + 1, locZ), "altar", Material.getMaterial(44), (byte) 5));

		// Build steps on all sides.
		Location leftSteps = new Location(locWorld, locX + 2, locY, locZ - 4);
		Location rightSteps = new Location(locWorld, locX + 2, locY, locZ + 4);
		Location topSteps = new Location(locWorld, locX + 4, locY, locZ - 2);
		Location botSteps = new Location(locWorld, locX - 4, locY, locZ - 2);

		// Create left steps
		blocks.add(new ProtectedBlock(leftSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
			blocks.add(new ProtectedBlock(leftSteps.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(leftSteps.add(0, 0, 1), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
			blocks.add(new ProtectedBlock(leftSteps.add(1, 0, 0), "altar", Material.getMaterial(98)));

		// Create right steps
		blocks.add(new ProtectedBlock(rightSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
			blocks.add(new ProtectedBlock(rightSteps.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(rightSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
			blocks.add(new ProtectedBlock(rightSteps.add(1, 0, 0), "altar", Material.getMaterial(98)));

		// Create top steps
		blocks.add(new ProtectedBlock(topSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
			blocks.add(new ProtectedBlock(topSteps.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(topSteps.subtract(1, 0, 0), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
			blocks.add(new ProtectedBlock(topSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)));

		// Create bottom steps
		blocks.add(new ProtectedBlock(botSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
			blocks.add(new ProtectedBlock(botSteps.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new ProtectedBlock(botSteps.add(1, 0, 0), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
			blocks.add(new ProtectedBlock(botSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)));

		// Create left step towers
		for(int i = 0; i < 3; i++)
			blocks.add(new ProtectedBlock(leftSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(leftSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(leftSteps.subtract(4, 0, 0), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(leftSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
			blocks.add(new ProtectedBlock(leftSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));

		// Create right step towers
		for(int i = 0; i < 3; i++)
			blocks.add(new ProtectedBlock(rightSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(rightSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(rightSteps.subtract(4, 0, 0), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(rightSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
			blocks.add(new ProtectedBlock(rightSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));

		// Create top step towers
		for(int i = 0; i < 3; i++)
			blocks.add(new ProtectedBlock(topSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(topSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(topSteps.add(0, 0, 4), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(topSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
			blocks.add(new ProtectedBlock(topSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));

		// Create bottom step towers
		for(int i = 0; i < 3; i++)
			blocks.add(new ProtectedBlock(botSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(botSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new ProtectedBlock(botSteps.add(0, 0, 4), "altar", Material.getMaterial(98)));
		blocks.add(new ProtectedBlock(botSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
			blocks.add(new ProtectedBlock(botSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));

		this.blocks = blocks;
	}
}
