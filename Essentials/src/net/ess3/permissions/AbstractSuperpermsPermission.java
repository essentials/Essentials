package net.ess3.permissions;

import net.ess3.api.IPermission;
import net.ess3.bukkit.PermissionFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;


public abstract class AbstractSuperpermsPermission implements IPermission
{
	private String parent = null;

	@Override
	public String getParentPermission()
	{
		if (parent != null)
		{
			return parent;
		}
		else
		{
			return PermissionFactory.registerParentPermission(getPermissionName());
		}
	}

	/**
	 * PermissionDefault is OP, if the method is not overwritten.
	 *
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
		return PermissionFactory.checkPermission(sender, this);
	}
}
