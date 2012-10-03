package net.ess3.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.api.IWarps;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import net.ess3.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandwarp extends EssentialsCommand
{
	private final static int WARPS_PER_PAGE = 20;

	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length == 0 || args[0].matches("[0-9]+"))
		{
			if (!Permissions.WARP_LIST.isAuthorized(user))
			{
				throw new Exception(_("warpListPermission"));
			}
			warpList(user, args);
			throw new NoChargeException();
		}
		if (args.length > 0)
		{
			IUser otherUser = null;
			if (args.length == 2 && Permissions.WARP_OTHERS.isAuthorized(user))
			{
				otherUser = ess.getUserMap().matchUser(args[1],Permissions.WARP_HIDDEN.isAuthorized(user), false);
				if (otherUser == null)
				{
					throw new Exception(_("playerNotFound"));
				}
				warpUser(otherUser, args[0]);
				throw new NoChargeException();
			}
			warpUser(user, args[0]);
			throw new NoChargeException();
		}
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2 || args[0].matches("[0-9]+"))
		{
			warpList(sender, args);
			throw new NoChargeException();
		}
		IUser otherUser = ess.getUserMap().matchUser(args[1],Permissions.WARP_HIDDEN.isAuthorized(sender), false);
		if (otherUser == null)
		{
			throw new Exception(_("playerNotFound"));
		}
		otherUser.getTeleport().warp(args[0], null, TeleportCause.COMMAND);
		throw new NoChargeException();

	}

	//TODO: Use one of the new text classes, like /help ?
	private void warpList(final CommandSender sender, final String[] args) throws Exception
	{
		final IWarps warps = ess.getWarps();
		if (warps.isEmpty())
		{
			throw new Exception(_("noWarpsDefined"));
		}
		final List<String> warpNameList = new ArrayList<String>(warps.getList());

		if (sender instanceof IUser)
		{
			final Iterator<String> iterator = warpNameList.iterator();
			while (iterator.hasNext())
			{
				final String warpName = iterator.next();
				if (!Permissions.WARPS.isAuthorized(sender, warpName))
				{
					iterator.remove();
				}
			}
		}
		int page = 1;
		if (args.length > 0)
		{
			page = Integer.parseInt(args[0]);
		}

		final int warpPage = (page - 1) * WARPS_PER_PAGE;
		final String warpList = Util.joinList(warpNameList.subList(warpPage, warpPage + Math.min(warpNameList.size() - warpPage, WARPS_PER_PAGE)));

		if (warpNameList.size() > WARPS_PER_PAGE)
		{
			sender.sendMessage(_("warpsCount", warpNameList.size(), page, (int)Math.ceil(warpNameList.size() / (double)WARPS_PER_PAGE)));
			sender.sendMessage(warpList);
		}
		else
		{
			sender.sendMessage(_("warps", warpList));
		}
	}

	private void warpUser(final IUser user, final String name) throws Exception
	{
		final Trade chargeWarp = new Trade("warp-" + name.toLowerCase(Locale.ENGLISH).replace('_', '-'), ess);
		final Trade chargeCmd = new Trade(this.commandName, ess);
		final double fullCharge = chargeWarp.getCommandCost(user) + chargeCmd.getCommandCost(user);
		final Trade charge = new Trade(fullCharge, ess);
		charge.isAffordableFor(user);
		if (Permissions.WARPS.isAuthorized(user, name))
		{
			user.getTeleport().warp(name, charge, TeleportCause.COMMAND);
			return;
		}
		throw new Exception(_("warpUsePermission"));
	}
}