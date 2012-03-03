package com.earth2me.essentials.perm;

import com.earth2me.essentials.api.IPermissions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.permissions.PermissionDefault;


public class KitPermissions
{
	private static Map<String, IPermissions> permissions = new HashMap<String, IPermissions>();

	public static IPermissions getPermission(final String kitName)
	{
		IPermissions perm = permissions.get(kitName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.kit.", kitName.toLowerCase(Locale.ENGLISH))
			{
				@Override
				public PermissionDefault getPermissionDefault()
				{
					return PermissionDefault.TRUE;
				}
			};
			permissions.put(kitName, perm);
		}
		return perm;
	}
}
