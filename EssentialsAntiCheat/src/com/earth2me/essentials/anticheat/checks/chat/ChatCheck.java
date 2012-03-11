package com.earth2me.essentials.anticheat.checks.chat;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.checks.Check;
import com.earth2me.essentials.anticheat.config.ConfigurationCacheStore;
import com.earth2me.essentials.anticheat.data.DataStore;


/**
 * Abstract base class for Chat checks, provides some convenience methods for access to data and config that's relevant
 * to this checktype
 */
public abstract class ChatCheck extends Check
{
	private static final String id = "chat";

	public ChatCheck(NoCheat plugin, String name)
	{
		super(plugin, id, name);
	}

	@Override
	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{

		if (wildcard == ParameterName.TEXT)
		// Filter colors from the players message when logging
		{
			return getData(player).message.replaceAll("\302\247.", "").replaceAll("\247.", "");
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}

	/**
	 * Get the "ChatData" object that belongs to the player. Will ensure that such a object exists and if not, create
	 * one
	 *
	 * @param player
	 * @return
	 */
	public static ChatData getData(NoCheatPlayer player)
	{
		DataStore base = player.getDataStore();
		ChatData data = base.get(id);
		if (data == null)
		{
			data = new ChatData();
			base.set(id, data);
		}
		return data;
	}

	/**
	 * Get the ChatConfig object that belongs to the world that the player currently resides in.
	 *
	 * @param player
	 * @return
	 */
	public static ChatConfig getConfig(NoCheatPlayer player)
	{
		return getConfig(player.getConfigurationStore());
	}

	public static ChatConfig getConfig(ConfigurationCacheStore cache)
	{
		ChatConfig config = cache.get(id);
		if (config == null)
		{
			config = new ChatConfig(cache.getConfiguration());
			cache.set(id, config);
		}
		return config;
	}
}
