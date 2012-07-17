package net.ess3.permissions;

import net.ess3.api.IPermission;
import net.ess3.api.server.CommandSender;
import net.ess3.api.server.Permission;
import net.ess3.utils.Util;


public abstract class AbstractSuperpermsPermission implements IPermission
{
	//todo - sort all this out
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
			return null;// Util.registerPermission(getPermission(), getPermissionDefault());
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
