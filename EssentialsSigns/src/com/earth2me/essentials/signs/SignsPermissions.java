package com.earth2me.essentials.signs;

import com.earth2me.essentials.api.IPermissions;
import com.earth2me.essentials.perm.BasePermission;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class SignsPermissions
{
	public static final IPermissions COLOR = new BasePermission("essentials.signs.", "color");
	public static final IPermissions PROTECTION_OVERRIDE = new BasePermission("essentials.signs.protection.", "override");
	public static final IPermissions TRADE_OVERRIDE = new BasePermission("essentials.signs.trade.", "override");
	private static Map<String, IPermissions> createpermissions = new HashMap<String, IPermissions>();

	public static IPermissions getCreatePermission(final String signName)
	{
		IPermissions perm = createpermissions.get(signName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.signs.create.", signName.toLowerCase(Locale.ENGLISH));
			createpermissions.put(signName, perm);
		}
		return perm;
	}
	private static Map<String, IPermissions> usepermissions = new HashMap<String, IPermissions>();

	public static IPermissions getUsePermission(final String signName)
	{
		IPermissions perm = usepermissions.get(signName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.signs.use.", signName.toLowerCase(Locale.ENGLISH));
			usepermissions.put(signName, perm);
		}
		return perm;
	}
	private static Map<String, IPermissions> breakpermissions = new HashMap<String, IPermissions>();

	public static IPermissions getBreakPermission(final String signName)
	{
		IPermissions perm = breakpermissions.get(signName);
		if (perm == null)
		{
			perm = new BasePermission("essentials.signs.break.", signName.toLowerCase(Locale.ENGLISH));
			breakpermissions.put(signName, perm);
		}
		return perm;
	}
}
