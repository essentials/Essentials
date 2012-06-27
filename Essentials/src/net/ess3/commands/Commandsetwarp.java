package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.api.IWarps;
import net.ess3.permissions.WarpPermissions;
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

		if (args[0].matches("[0-9]+"))
		{
			throw new NotEnoughArgumentsException();
		}

		final Location loc = user.getLocation();
		final IWarps warps = ess.getWarps();
		Location warpLoc = null;

		try
		{
			warpLoc = warps.getWarp(args[0]);
		}
		catch (WarpNotFoundException ex)
		{
		}

		//todo permissions
		if (warpLoc == null || WarpPermissions.getPermission("overwrite." + args[0]).isAuthorized(user))
			
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
