package com.earth2me.essentials;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Pattern;


public class Util
{
	private Util()
	{
	}
	private final static Pattern INVALIDFILECHARS = Pattern.compile("[^a-z0-9]");
	private final static Pattern INVALIDCHARS = Pattern.compile("[^\t\n\r\u0020-\u007E\u0085\u00A0-\uD7FF\uE000-\uFFFC]");

	//Used to clean file names before saving to disk
	public static String sanitizeFileName(final String name)
	{
		return safeString(name);
	}

	//Used to clean strings/names before saving as filenames/permissions
	public static String safeString(final String string)
	{
		return INVALIDFILECHARS.matcher(string.toLowerCase(Locale.ENGLISH)).replaceAll("_");
	}

	//Less restrictive string sanitizing, when not used as perm or filename
	public static String sanitizeString(final String string)
	{
		return INVALIDCHARS.matcher(string).replaceAll("");
	}
	private static DecimalFormat dFormat = new DecimalFormat("#0.00", DecimalFormatSymbols.getInstance(Locale.US));

	public static String formatAsCurrency(final double value)
	{
		String str = dFormat.format(value);
		if (str.endsWith(".00"))
		{
			str = str.substring(0, str.length() - 3);
		}
		return str;
	}

	public static double roundDouble(final double d)
	{
		return Math.round(d * 100.0) / 100.0;
	}

	public static boolean isInt(final String sInt)
	{
		try
		{
			Integer.parseInt(sInt);
		}
		catch (NumberFormatException e)
		{
			return false;
		}
		return true;
	}

	public static String joinList(Object... list)
	{
		return joinList(", ", list);
	}

	public static String joinList(String seperator, Object... list)
	{
		StringBuilder buf = new StringBuilder();
		for (Object each : list)
		{
			if (buf.length() > 0)
			{
				buf.append(seperator);
			}

			if (each instanceof Collection)
			{
				buf.append(joinList(seperator, ((Collection)each).toArray()));
			}
			else
			{
				try
				{
					buf.append(each.toString());
				}
				catch (Exception e)
				{
					buf.append(each.toString());
				}
			}
		}
		return buf.toString();
	}

	public static String lastCode(final String input)
	{
		int pos = input.lastIndexOf("§");
		if (pos == -1 || (pos + 1) == input.length())
		{
			return "";
		}
		return input.substring(pos, pos + 2);
	}
}