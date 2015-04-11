package org.mcess.essentials.commands;

import org.mcess.essentials.User;
import org.mcess.essentials.api.IWarps;
import org.mcess.essentials.utils.NumberUtil;
import org.mcess.essentials.utils.StringUtil;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Location;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandsetwarp extends EssentialsCommand
{
	public Commandsetwarp()
	{
		super("setwarp");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		if (NumberUtil.isInt(args[0]) || args[0].isEmpty())
		{
			throw new NoSuchFieldException(I18n.tl("invalidWarpName"));
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
		catch (InvalidWorldException ex)
		{
		}

		if (warpLoc == null || user.isAuthorized("essentials.warp.overwrite." + StringUtil.safeString(args[0])))
		{
			warps.setWarp(args[0], loc);
		}
		else
		{
			throw new Exception(I18n.tl("warpOverwrite"));
		}
		user.sendMessage(I18n.tl("warpSet", args[0]));
	}
}
