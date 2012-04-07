package com.earth2me.essentials.permissions;

import com.earth2me.essentials.api.IPermission;
import com.earth2me.essentials.api.server.Permission;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class KitPermissions
{
	private static Map<String, IPermission> permissions = new HashMap<String, IPermission>();

	public static IPermission getPermission(final String kitName)
	{
		IPermission perm = permissions.get(kitName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.kit.", kitName.toLowerCase(Locale.ENGLISH))
			{
				@Override
				public Permission.Default getPermissionDefault()
				{
					return Permission.Default.TRUE;
				}
			};
			permissions.put(kitName, perm);
		}
		return perm;
	}
}
