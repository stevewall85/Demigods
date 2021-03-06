package com.demigodsrpg.demigods.greek.deity.titan;

import com.demigodsrpg.demigods.engine.deity.Ability;
import com.demigodsrpg.demigods.engine.deity.Alliance;
import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.greek.ability.offense.Blaze;
import com.demigodsrpg.demigods.greek.ability.offense.Fireball;
import com.demigodsrpg.demigods.greek.ability.passive.NoFire;
import com.demigodsrpg.demigods.greek.ability.ultimate.Firestorm;
import com.demigodsrpg.demigods.greek.deity.GreekAlliance;
import com.demigodsrpg.demigods.greek.deity.GreekDeity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.permissions.PermissionDefault;

import java.util.*;

public class Perses extends GreekDeity
{
	public static final String NAME = "Perses", SHORT_DESCRIPTION = ChatColor.GRAY + "The titan of anger and destruction.";
	public static final Alliance ALLIANCE = GreekAlliance.TITAN;
	public static final String PERMISSION = ALLIANCE.getPermission() + "." + NAME.toLowerCase();
	public static final PermissionDefault PERMISSION_DEFAULT = PermissionDefault.NOT_OP;
	public static final int ACCURACY = 15, FAVOR_REGEN = 5, MAX_FAVOR = 20000, MAX_HEALTH = 30, FAVOR_BANK = 10000;
	public static final ChatColor COLOR = ChatColor.GOLD;
	public static final Map<Material, Integer> CLAIM_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.FLINT, 1));
	public static final Map<Material, Integer> FORSAKE_ITEMS = Maps.newHashMap(ImmutableMap.of(Material.FIREWORK, 4));
	public static final List<String> LORE = Arrays.asList();
	public static final Set<Deity.Flag> FLAGS = Sets.newHashSet(Deity.Flag.MAJOR_DEITY, Deity.Flag.PLAYABLE);
	public static final List<Ability> ABILITIES = Lists.newArrayList(new Fireball(NAME), new Blaze(NAME), new Firestorm(NAME), new NoFire(NAME));

	// Mood Manager
	private static EnumMap<Mood, MoodPack> moodPacks = Maps.newEnumMap(Deity.Mood.class);

	private Perses()
	{
		super(NAME, PERMISSION, PERMISSION_DEFAULT, ALLIANCE, COLOR, CLAIM_ITEMS, FORSAKE_ITEMS, SHORT_DESCRIPTION, LORE, FLAGS, ABILITIES, ACCURACY, FAVOR_REGEN, MAX_FAVOR, MAX_HEALTH, FAVOR_BANK, moodPacks);
	}

	private static final Deity INST = new Perses();

	public static Deity inst()
	{
		return INST;
	}
}
