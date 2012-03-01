package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Trade;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.perm.Permissions;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtpaccept extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (user.getTeleportRequester() == null)
		{
			throw new Exception(_("noPendingRequest"));
		}

		final IUserComponent target = user.getTeleportRequester();
		if (target == null
			|| target.getBase() instanceof OfflinePlayer
			|| (user.isTeleportRequestHere() && !Permissions.TPAHERE.isAuthorized(target))
			|| (!user.isTeleportRequestHere() && !Permissions.TPA.isAuthorized(target) && !Permissions.TPAALL.isAuthorized(target)))
		{
			throw new Exception(_("noPendingRequest"));
		}

		if (args.length > 0 && !target.getName().contains(args[0]))
		{
			throw new Exception(_("noPendingRequest"));
		}

		int tpaAcceptCancellation = 0;
		ISettingsComponent settings = getContext().getSettings();
		settings.acquireReadLock();
		try
		{
			tpaAcceptCancellation = settings.getData().getCommands().getTpa().getTimeout();
		}
		finally
		{
			settings.unlock();
		}

		if (tpaAcceptCancellation != 0 && (System.currentTimeMillis() - user.getTeleportRequestTime()) / 1000 > tpaAcceptCancellation)
		{
			user.requestTeleport(null, false);
			throw new Exception(_("requestTimedOut"));
		}

		final Trade charge = new Trade(getCommandName(), getContext());
		if (user.isTeleportRequestHere())
		{
			charge.isAffordableFor(user);
		}
		else
		{
			charge.isAffordableFor(target);
		}
		user.sendMessage(_("requestAccepted"));
		target.sendMessage(_("requestAcceptedFrom", user.getDisplayName()));

		if (user.isTeleportRequestHere())
		{
			user.getTeleporter().teleport(target, charge, TeleportCause.COMMAND);
		}
		else
		{
			target.getTeleporter().teleport(user, charge, TeleportCause.COMMAND);
		}
		user.requestTeleport(null, false);
	}
}
