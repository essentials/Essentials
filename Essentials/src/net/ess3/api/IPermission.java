package net.ess3.api;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;


public interface IPermission
{
	String getPermissionName();

	boolean isAuthorized(CommandSender sender);

	String getParentPermission();

	PermissionDefault getPermissionDefault();
}
