package com.earth2me.essentials.api;

import com.earth2me.essentials.api.server.ICommandSender;
import com.earth2me.essentials.api.server.Permission;


public interface IPermission
{
	String getPermissionName();

	boolean isAuthorized(ICommandSender sender);

	Permission getPermission();

	Permission.Default getPermissionDefault();
}
