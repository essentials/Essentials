package com.earth2me.essentials.bukkit;

import com.earth2me.essentials.api.server.Permission;
import java.util.regex.Pattern;
import lombok.Delegate;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;


public class BukkitPermission extends Permission
{
	public static class BukkitPermissionFactory implements PermissionFactory
	{
		private static transient final Pattern DOT_PATTERN = Pattern.compile("\\.");

		@Override
		public Permission create(String permission, Permission.Default defaultPerm)
		{
			final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
			final String[] parts = DOT_PATTERN.split(permission);
			final StringBuilder builder = new StringBuilder(permission.length());
			org.bukkit.permissions.Permission parent = null;
			for (int i = 0; i < parts.length - 1; i++)
			{
				builder.append(parts[i]).append(".*");
				String permString = builder.toString();
				org.bukkit.permissions.Permission perm = pluginManager.getPermission(permString);
				if (perm == null)
				{
					perm = new org.bukkit.permissions.Permission(permString, PermissionDefault.FALSE);
					pluginManager.addPermission(perm);
					if (parent != null)
					{
						parent.getChildren().put(perm.getName(), Boolean.TRUE);
					}
					parent = perm;
				}
				builder.deleteCharAt(builder.length() - 1);
			}
			org.bukkit.permissions.Permission perm = pluginManager.getPermission(permission);
			if (perm == null)
			{
				perm = new org.bukkit.permissions.Permission(permission, getBukkitDefaultPermission(defaultPerm));
				pluginManager.addPermission(perm);
				if (parent != null)
				{
					parent.getChildren().put(perm.getName(), Boolean.TRUE);
				}
				parent = perm;
			}
			perm.recalculatePermissibles();
			return new BukkitPermission(perm);
		}
	}
	@Delegate
	@Getter
	private final org.bukkit.permissions.Permission bukkitPermission;

	public BukkitPermission(org.bukkit.permissions.Permission bukkitPermission)
	{
		this.bukkitPermission = bukkitPermission;
	}

	public static PermissionDefault getBukkitDefaultPermission(final Permission.Default defaultPerm)
	{
		switch (defaultPerm)
		{
		case TRUE:
			return PermissionDefault.TRUE;
		case OP:
			return PermissionDefault.OP;
		case NOT_OP:
			return PermissionDefault.NOT_OP;
		case FALSE:
			return PermissionDefault.FALSE;
		default:
			return PermissionDefault.FALSE;
		}
	}
}
