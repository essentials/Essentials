package com.earth2me.essentials.perm;

import com.earth2me.essentials.api.IPermissions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WorldPermissions
{
	private static Map<String, IPermissions> permissions = new HashMap<String, IPermissions>();

	public static IPermissions getPermission(final String worldName)
	{
		IPermissions perm = permissions.get(worldName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.world.", worldName.toLowerCase(Locale.ENGLISH));
			permissions.put(worldName, perm);
		}
		return perm;
	}
}
