package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.warps.IWarpsComponent;
import com.earth2me.essentials.components.warps.Warp;
import com.earth2me.essentials.storage.LocationData;
import org.bukkit.Location;


public class Commandsetwarp extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
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
		final IWarpsComponent warps = getContext().getWarps();
		Location warpLoc = warps.getWarp(args[0]).getLocation().getBukkitLocation();

		if (warpLoc == null || user.hasPermission("essentials.warp.overwrite." + args[0]))
		{
			final Warp warp = new Warp();
			warp.setLocation(new LocationData(loc));
			warp.setName(args[0]);
			warps.setWarp(args[0], warp);
		}
		else
		{
			throw new Exception($("warpOverwrite"));
		}

		user.sendMessage($("warpSet", args[0]));
	}
}
