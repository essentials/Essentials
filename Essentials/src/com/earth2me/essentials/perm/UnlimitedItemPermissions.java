package com.earth2me.essentials.perm;

import com.earth2me.essentials.api.IPermissions;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Material;

public class UnlimitedItemPermissions
{
	private static Map<Material, IPermissions> permissions = new EnumMap<Material, IPermissions>(Material.class);

	public static IPermissions getPermission(final Material mat)
	{
		IPermissions perm = permissions.get(mat);
		if (perm == null)
		{
			perm = new BasePermission("essentials.unlimited.item-", mat.toString().toLowerCase(Locale.ENGLISH).replace("_", ""));
			permissions.put(mat, perm);
		}
		return perm;
	}
}
