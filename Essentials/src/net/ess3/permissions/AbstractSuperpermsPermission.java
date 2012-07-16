package net.ess3.permissions;

import net.ess3.api.IPermission;
import net.ess3.api.server.CommandSender;
import net.ess3.api.server.Permission;


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
	public boolean isAuthorized(final CommandSender sender)
	{
		return sender.hasPermission(getPermission());
	}
}
