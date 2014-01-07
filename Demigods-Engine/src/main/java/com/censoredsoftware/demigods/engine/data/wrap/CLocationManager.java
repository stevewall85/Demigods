package com.censoredsoftware.demigods.engine.data.wrap;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;

import com.censoredsoftware.censoredlib.data.location.CLocation;
import com.censoredsoftware.censoredlib.data.location.Region;
import com.censoredsoftware.demigods.engine.data.Data;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

public class CLocationManager extends CLocation.Util
{
	public static void save(CLocation location)
	{
		Data.LOCATION.put(location.getId(), location);
	}

	public static void delete(UUID id)
	{
		Data.LOCATION.remove(id);
	}

	public static CLocation load(UUID id)
	{
		return Data.LOCATION.get(id);
	}

	public static CLocation create(String world, double X, double Y, double Z, float yaw, float pitch)
	{
		CLocation trackedLocation = new CLocation();
		trackedLocation.generateId();
		trackedLocation.setWorld(world);
		trackedLocation.setX(X);
		trackedLocation.setY(Y);
		trackedLocation.setZ(Z);
		trackedLocation.setYaw(yaw);
		trackedLocation.setPitch(pitch);
		trackedLocation.setRegion(Region.Util.getRegion((int) X, (int) Z, world));
		save(trackedLocation);
		return trackedLocation;
	}

	public static CLocation create(Location location)
	{
		return create(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}

	public static Set<CLocation> loadAll()
	{
		return Sets.newHashSet(Data.LOCATION.values());
	}

	public static CLocation get(final Location location)
	{
		try
		{
			return Iterators.find(loadAll().iterator(), new Predicate<CLocation>()
			{
				@Override
				public boolean apply(CLocation tracked)
				{
					return location.getX() == tracked.getX() && location.getY() == tracked.getY() && location.getBlockZ() == tracked.getZ() && location.getWorld().getName().equals(tracked.getWorld());
				}
			});
		}
		catch(NoSuchElementException ignored)
		{
			// ignored
		}
		return create(location);
	}
}
