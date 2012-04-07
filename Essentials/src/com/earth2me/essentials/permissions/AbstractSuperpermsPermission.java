package com.earth2me.essentials.permissions;

import com.earth2me.essentials.utils.Util;
import com.earth2me.essentials.api.IPermission;
import com.earth2me.essentials.api.server.ICommandSender;
import com.earth2me.essentials.api.server.Permission;


public abstract class AbstractSuperpermsPermission implements IPermission
{
	protected Permission bukkitPerm;

	@Override
	public Permission getPermission()
	{
		if (bukkitPerm != null)
		{
			return bukkitPerm;
		}
		else
		{
			return Permission.create(getPermissionName(), getPermissionDefault());
		}
	}

	/**
	 * PermissionDefault is OP, if the method is not overwritten.
	 * @return 
	 */
	@Override
	public Permission.Default getPermissionDefault()
	{
		return Permission.Default.OP;
	}

	@Override
	public boolean isAuthorized(final ICommandSender sender)
	{
		return sender.hasPermission(getPermission());
	}
}
