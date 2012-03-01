package com.earth2me.essentials;

import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;


public final class Versioning
{
	public static final int BUKKIT_VERSION = 1952;

	private static final Pattern bukkitVersionPattern = Pattern.compile("git-Bukkit-([0-9]+).([0-9]+).([0-9]+)-R[0-9]+-(?:[0-9]+-g[0-9a-f]+-)?b([0-9]+)jnks.*");

	public boolean checkServerVersion(final Plugin plugin)
	{
		final String pluginVersion = plugin.getDescription().getVersion();
		final Server server = plugin.getServer();
		final PluginManager pluginManager = server.getPluginManager();
		for (Plugin p : pluginManager.getPlugins())
		{
			if (p.getDescription().getName().startsWith("Essentials")
				&& !p.getDescription().getVersion().equals(pluginVersion))
			{
				plugin.getLogger().log(Level.WARNING, $("versionMismatch", plugin.getDescription().getName()));
			}
		}

		final Matcher versionMatch = bukkitVersionPattern.matcher(server.getVersion());
		if (versionMatch.matches())
		{
			final int versionNumber = Integer.parseInt(versionMatch.group(4));
			if (versionNumber < BUKKIT_VERSION)
			{
				plugin.getLogger().log(Level.SEVERE, $("notRecommendedBukkit"));
				plugin.getLogger().log(Level.SEVERE, $("requiredBukkit", Integer.toString(BUKKIT_VERSION)));
				return false;
			}
		}
		else
		{
			plugin.getLogger().log(Level.INFO, $("bukkitFormatChanged"));
			plugin.getLogger().log(Level.INFO, server.getVersion());
			plugin.getLogger().log(Level.INFO, server.getBukkitVersion());
		}

		return true;
	}
}
