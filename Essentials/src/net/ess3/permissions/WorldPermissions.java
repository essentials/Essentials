package net.ess3.permissions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.ess3.api.IPermission;

public class WorldPermissions
{
	private static Map<String, IPermission> permissions = new HashMap<String, IPermission>();

	public static IPermission getPermission(final String worldName)
	{
		IPermission perm = permissions.get(worldName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.world.", worldName.toLowerCase(Locale.ENGLISH));
			permissions.put(worldName, perm);
		}
		return perm;
	}
}
