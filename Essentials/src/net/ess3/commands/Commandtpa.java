package net.ess3.commands;

import lombok.Cleanup;
import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.WorldPermissions;


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
		IUser player = ess.getUserMap().matchUser(args[0], false, false);
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
		if (!player.isIgnoringPlayer(user))
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
