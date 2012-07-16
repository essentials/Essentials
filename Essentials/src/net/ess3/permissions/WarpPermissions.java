package net.ess3.permissions;

import net.ess3.api.IPermission;
import net.ess3.api.server.Permission;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class WarpPermissions
{
	private static Map<String, IPermission> permissions = new HashMap<String, IPermission>();

	public static IPermission getPermission(final String warpName)
	{
		IPermission perm = permissions.get(warpName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.warp.", warpName.toLowerCase(Locale.ENGLISH))
			{
				@Override
				public Permission.Default getPermissionDefault()
				{
					return Permission.Default.TRUE;
				}
			};
			permissions.put(warpName, perm);
		}
		return perm;
	}
}