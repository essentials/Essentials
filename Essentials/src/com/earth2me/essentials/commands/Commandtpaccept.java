package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.economy.Trade;
import com.earth2me.essentials.permissions.Permissions;
import com.earth2me.essentials.permissions.WorldPermissions;
import lombok.Cleanup;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtpaccept extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final IUser target = user.getTeleportRequester();
		@Cleanup
		ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		if (target == null || !target.isOnline()
			|| (args.length > 0 && !target.getName().contains(args[0]))
			|| (user.isTpRequestHere() && !Permissions.TPAHERE.isAuthorized(target))
			|| (!user.isTpRequestHere() && ((!Permissions.TPA.isAuthorized(target) && !Permissions.TPAALL.isAuthorized(target))
											|| (user.getWorld() != target.getWorld()
												&& settings.getData().getGeneral().isWorldTeleportPermissions()
												&& !WorldPermissions.getPermission(user.getWorld().getName()).isAuthorized(user)))))
		{
			throw new Exception(_("noPendingRequest"));
		}

		long timeout = settings.getData().getCommands().getTpa().getTimeout();
		if (timeout != 0 && (System.currentTimeMillis() - user.getTeleportRequestTime()) / 1000 > timeout)
		{
			user.requestTeleport(null, false);
			throw new Exception(_("requestTimedOut"));
		}

		final Trade charge = new Trade(this.commandName, ess);
		if (user.isTpRequestHere())
		{
			charge.isAffordableFor(user);
		}
		else
		{
			charge.isAffordableFor(target);
		}
		user.sendMessage(_("requestAccepted"));
		target.sendMessage(_("requestAcceptedFrom", user.getDisplayName()));

		if (user.isTpRequestHere())
		{
			user.getTeleport().teleport(target, charge, TeleportCause.COMMAND);
		}
		else
		{
			target.getTeleport().teleport(user, charge, TeleportCause.COMMAND);
		}
		user.requestTeleport(null, false);
	}
}
