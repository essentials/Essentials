package com.earth2me.essentials.anticheat.config;

import com.earth2me.essentials.anticheat.actions.Action;
import com.earth2me.essentials.anticheat.actions.types.ActionList;
import java.lang.reflect.Field;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.DumperOptions;


public class NoCheatConfiguration extends YamlConfiguration
{
	private ActionFactory factory;

	@Override
	public String saveToString()
	{
		// Some reflection wizardry to avoid having a lot of
		// linebreaks in the yml file, and get a "footer" into the file
		try
		{
			Field op;
			op = YamlConfiguration.class.getDeclaredField("yamlOptions");
			op.setAccessible(true);
			DumperOptions options = (DumperOptions)op.get(this);
			options.setWidth(200);
		}
		catch (Exception e)
		{
		}

		String result = super.saveToString();

		return result;
	}

	/**
	 * Do this after reading new data
	 */
	public void regenerateActionLists()
	{
		factory = new ActionFactory(((MemorySection)this.get(ConfPaths.STRINGS)).getValues(false));
	}

	/**
	 * A convenience method to get action lists from the config
	 *
	 * @param path
	 * @return
	 */
	public ActionList getActionList(String path, String permission)
	{

		String value = this.getString(path);
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

		set(path, string.toString().trim());
	}
}
