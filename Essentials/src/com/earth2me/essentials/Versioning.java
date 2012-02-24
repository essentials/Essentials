package com.earth2me.essentials;

import static com.earth2me.essentials.I18nComponent._;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;


public final class Versioning
{
	public static final int BUKKIT_VERSION = 1952;
	
	private static final Logger logger = Logger.getLogger("Minecraft");
	private static final Pattern bukkitVersionPattern = Pattern.compile("git-Bukkit-([0-9]+).([0-9]+).([0-9]+)-R[0-9]+-(?:[0-9]+-g[0-9a-f]+-)?b([0-9]+)jnks.*");
	
	public boolean checkServerVersion(Server server, String pluginVersion)
	{
		final PluginManager pluginManager = server.getPluginManager();
		for (Plugin plugin : pluginManager.getPlugins())
		{
			if (plugin.getDescription().getName().startsWith("Essentials")
				&& !plugin.getDescription().getVersion().equals(pluginVersion))
			{
				logger.log(Level.WARNING, _("versionMismatch", plugin.getDescription().getName()));
			}
		}
		
		final Matcher versionMatch = bukkitVersionPattern.matcher(server.getVersion());
		if (versionMatch.matches())
		{
			final int versionNumber = Integer.parseInt(versionMatch.group(4));
			if (versionNumber < BUKKIT_VERSION)
			{
				logger.log(Level.SEVERE, _("notRecommendedBukkit"));
				logger.log(Level.SEVERE, _("requiredBukkit", Integer.toString(BUKKIT_VERSION)));
				return false;
			}
		}
		else
		{
			logger.log(Level.INFO, _("bukkitFormatChanged"));
			logger.log(Level.INFO, server.getVersion());
			logger.log(Level.INFO, server.getBukkitVersion());
		}
		
		return true;
	}
}
