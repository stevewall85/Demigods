package com.censoredsoftware.demigods.greek.deity.fate;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Alliance;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.greek.ability.offense.Fireball;
import com.censoredsoftware.demigods.greek.ability.passive.AlwaysInvisible;
import com.censoredsoftware.demigods.greek.ability.passive.Carry;
import com.censoredsoftware.demigods.greek.ability.passive.NoDamage;
import com.censoredsoftware.demigods.greek.deity.GreekAlliance;
import com.censoredsoftware.demigods.greek.deity.GreekDeity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Atropos extends GreekDeity
{
	public final static String NAME = "Atropos", SHORT_DESCRIPTION = ChatColor.GRAY + "The one who cuts the string.";
	public final static Alliance ALLIANCE = GreekAlliance.FATE;
	public final static String PERMISSION = ALLIANCE.getPermission() + "." + NAME.toLowerCase();
	public final static int ACCURACY = 15, FAVOR_REGEN = 999, MAX_FAVOR = 20000, MAX_HEALTH = 2, FAVOR_BANK = 10000;
	public final static ChatColor COLOR = ChatColor.DARK_RED;
	public final static Map<Material, Integer> CLAIM_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 3));
	public final static Map<Material, Integer> FORSAKE_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.BEDROCK, 10));
	public final static List<String> LORE = Arrays.asList();
	public final static Set<Deity.Flag> FLAGS = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE, Deity.Flag.NO_BATTLE, Deity.Flag.NO_SHRINE);
	public final static List<Ability> ABILITIES = Lists.newArrayList(new NoDamage(NAME), new AlwaysInvisible(NAME), new Fireball(NAME), new Carry(NAME, false));

	// Mood Manager
	private static EnumMap<Deity.Mood, Deity.MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private Atropos()
	{
		super(NAME, PERMISSION, ALLIANCE, COLOR, CLAIM_ITEMS, FORSAKE_ITEMS, SHORT_DESCRIPTION, LORE, FLAGS, ABILITIES, ACCURACY, FAVOR_REGEN, MAX_FAVOR, MAX_HEALTH, FAVOR_BANK, moodPacks);
	}

	private static final Deity INST = new Atropos();

	public static Deity inst()
	{
		return INST;
	}
}
