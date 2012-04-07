package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.economy.Trade;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.api.server.ILocation;
import com.earth2me.essentials.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandjump extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		Location loc;
		final ILocation cloc = user.getLocation();

		try
		{
			loc = LocationUtil.getTarget(user);
			loc.setYaw(cloc.getYaw());
			loc.setPitch(cloc.getPitch());
			loc.setY(loc.getY() + 1);
		}
		catch (NullPointerException ex)
		{
			throw new Exception(_("jumpError"), ex);
		}

		final Trade charge = new Trade(commandName, ess);
		charge.isAffordableFor(user);
		user.getTeleport().teleport(loc, charge, TeleportCause.COMMAND);
	}
}
