package com.earth2me.essentials.anticheat.config;

import com.earth2me.essentials.anticheat.NoCheat;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.*;


/**
 * Central location for everything that's described in the configuration file(s)
 *
 */
public class ConfigurationManager
{
	private final static String configFileName = "config.yml";
	private final Map<String, ConfigurationCacheStore> worldnameToConfigCacheMap = new HashMap<String, ConfigurationCacheStore>();
	private FileHandler fileHandler;
	private final NoCheat plugin;


	private static class LogFileFormatter extends Formatter
	{
		private final SimpleDateFormat date;

		public LogFileFormatter()
		{
			date = new SimpleDateFormat("yy.MM.dd HH:mm:ss");
		}

		@Override
		public String format(LogRecord record)
		{
			StringBuilder builder = new StringBuilder();
			Throwable ex = record.getThrown();

			builder.append(date.format(record.getMillis()));
			builder.append(" [");
			builder.append(record.getLevel().getLocalizedName().toUpperCase());
			builder.append("] ");
			builder.append(record.getMessage());
			builder.append('\n');

			if (ex != null)
			{
				StringWriter writer = new StringWriter();
				ex.printStackTrace(new PrintWriter(writer));
				builder.append(writer);
			}

			return builder.toString();
		}
	}

	public ConfigurationManager(NoCheat plugin, File rootConfigFolder)
	{

		this.plugin = plugin;

		// Setup the real configuration
		initializeConfig(rootConfigFolder);

	}

	/**
	 * Read the configuration file and assign either standard values or whatever is declared in the file
	 *
	 * @param configurationFile
	 */
	private void initializeConfig(File rootConfigFolder)
	{

		// First try to obtain and parse the global config file
		NoCheatConfiguration root = new NoCheatConfiguration();
		root.setDefaults(new DefaultConfiguration());
		root.options().copyDefaults(true);
		root.options().copyHeader(true);

		File globalConfigFile = getGlobalConfigFile(rootConfigFolder);

		if (globalConfigFile.exists())
		{
			try
			{
				root.load(globalConfigFile);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			root.save(globalConfigFile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		root.regenerateActionLists();

		// Create a corresponding Configuration Cache
		// put the global config on the config map
		worldnameToConfigCacheMap.put(null, new ConfigurationCacheStore(root));

		plugin.setFileLogger(setupFileLogger(new File(rootConfigFolder, root.getString(ConfPaths.LOGGING_FILENAME))));

		// Try to find world-specific config files
		Map<String, File> worldFiles = getWorldSpecificConfigFiles(rootConfigFolder);

		for (Entry<String, File> worldEntry : worldFiles.entrySet())
		{

			File worldConfigFile = worldEntry.getValue();

			NoCheatConfiguration world = new NoCheatConfiguration();
			world.setDefaults(root);

			try
			{
				world.load(worldConfigFile);

				worldnameToConfigCacheMap.put(worldEntry.getKey(), new ConfigurationCacheStore(world));

				// write the config file back to disk immediately
				world.save(worldConfigFile);

			}
			catch (Exception e)
			{
				plugin.getLogger().warning("Couldn't load world-specific config for " + worldEntry.getKey());
				e.printStackTrace();
			}

			world.regenerateActionLists();
		}
	}

	private static File getGlobalConfigFile(File rootFolder)
	{

		File globalConfig = new File(rootFolder, configFileName);

		return globalConfig;
	}

	private static Map<String, File> getWorldSpecificConfigFiles(File rootFolder)
	{

		HashMap<String, File> files = new HashMap<String, File>();

		if (rootFolder.isDirectory())
		{
			for (File f : rootFolder.listFiles())
			{
				if (f.isFile())
				{
					String filename = f.getName();
					if (filename.matches(".+_" + configFileName + "$"))
					{
						// Get the first part = world name
						String worldname = filename.substring(0, filename.length() - (configFileName.length() + 1));
						files.put(worldname, f);
					}
				}
			}
		}
		return files;
	}

	private Logger setupFileLogger(File logfile)
	{

		Logger l = Logger.getAnonymousLogger();
		l.setLevel(Level.INFO);
		// Ignore parent's settings
		l.setUseParentHandlers(false);
		for (Handler h : l.getHandlers())
		{
			l.removeHandler(h);
		}

		if (fileHandler != null)
		{
			fileHandler.close();
			l.removeHandler(fileHandler);
			fileHandler = null;
		}

		try
		{
			try
			{
				logfile.getParentFile().mkdirs();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			fileHandler = new FileHandler(logfile.getCanonicalPath(), true);
			fileHandler.setLevel(Level.ALL);
			fileHandler.setFormatter(new LogFileFormatter());

			l.addHandler(fileHandler);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return l;
	}

	/**
	 * Reset the loggers and flush and close the fileHandlers to be able to use them next time without problems
	 */
	public void cleanup()
	{
		fileHandler.flush();
		fileHandler.close();
		Logger l = Logger.getLogger("NoCheat");
		l.removeHandler(fileHandler);
		fileHandler = null;
	}

	/**
	 * Get the cache of the specified world, or the default cache, if no cache exists for that world.
	 *
	 * @param worldname
	 * @return
	 */
	public ConfigurationCacheStore getConfigurationCacheForWorld(String worldname)
	{

		ConfigurationCacheStore cache = worldnameToConfigCacheMap.get(worldname);

		if (cache != null)
		{
			return cache;
		}
		else
		{
			// Enter a reference to the cache under the new name
			// to be faster in looking it up later
			cache = worldnameToConfigCacheMap.get(null);
			worldnameToConfigCacheMap.put(worldname, cache);

			return cache;
		}
	}
}
