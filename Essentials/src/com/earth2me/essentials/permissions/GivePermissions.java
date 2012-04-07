package com.earth2me.essentials.permissions;

import com.earth2me.essentials.api.IPermission;
import com.earth2me.essentials.api.server.Material;
import com.earth2me.essentials.api.server.Permission;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class GivePermissions {
	private static Map<Material, IPermission> permissions = new HashMap<Material, IPermission>();

	public static IPermission getPermission(final Material mat)
	{
		IPermission perm = permissions.get(mat);
		if (perm == null)
		{
			perm = new BasePermission("essentials.give.item-", mat.toString().toLowerCase(Locale.ENGLISH).replace("_", ""))
			{
				@Override
				public Permission.Default getPermissionDefault()
				{
					return Permission.Default.TRUE;
				}
			};
			permissions.put(mat, perm);
		}
		return perm;
	}
}
