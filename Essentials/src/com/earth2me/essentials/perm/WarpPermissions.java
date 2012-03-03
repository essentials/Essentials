package com.earth2me.essentials.perm;

import com.earth2me.essentials.api.IPermissions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.permissions.PermissionDefault;


public class WarpPermissions
{
	private static Map<String, IPermissions> permissions = new HashMap<String, IPermissions>();

	public static IPermissions getPermission(final String warpName)
	{
		IPermissions perm = permissions.get(warpName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.warp.", warpName.toLowerCase(Locale.ENGLISH))
			{
				@Override
				public PermissionDefault getPermissionDefault()
				{
					return PermissionDefault.TRUE;
				}
			};
			permissions.put(warpName, perm);
		}
		return perm;
	}
}