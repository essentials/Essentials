package net.ess3.antibuild;

import java.util.EnumMap;
import java.util.Locale;
import net.ess3.api.IPermission;
import net.ess3.bukkit.PermissionFactory;
import net.ess3.permissions.BasePermission;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;


public enum Permissions implements IPermission
{
	BLACKLIST_ALLOWPLACEMENT,
	BLACKLIST_ALLOWUSAGE,
	BLACKLIST_ALLOWBREAK,
	ALERTS,
	ALERTS_NOTRIGGER;
	private static final String base = "essentials.build.";
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
	private static EnumMap<Material, IPermission> permissions = new EnumMap<Material, IPermission>(Material.class);

	public static IPermission getPlacePermission(final Material mat)
	{
		IPermission perm = permissions.get(mat);
		if (perm == null)
		{
			perm = new BasePermission("essentials.place.", mat.toString().toLowerCase(Locale.ENGLISH).replace("_", ""))
			{
				@Override
				public PermissionDefault getPermissionDefault()
				{
					return PermissionDefault.TRUE;
				}
			};
			permissions.put(mat, perm);
		}
		return perm;
	}

	public static IPermission getBreakPermission(final Material mat)
	{
		IPermission perm = permissions.get(mat);
		if (perm == null)
		{
			perm = new BasePermission("essentials.break.", mat.toString().toLowerCase(Locale.ENGLISH).replace("_", ""))
			{
				@Override
				public PermissionDefault getPermissionDefault()
				{
					return PermissionDefault.TRUE;
				}
			};
			permissions.put(mat, perm);
		}
		return perm;
	}

	public static IPermission getInteractPermission(final Material mat)
	{
		IPermission perm = permissions.get(mat);
		if (perm == null)
		{
			perm = new BasePermission("essentials.interact.", mat.toString().toLowerCase(Locale.ENGLISH).replace("_", ""))
			{
				@Override
				public PermissionDefault getPermissionDefault()
				{
					return PermissionDefault.TRUE;
				}
			};
			permissions.put(mat, perm);
		}
		return perm;
	}

	public static IPermission getCraftPermission(final Material mat)
	{
		IPermission perm = permissions.get(mat);
		if (perm == null)
		{
			perm = new BasePermission("essentials.craft.", mat.toString().toLowerCase(Locale.ENGLISH).replace("_", ""))
			{
				@Override
				public PermissionDefault getPermissionDefault()
				{
					return PermissionDefault.TRUE;
				}
			};
			permissions.put(mat, perm);
		}
		return perm;
	}

	public static IPermission getPickupPermission(final Material mat)
	{
		IPermission perm = permissions.get(mat);
		if (perm == null)
		{
			perm = new BasePermission("essentials.pickup.", mat.toString().toLowerCase(Locale.ENGLISH).replace("_", ""))
			{
				@Override
				public PermissionDefault getPermissionDefault()
				{
					return PermissionDefault.TRUE;
				}
			};
			permissions.put(mat, perm);
		}
		return perm;
	}

	public static IPermission getDropPermission(final Material mat)
	{
		IPermission perm = permissions.get(mat);
		if (perm == null)
		{
			perm = new BasePermission("essentials.drop.", mat.toString().toLowerCase(Locale.ENGLISH).replace("_", ""))
			{
				@Override
				public PermissionDefault getPermissionDefault()
				{
					return PermissionDefault.TRUE;
				}
			};
			permissions.put(mat, perm);
		}
		return perm;
	}
}
