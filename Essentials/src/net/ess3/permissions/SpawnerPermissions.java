package net.ess3.permissions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.ess3.api.IPermission;


public class SpawnerPermissions
{
	private static Map<String, IPermission> permissions = new HashMap<String, IPermission>();

	public static IPermission getPermission(final String mobName)
	{
		IPermission perm = permissions.get(mobName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.spawner.", mobName.toLowerCase(Locale.ENGLISH).replace("_", ""));
			permissions.put(mobName, perm);
		}
		return perm;
	}
}
