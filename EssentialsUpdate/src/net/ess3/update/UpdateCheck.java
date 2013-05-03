package net.ess3.update;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;


public class UpdateCheck
{
	private CheckResult result = CheckResult.UNKNOWN;
	private Version currentVersion;
	private Version newVersion = null;
	private int bukkitResult = 0;
	private UpdateFile updateFile;
	private final static int CHECK_INTERVAL = 20 * 60 * 60 * 6;
	private final Plugin plugin;
	private boolean essentialsInstalled;
	private final Pattern bukkitVersionPattern = Pattern.compile("git-Bukkit-(?:(?:[0-9]+)\\.)+[0-9]+-R[\\.0-9]+-(?:[0-9]+-g[0-9a-f]+-)?b([0-9]+)jnks.*");

	public UpdateCheck(final Plugin plugin)
	{
		this.plugin = plugin;
		updateFile = new UpdateFile(plugin);
		checkForEssentials();
	}

	private void checkForEssentials()
	{
		final PluginManager pluginManager = plugin.getServer().getPluginManager();
		final Plugin essentials = pluginManager.getPlugin("Essentials-3");
		essentialsInstalled = essentials != null;
		if (essentialsInstalled)
		{
			currentVersion = new Version(essentials.getDescription().getVersion());
		}
		else
		{
			if (new File(plugin.getDataFolder().getParentFile(), "Essentials.jar").exists())
			{
				plugin.getLogger().severe("Essentials.jar found, but not recognized by Bukkit. Broken download?");
			}
		}
	}

	public void scheduleUpdateTask()
	{
		plugin.getServer().getScheduler().scheduleAsyncRepeatingTask( //should probably not be async
				plugin, new Runnable()
		{
			@Override
			public void run()
			{
				updateFile = new UpdateFile(plugin);
				checkForUpdates();
			}
		}, CHECK_INTERVAL, CHECK_INTERVAL);
	}

	public boolean isEssentialsInstalled()
	{
		return essentialsInstalled;
	}

	public CheckResult getResult()
	{
		return result;
	}

	public int getNewBukkitVersion()
	{
		return bukkitResult;
	}

	public VersionInfo getNewVersionInfo()
	{
		return updateFile.getVersions().get(newVersion);
	}


	public enum CheckResult
	{
		NEW_ESS, NEW_ESS_BUKKIT, NEW_BUKKIT, OK, UNKNOWN
	}

	public void checkForUpdates()
	{
		if (currentVersion == null)
		{
			return;
		}
		final Map<Version, VersionInfo> versions = updateFile.getVersions();
		final int bukkitVersion = getBukkitVersion();
		Version higher = null;
		Version found = null;
		Version lower = null;
		int bukkitHigher = 0;
		int bukkitLower = 0;
		for (Entry<Version, VersionInfo> entry : versions.entrySet())
		{
			final int minBukkit = entry.getValue().getMinBukkit();
			final int maxBukkit = entry.getValue().getMaxBukkit();
			if (minBukkit == 0 || maxBukkit == 0)
			{
				continue;
			}
			if (bukkitVersion <= maxBukkit)
			{
				if (bukkitVersion < minBukkit)
				{
					if (higher == null || higher.compareTo(entry.getKey()) < 0)
					{

						higher = entry.getKey();
						bukkitHigher = minBukkit;
					}
				}
				else
				{
					if (found == null || found.compareTo(entry.getKey()) < 0)
					{
						found = entry.getKey();
					}
				}
			}
			else
			{
				if (lower == null || lower.compareTo(entry.getKey()) < 0)
				{
					lower = entry.getKey();
					bukkitLower = minBukkit;
				}
			}
		}
		if (found != null)
		{
			if (found.compareTo(currentVersion) > 0)
			{
				result = CheckResult.NEW_ESS;
				newVersion = found;
			}
			else
			{
				result = CheckResult.OK;
			}
		}
		else if (higher != null)
		{
			if (higher.compareTo(currentVersion) > 0)
			{
				newVersion = higher;
				result = CheckResult.NEW_ESS_BUKKIT;
				bukkitResult = bukkitHigher;
			}
			else if (higher.compareTo(currentVersion) < 0)
			{
				result = CheckResult.UNKNOWN;
			}
			else
			{
				result = CheckResult.NEW_BUKKIT;
				bukkitResult = bukkitHigher;
			}
		}
		else if (lower != null)
		{
			if (lower.compareTo(currentVersion) > 0)
			{
				result = CheckResult.NEW_ESS_BUKKIT;
				newVersion = lower;
				bukkitResult = bukkitLower;
			}
			else if (lower.compareTo(currentVersion) < 0)
			{
				result = CheckResult.UNKNOWN;
			}
			else
			{
				result = CheckResult.NEW_BUKKIT;
				bukkitResult = bukkitLower;
			}
		}

	}

	private int getBukkitVersion()
	{
		final Matcher versionMatch = bukkitVersionPattern.matcher(plugin.getServer().getVersion());
		if (versionMatch.matches())
		{
			return Integer.parseInt(versionMatch.group(1));
		}
		throw new NumberFormatException("Bukkit Version changed!");
	}

	public Version getNewVersion()
	{
		return newVersion;
	}
}
