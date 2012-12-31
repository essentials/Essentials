package net.ess3.bukkit;

import java.util.StringTokenizer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import net.ess3.api.IPermission;


public class PermissionFactory
{
	public static String registerParentPermission(String permission)
	{
		final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
		final StringTokenizer tokenizer = new StringTokenizer(permission, ".");
		final StringBuilder builder = new StringBuilder(permission.length());
		Permission parent = null;
		while (tokenizer.hasMoreTokens())
		{
			String part = tokenizer.nextToken();
			if (!tokenizer.hasMoreTokens())
			{
				break;
			}
			builder.append(part).append(".*");
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
				perm.recalculatePermissibles();
			}
			builder.deleteCharAt(builder.length() - 1);
		}
		return parent == null ? null : parent.getName();
	}

	public static boolean checkPermission(CommandSender sender, IPermission perm)
	{
		final String permission = perm.getPermissionName();
		if (sender.isPermissionSet(permission))
		{
			return sender.hasPermission(permission);
		}
		else
		{
			final String parentPermission = perm.getParentPermission();
			if (parentPermission != null && sender.isPermissionSet(parentPermission))
			{
				return sender.hasPermission(parentPermission);
			}
			else
			{
				return perm.getPermissionDefault().getValue(sender.isOp());
			}
		}
	}
}
