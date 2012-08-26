package net.ess3.bukkit;

import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;


public class PermissionFactory
{
	private static transient final Pattern DOT_PATTERN = Pattern.compile("\\.");

	public static Permission registerPermission(String permission, PermissionDefault defaultPerm)
	{
		final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
		final String[] parts = DOT_PATTERN.split(permission);
		final StringBuilder builder = new StringBuilder(permission.length());
		Permission parent = null;
		for (int i = 0; i < parts.length - 1; i++)
		{
			builder.append(parts[i]).append(".*");
			String permString = builder.toString();
			Permission perm = pluginManager.getPermission(permString);
			if (perm == null)
			{
				perm = new Permission(permString, PermissionDefault.FALSE);
				pluginManager.addPermission(perm);
				if (parent != null)
				{
					parent.getChildren().put(perm.getName(), Boolean.TRUE);
				}
				parent = perm;
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		Permission perm = pluginManager.getPermission(permission);
		if (perm == null)
		{
			perm = new Permission(permission, defaultPerm);
			pluginManager.addPermission(perm);
			if (parent != null)
			{
				parent.getChildren().put(perm.getName(), Boolean.TRUE);
			}
			parent = perm;
		}
		perm.recalculatePermissibles();
		return perm;
	}
}
