package com.earth2me.essentials.perm;

import com.earth2me.essentials.api.IPermissions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class EnchantPermissions
{
	private static Map<String, IPermissions> permissions = new HashMap<String, IPermissions>();

	public static IPermissions getPermission(final String enchantName)
	{
		IPermissions perm = permissions.get(enchantName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.enchant.",enchantName.toLowerCase(Locale.ENGLISH));
			permissions.put(enchantName, perm);
		}
		return perm;
	}
}