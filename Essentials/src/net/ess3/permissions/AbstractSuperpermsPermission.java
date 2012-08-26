package net.ess3.permissions;

import net.ess3.bukkit.PermissionFactory;
import net.ess3.api.IPermission;
import net.ess3.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;


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
			return PermissionFactory.registerPermission(getPermissionName(), getPermissionDefault());
		}
	}

	/**
	 * PermissionDefault is OP, if the method is not overwritten.
	 * @return 
	 */
	
	@Override
	public PermissionDefault getPermissionDefault()
	{
		return PermissionDefault.OP;
	}

	
	@Override
	public boolean isAuthorized(final CommandSender sender)
	{
		return sender.hasPermission(getPermission());
	}
}
