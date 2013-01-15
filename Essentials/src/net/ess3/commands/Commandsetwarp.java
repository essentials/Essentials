package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.api.IWarps;
import net.ess3.permissions.Permissions;
import net.ess3.utils.Util;
import org.bukkit.Location;


public class Commandsetwarp extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		if (Util.isInt(args[0]))
		{
			throw new NoSuchFieldException(_("invalidWarpName"));
		}

		final Location loc = user.getPlayer().getLocation();
		final IWarps warps = ess.getWarps();
		Location warpLoc = null;

		try
		{
			warpLoc = warps.getWarp(args[0]);
		}
		catch (WarpNotFoundException ex)
		{
		}

		if (warpLoc == null || Permissions.WARP_OVERWRITE.isAuthorized(user, args[0]))
		{
			warps.setWarp(args[0], loc);
		}
		else
		{
			throw new Exception(_("warpOverwrite"));
		}

		user.sendMessage(_("warpSet", args[0]));
	}
}
