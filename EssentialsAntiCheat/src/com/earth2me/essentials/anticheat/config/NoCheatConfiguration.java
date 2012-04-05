package com.earth2me.essentials.anticheat.config;

import com.earth2me.essentials.anticheat.actions.Action;
import com.earth2me.essentials.anticheat.actions.types.ActionList;
import org.bukkit.configuration.file.FileConfiguration;


public class NoCheatConfiguration
{
	private ActionFactory factory;
	private FileConfiguration conf;

	public NoCheatConfiguration(FileConfiguration conf)
	{
		this.conf = conf;
	}

	/**
	 * Do this after reading new data
	 */
	public void regenerateActionLists()
	{
		factory = new ActionFactory(conf.getConfigurationSection(ConfPaths.STRINGS).getValues(false));
	}

	/**
	 * A convenience method to get action lists from the config
	 *
	 * @param path
	 * @return
	 */
	public ActionList getActionList(String path, String permission)
	{
		String value = conf.getString(path);
		return factory.createActionList(value, permission);
	}

	/**
	 * Savely store ActionLists back into the yml file
	 *
	 * @param path
	 * @param list
	 */
	public void set(String path, ActionList list)
	{
		StringBuilder string = new StringBuilder();

		for (int treshold : list.getTresholds())
		{
			if (treshold > 0)
			{
				string.append(" vl>").append(treshold);
			}
			for (Action action : list.getActions(treshold))
			{
				string.append(" ").append(action);
			}
		}

		conf.set(path, string.toString().trim());
	}

	public int getInt(String path)
	{
		return conf.getInt(path);
	}

	public int getInt(String path, int def)
	{
		return conf.getInt(path, def);
	}

	public boolean getBoolean(String path)
	{
		return conf.getBoolean(path);
	}

	public String getString(String path)
	{
		return conf.getString(path);
	}
}
