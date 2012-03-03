package com.earth2me.essentials.protect;

import com.earth2me.essentials.api.IPermissions;
import com.earth2me.essentials.perm.AbstractSuperpermsPermission;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.permissions.PermissionDefault;

public class BlockBreakPermissions extends AbstractSuperpermsPermission{
	private static Map<Material,IPermissions> permissions = new EnumMap<Material, IPermissions>(Material.class);
	private static final String base = "essentials.protect.blockbreak.";
	private final String permission;

	public static IPermissions getPermission(Material mat)
	{
		IPermissions perm = permissions.get(mat);
		if (perm == null) {
			perm = new BlockBreakPermissions(mat.toString().toLowerCase(Locale.ENGLISH));
			permissions.put(mat, perm);
		}
		return perm;
	}

	private BlockBreakPermissions(String matName)
	{
		this.permission = base + matName;
	}

	@Override
	public String getPermission()
	{
		return this.permission;
	}

	@Override
	public PermissionDefault getPermissionDefault()
	{
		return PermissionDefault.TRUE;
	}
}

