package net.ess3.protect;

import java.util.Locale;
import net.ess3.api.IPermission;
import net.ess3.bukkit.PermissionFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;


public enum Permissions implements IPermission
{	
	ENTITYTARGET(PermissionDefault.TRUE),
	PVP(PermissionDefault.TRUE),
	PREVENTDAMAGE_FALL(PermissionDefault.FALSE),
	PREVENTDAMAGE_CREEPER(PermissionDefault.FALSE),
	PREVENTDAMAGE_CONTACT(PermissionDefault.FALSE),
	PREVENTDAMAGE_FIREBALL(PermissionDefault.FALSE),
	PREVENTDAMAGE_PROJECTILES(PermissionDefault.FALSE),
	PREVENTDAMAGE_LAVADAMAGE(PermissionDefault.FALSE),
	PREVENTDAMAGE_TNT(PermissionDefault.FALSE),
	PREVENTDAMAGE_SUFFOCATION(PermissionDefault.FALSE),
	PREVENTDAMAGE_FIRE(PermissionDefault.FALSE),
	PREVENTDAMAGE_DROWNING(PermissionDefault.FALSE),
	PREVENTDAMAGE_LIGHTNING(PermissionDefault.FALSE),
	PREVENTDAMAGE_NONE(PermissionDefault.FALSE),	
	USEFLINTSTEEL(PermissionDefault.TRUE);
	private static final String base = "essentials.protect.";
	private final String permission;
	private final PermissionDefault defaultPerm;
	private transient String parent = null;

	private Permissions()
	{
		this(PermissionDefault.OP);
	}

	private Permissions(final PermissionDefault defaultPerm)
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
}
