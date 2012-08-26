package net.ess3.permissions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.ess3.api.IPermission;
import org.bukkit.permissions.PermissionDefault;


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