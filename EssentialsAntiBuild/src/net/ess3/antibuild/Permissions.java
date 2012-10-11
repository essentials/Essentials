package net.ess3.antibuild;

import java.util.EnumMap;
import java.util.Locale;
import net.ess3.api.IPermission;
import net.ess3.bukkit.PermissionFactory;
import net.ess3.permissions.BasePermission;
import net.ess3.permissions.MaterialDotStarPermission;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;


public enum Permissions implements IPermission
{
	BUILD("essentials.", PermissionDefault.TRUE),
	BLACKLIST_ALLOWPLACEMENT,
	BLACKLIST_ALLOWUSAGE,
	BLACKLIST_ALLOWBREAK,
	ALERTS,
	ALERTS_NOTRIGGER;
	private static final String defaultBase = "essentials.build.";
	private final String permission;
	private final PermissionDefault defaultPerm;
	private transient String parent = null;

	private Permissions()
	{
		this(PermissionDefault.OP);
	}

	private Permissions(final PermissionDefault defaultPerm)
	{
		this(defaultBase, defaultPerm);
	}
	
	private Permissions(final String base, final PermissionDefault defaultPerm)
	{
		permission = base + toString().toLowerCase(Locale.ENGLISH).replace('_', '.');
		this.defaultPerm = defaultPerm;
	}

	@Override
	public String getPermissionName()
	{
		return permission;
	}

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

	@Override
	public PermissionDefault getPermissionDefault()
	{
		return this.defaultPerm;
	}

	@Override
	public boolean isAuthorized(final CommandSender sender)
	{
		return PermissionFactory.checkPermission(sender, this);
	}
	
	public static MaterialDotStarPermission PLACEMENT = new MaterialDotStarPermission("essentials.build.place");
	public static MaterialDotStarPermission BREAK = new MaterialDotStarPermission("essentials.build.break");
	public static MaterialDotStarPermission INTERACT = new MaterialDotStarPermission("essentials.build.interact");
	public static MaterialDotStarPermission CRAFT = new MaterialDotStarPermission("essentials.build.craft");
	public static MaterialDotStarPermission PICKUP = new MaterialDotStarPermission("essentials.build.pickup");
	public static MaterialDotStarPermission DROP = new MaterialDotStarPermission("essentials.build.drop");
	
}
