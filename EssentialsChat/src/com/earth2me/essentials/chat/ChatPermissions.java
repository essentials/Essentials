package com.earth2me.essentials.chat;

import com.earth2me.essentials.api.IPermissions;
import com.earth2me.essentials.perm.BasePermission;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChatPermissions {
	private static Map<String, IPermissions> permissions = new HashMap<String, IPermissions>();

	public static IPermissions getPermission(final String groupName)
	{
		IPermissions perm = permissions.get(groupName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.chat.",groupName.toLowerCase(Locale.ENGLISH));
			permissions.put(groupName, perm);
		}
		return perm;
	}
}
