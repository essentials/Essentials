package com.earth2me.essentials.anticheat.debug;

import com.earth2me.essentials.anticheat.EventManager;
import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.config.ConfigurationCacheStore;
import java.util.List;
import org.bukkit.World;


/**
 * Prints the list of active checks per world on startup, if requested
 *
 */
public class ActiveCheckPrinter
{
	public static void printActiveChecks(NoCheat plugin, List<EventManager> eventManagers)
	{

		boolean introPrinted = false;

		// Print active checks for NoCheat, if needed.
		for (World world : plugin.getServer().getWorlds())
		{

			StringBuilder line = new StringBuilder("  ").append(world.getName()).append(": ");

			int length = line.length();

			ConfigurationCacheStore cc = plugin.getConfig(world);

			if (!cc.logging.showactivechecks)
			{
				continue;
			}

			for (EventManager em : eventManagers)
			{
				if (em.getActiveChecks(cc).isEmpty())
				{
					continue;
				}

				for (String active : em.getActiveChecks(cc))
				{
					line.append(active).append(' ');
				}

				if (!introPrinted)
				{
					plugin.getLogger().info("Active Checks: ");
					introPrinted = true;
				}

				plugin.getServer().getLogger().info(line.toString());

				line = new StringBuilder(length);

				for (int i = 0; i < length; i++)
				{
					line.append(' ');
				}
			}

		}
	}
}
