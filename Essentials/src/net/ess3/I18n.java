package net.ess3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.ess3.api.IEssentials;
import net.ess3.api.II18n;


public class I18n implements II18n
{
	private static I18n instance;
	private static final String MESSAGES = "messages";
	private final Locale defaultLocale = Locale.getDefault();
	private Locale currentLocale = defaultLocale;
	private ResourceBundle customBundle;
	private ResourceBundle localeBundle;
	private final Map<String, MessageFormat> messageFormatCache = new HashMap<String, MessageFormat>();
	private final IEssentials ess;

	public I18n(final IEssentials ess)
	{
		this.ess = ess;
		customBundle = ResourceBundle.getBundle(MESSAGES, defaultLocale, new FileResClassLoader(I18n.class.getClassLoader(), ess));
		localeBundle = ResourceBundle.getBundle(MESSAGES, defaultLocale);
	}

	public void onEnable()
	{
		instance = this;
	}

	public void onDisable()
	{
		instance = null;
	}

	@Override
	public Locale getCurrentLocale()
	{
		return currentLocale;
	}

	public String translate(final String string)
	{
		try
		{
			try
			{
				return customBundle.getString(string);
			}
			catch (MissingResourceException ex)
			{
				return localeBundle.getString(string);
			}
		}
		catch (MissingResourceException ex)
		{
			return string;
		}
	}

	public static String _(final String string)
	{
		if (instance == null)
		{
			return "";
		}
		return instance.translate(string);
	}

	public static String _(final String string, final Object... objects)
	{
		if (instance == null)
		{
			return "";
		}
		if (objects.length == 0)
		{
			return instance.translate(string);
		}
		else
		{
			return instance.format(string, objects);
		}
	}

	public String format(final String string, final Object... objects)
	{
		final String format = translate(string);
		MessageFormat messageFormat = messageFormatCache.get(format);
		if (messageFormat == null)
		{
			messageFormat = new MessageFormat(format.replace("'", "''"));
			messageFormatCache.put(format, messageFormat);
		}
		return messageFormat.format(objects);
	}
	private final Pattern partSplit = Pattern.compile("[_\\.]");

	public void updateLocale(final String loc)
	{
		if (loc == null || loc.isEmpty())
		{
			return;
		}
		final String[] parts = partSplit.split(loc);
		if (parts.length == 1)
		{
			currentLocale = new Locale(parts[0]);
		}
		if (parts.length == 2)
		{
			currentLocale = new Locale(parts[0], parts[1]);
		}
		if (parts.length == 3)
		{
			currentLocale = new Locale(parts[0], parts[1], parts[2]);
		}
		ResourceBundle.clearCache();
		ess.getLogger().log(Level.INFO, String.format("Using locale %s", currentLocale.toString()));
		customBundle = ResourceBundle.getBundle(MESSAGES, currentLocale, new FileResClassLoader(I18n.class.getClassLoader(), ess));
		localeBundle = ResourceBundle.getBundle(MESSAGES, currentLocale);
	}

	public static String capitalCase(final String input)
	{
		return input == null || input.length() == 0 ? input : input.toUpperCase(Locale.ENGLISH).charAt(0) + input.toLowerCase(Locale.ENGLISH).substring(1);
	}


	private static class FileResClassLoader extends ClassLoader
	{
		private final File dataFolder;

		public FileResClassLoader(final ClassLoader classLoader, final IEssentials ess)
		{
			super(classLoader);
			this.dataFolder = ess.getPlugin().getDataFolder();
		}

		@Override
		public URL getResource(final String string)
		{
			final File file = new File(dataFolder, string);
			if (file.exists())
			{
				try
				{
					return file.toURI().toURL();
				}
				catch (MalformedURLException ex)
				{
				}
			}
			return super.getResource(string);
		}

		@Override
		public InputStream getResourceAsStream(final String string)
		{
			final File file = new File(dataFolder, string);
			if (file.exists())
			{
				try
				{
					return new FileInputStream(file);
				}
				catch (FileNotFoundException ex)
				{
				}
			}
			return super.getResourceAsStream(string);
		}
	}
}
