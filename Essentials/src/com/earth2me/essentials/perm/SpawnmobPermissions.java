package com.earth2me.essentials.perm;

import com.earth2me.essentials.api.IPermissions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpawnmobPermissions {
	private static Map<String, IPermissions> permissions = new HashMap<String, IPermissions>();

	public static IPermissions getPermission(final String mobName)
	{
		IPermissions perm = permissions.get(mobName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.spawnmob.", mobName.toLowerCase(Locale.ENGLISH).replace("_", ""));
			permissions.put(mobName, perm);
		}
		return perm;
	}
}
