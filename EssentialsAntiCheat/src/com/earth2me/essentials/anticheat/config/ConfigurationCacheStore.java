package com.earth2me.essentials.anticheat.config;

import com.earth2me.essentials.anticheat.ConfigItem;
import java.util.HashMap;
import java.util.Map;


/**
 * A class to keep all configurables of the plugin associated with a world
 *
 */
public class ConfigurationCacheStore
{
	public final LoggingConfig logging;
	private final Map<String, ConfigItem> configMap = new HashMap<String, ConfigItem>();
	private final NoCheatConfiguration data;

	/**
	 * Instantiate a config cache and populate it with the data of a Config tree (and its parent tree)
	 */
	public ConfigurationCacheStore(NoCheatConfiguration data)
	{

		logging = new LoggingConfig(data);

		this.data = data;
	}

	@SuppressWarnings("unchecked")
	public <T extends ConfigItem> T get(String id)
	{
		return (T)configMap.get(id);
	}

	public void set(String id, ConfigItem config)
	{

		configMap.put(id, config);
	}

	public NoCheatConfiguration getConfiguration()
	{
		return this.data;
	}
}
