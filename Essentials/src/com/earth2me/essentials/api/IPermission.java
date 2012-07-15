package com.earth2me.essentials.api;

import com.earth2me.essentials.api.server.CommandSender;
import com.earth2me.essentials.api.server.Permission;


public interface IPermission
{
	String getPermissionName();

	boolean isAuthorized(CommandSender sender);

	Permission getPermission();

	Permission.Default getPermissionDefault();
}
