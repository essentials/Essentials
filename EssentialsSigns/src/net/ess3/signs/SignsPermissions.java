package net.ess3.signs;

import net.ess3.api.IPermission;
import net.ess3.permissions.BasePermission;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.ess3.permissions.DotStarPermission;


public class SignsPermissions
{
	public static final IPermission COLOR = new BasePermission("essentials.signs.", "color");
	public static final IPermission PROTECTION_OVERRIDE = new BasePermission("essentials.signs.protection.", "override");
	public static final IPermission TRADE_OVERRIDE = new BasePermission("essentials.signs.trade.", "override");
	public static final DotStarPermission CREATE = new DotStarPermission("essentials.signs.create");
	public static final DotStarPermission USE = new DotStarPermission("essentials.signs.use");
	public static final DotStarPermission BREAK = new DotStarPermission("essentials.signs.break");
}
