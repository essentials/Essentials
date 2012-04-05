package com.earth2me.essentials.anticheat.config;

import com.earth2me.essentials.anticheat.NoCheat;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.*;
import org.bukkit.configuration.file.FileConfiguration;


/**
 * Central location for everything that's described in the configuration file(s)
 *
 */
public class ConfigurationManager
{
	private FileHandler fileHandler;
	private ConfigurationCacheStore cache;
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
		FileConfiguration conf = plugin.getConfig();
		conf.options().copyDefaults(true);
		conf.options().copyHeader(true);
		plugin.saveConfig();
		NoCheatConfiguration root = new NoCheatConfiguration(conf);

		root.regenerateActionLists();

		// Create a corresponding Configuration Cache
		// put the global config on the config map
		cache = new ConfigurationCacheStore(root);

		plugin.setFileLogger(setupFileLogger(new File(rootConfigFolder, root.getString(ConfPaths.LOGGING_FILENAME))));
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
		return cache;
	}
}
