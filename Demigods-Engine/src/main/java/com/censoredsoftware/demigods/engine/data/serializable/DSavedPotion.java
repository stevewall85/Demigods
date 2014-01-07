package com.censoredsoftware.demigods.engine.data.serializable;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;

import com.censoredsoftware.censoredlib.data.player.SavedPotion;
import com.censoredsoftware.demigods.engine.data.Data;

public class DSavedPotion extends SavedPotion
{
	public DSavedPotion(PotionEffect effect)
	{
		super(effect);
	}

	public DSavedPotion(UUID id, ConfigurationSection conf)
	{
		super(id, conf);
	}

	protected void save()
	{
		Data.SAVED_POTION.put(getId(), this);
	}
}
