package net.ess3.permissions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.ess3.api.IPermission;


public class HelpPermissions
{
	private static Map<String, IPermission> permissions = new HashMap<String, IPermission>();

	public static IPermission getPermission(final String pluginName)
	{
		IPermission perm = permissions.get(pluginName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.help.", pluginName.toLowerCase(Locale.ENGLISH));
			permissions.put(pluginName, perm);
		}
		return perm;
	}
}
