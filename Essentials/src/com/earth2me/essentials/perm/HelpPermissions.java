package com.earth2me.essentials.perm;

import com.earth2me.essentials.api.IPermissions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class HelpPermissions
{
	private static Map<String, IPermissions> permissions = new HashMap<String, IPermissions>();

	public static IPermissions getPermission(final String pluginName)
	{
		IPermissions perm = permissions.get(pluginName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.help.", pluginName.toLowerCase(Locale.ENGLISH));
			permissions.put(pluginName, perm);
		}
		return perm;
	}
}
