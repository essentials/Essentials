package com.earth2me.essentials.anticheat.checks.blockplace;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.checks.Check;
import com.earth2me.essentials.anticheat.config.ConfigurationCacheStore;
import com.earth2me.essentials.anticheat.data.DataStore;
import com.earth2me.essentials.anticheat.data.SimpleLocation;
import java.util.Locale;


/**
 * Abstract base class for BlockPlace checks, provides some convenience methods for access to data and config that's
 * relevant to this checktype
 */
public abstract class BlockPlaceCheck extends Check
{
	private static final String id = "blockplace";

	public BlockPlaceCheck(NoCheat plugin, String name)
	{
		super(plugin, id, name);
	}

	@Override
	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{
		if (wildcard == ParameterName.PLACE_LOCATION)
		{
			SimpleLocation l = getData(player).blockPlaced;
			if (l.isSet())
			{
				return String.format(Locale.US, "%d %d %d", l.x, l.y, l.z);
			}
			else
			{
				return "null";
			}
		}
		else if (wildcard == ParameterName.PLACE_AGAINST)
		{
			SimpleLocation l = getData(player).blockPlacedAgainst;
			if (l.isSet())
			{
				return String.format(Locale.US, "%d %d %d", l.x, l.y, l.z);
			}
			else
			{
				return "null";
			}
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}

	/**
	 * Get the "BlockPlaceData" object that belongs to the player. Will ensure that such a object exists and if not,
	 * create one
	 *
	 * @param player
	 * @return
	 */
	public static BlockPlaceData getData(NoCheatPlayer player)
	{
		DataStore base = player.getDataStore();
		BlockPlaceData data = base.get(id);
		if (data == null)
		{
			data = new BlockPlaceData();
			base.set(id, data);
		}
		return data;
	}

	/**
	 * Get the BlockPlaceConfig object that belongs to the world that the player currently resides in.
	 *
	 * @param player
	 * @return
	 */
	public static BlockPlaceConfig getConfig(NoCheatPlayer player)
	{
		return getConfig(player.getConfigurationStore());
	}

	public static BlockPlaceConfig getConfig(ConfigurationCacheStore cache)
	{
		BlockPlaceConfig config = cache.get(id);
		if (config == null)
		{
			config = new BlockPlaceConfig(cache.getConfiguration());
			cache.set(id, config);
		}
		return config;
	}
}
