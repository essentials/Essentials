package com.earth2me.essentials.perm;

import com.earth2me.essentials.api.IPermissions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class NoCommandCostPermissions
{
	private static Map<String, IPermissions> permissions = new HashMap<String, IPermissions>();

	public static IPermissions getPermission(final String command)
	{
		IPermissions perm = permissions.get(command);
		if (perm == null)
		{
			perm = new BasePermission("essentials.nocommandcost.", command.toLowerCase(Locale.ENGLISH));
			permissions.put(command, perm);
		}
		return perm;
	}
}
