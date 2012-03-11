package com.earth2me.essentials.anticheat.checks.fight;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.checks.Check;
import com.earth2me.essentials.anticheat.config.ConfigurationCacheStore;
import com.earth2me.essentials.anticheat.data.DataStore;


/**
 * Abstract base class for Fight checks, provides some convenience methods for access to data and config that's relevant
 * to this checktype
 */
public abstract class FightCheck extends Check
{
	private static final String id = "fight";
	public final String permission;

	public FightCheck(NoCheat plugin, String name, String permission)
	{
		super(plugin, id, name);
		this.permission = permission;
	}

	public abstract boolean check(NoCheatPlayer player, FightData data, FightConfig cc);

	public abstract boolean isEnabled(FightConfig cc);

	/**
	 * Get the "FightData" object that belongs to the player. Will ensure that such a object exists and if not, create
	 * one
	 *
	 * @param player
	 * @return
	 */
	public static FightData getData(NoCheatPlayer player)
	{
		DataStore base = player.getDataStore();
		FightData data = base.get(id);
		if (data == null)
		{
			data = new FightData();
			base.set(id, data);
		}
		return data;
	}

	/**
	 * Get the FightConfig object that belongs to the world that the player currently resides in.
	 *
	 * @param player
	 * @return
	 */
	public static FightConfig getConfig(NoCheatPlayer player)
	{
		return getConfig(player.getConfigurationStore());
	}

	public static FightConfig getConfig(ConfigurationCacheStore cache)
	{
		FightConfig config = cache.get(id);
		if (config == null)
		{
			config = new FightConfig(cache.getConfiguration());
			cache.set(id, config);
		}
		return config;
	}
}
