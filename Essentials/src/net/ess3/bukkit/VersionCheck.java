package net.ess3.bukkit;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static net.ess3.I18n._;
import org.bukkit.plugin.PluginManager;


public class VersionCheck
{
	public static final int BUKKIT_VERSION = 2015;
	private static final Pattern bukkitVersionPattern = Pattern.compile("git-Bukkit-(?:(?:[0-9]+)\\.)+[0-9]+-R[\\.0-9]+-(?:[0-9]+-g[0-9a-f]+-)?b([0-9]+)jnks.*");

	public static boolean checkVersion(final org.bukkit.plugin.Plugin plugin)
	{
		final PluginManager pm = plugin.getServer().getPluginManager();
		final String pluginVersion = plugin.getDescription().getVersion();
		for (org.bukkit.plugin.Plugin p : pm.getPlugins())
		{
			if (p.getDescription().getName().startsWith("Essentials")
				&& !p.getDescription().getVersion().equals(pluginVersion))
			{
				p.getLogger().log(Level.WARNING, _("versionMismatch", p.getDescription().getName()));
			}
		}
		final Matcher versionMatch = bukkitVersionPattern.matcher(plugin.getServer().getVersion());
		if (versionMatch.matches())
		{
			final int versionNumber = Integer.parseInt(versionMatch.group(1));
			if (versionNumber < BUKKIT_VERSION && versionNumber > 100)
			{
				plugin.getLogger().log(Level.SEVERE, _("notRecommendedBukkit"));
				plugin.getLogger().log(Level.SEVERE, _("requiredBukkit", Integer.toString(BUKKIT_VERSION)));
				return false;
			}
		}
		else
		{
			plugin.getLogger().log(Level.INFO, _("bukkitFormatChanged"));
			plugin.getLogger().log(Level.INFO, plugin.getServer().getVersion());
			plugin.getLogger().log(Level.INFO, plugin.getServer().getBukkitVersion());
		}
		return true;
	}
}
