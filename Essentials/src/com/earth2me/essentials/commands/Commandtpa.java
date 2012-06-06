package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.WorldPermissions;
import lombok.Cleanup;


public class Commandtpa extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		@Cleanup
		IUser player = getPlayer(args, 0);
		player.acquireReadLock();
		if (!player.getData().isTeleportEnabled())
		{
			throw new Exception(_("teleportDisabled", player.getDisplayName()));
		}
		@Cleanup
		ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		if (user.getWorld() != player.getWorld() && ess.getSettings().getData().getGeneral().isWorldTeleportPermissions()
			&& !WorldPermissions.getPermission(user.getWorld().getName()).isAuthorized(user))
		{
			throw new Exception(_("noPerm", "essentials.world." + player.getWorld().getName()));
		}
		if (!player.isIgnoringPlayer(user.getName()))
		{
			player.requestTeleport(user, false);
			player.sendMessage(_("teleportRequest", user.getDisplayName()));
			player.sendMessage(_("typeTpaccept"));
			player.sendMessage(_("typeTpdeny"));
			int tpaAcceptCancellation = 0;
			tpaAcceptCancellation = settings.getData().getCommands().getTpa().getTimeout();
			if (tpaAcceptCancellation != 0)
			{
				player.sendMessage(_("teleportRequestTimeoutInfo", tpaAcceptCancellation));
			}
		}
		user.sendMessage(_("requestSent", player.getDisplayName()));
	}
}
