package net.ess3.permissions;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;


public class DotStarPermission extends AbstractSuperpermsPermission
{
	private final String base;
	private final String dotStarPermission;
	private final PermissionDefault defaultPerm;

	public DotStarPermission(final String base)
	{
		this(base, PermissionDefault.OP);
	}

	public DotStarPermission(final String base, final PermissionDefault defaultPerm)
	{
		this.base = base + ".";
		this.dotStarPermission = base + ".*";
		this.defaultPerm = defaultPerm;
	}

	protected String getBase()
	{
		return base;
	}

	@Override
	public String getPermissionName()
	{
		return dotStarPermission;
	}

	@Override
	public PermissionDefault getPermissionDefault()
	{
		return defaultPerm;
	}

	public boolean isAuthorized(CommandSender sender, final String... subPerms)
	{
		for (String subPerm : subPerms)
		{
			final String permission = getBase().concat(subPerm);
			if (sender.isPermissionSet(permission))
			{
				return sender.hasPermission(permission);
			}
		}
		final String parentPermission = getParentPermission();
		if (parentPermission != null && sender.isPermissionSet(parentPermission))
		{
			return sender.hasPermission(parentPermission);
		}
		else
		{
			return getPermissionDefault().getValue(sender.isOp());
		}
	}
}
