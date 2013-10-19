package com.earth2me.essentials.utils;

import java.util.*;
import java.util.regex.Pattern;


public class StringUtil
{
	private StringUtil()
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
}
