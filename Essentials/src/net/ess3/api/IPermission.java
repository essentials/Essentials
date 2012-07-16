package net.ess3.api;

import net.ess3.api.server.CommandSender;
import net.ess3.api.server.Permission;


public interface IPermission
{
	String getPermissionName();

	boolean isAuthorized(CommandSender sender);

	Permission getPermission();

	Permission.Default getPermissionDefault();
}
