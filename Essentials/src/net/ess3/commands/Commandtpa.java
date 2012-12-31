package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandtpa extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}


		IUser player = ess.getUserMap().matchUserExcludingHidden(args[0], user.getPlayer());
		if (!player.getData().isTeleportEnabled())
		{
			throw new Exception(_("teleportDisabled", player.getPlayer().getDisplayName()));
		}

		ISettings settings = ess.getSettings();
		if (user.getPlayer().getWorld() != player.getPlayer().getWorld() && ess.getSettings().getData().getGeneral().isWorldTeleportPermissions() && !Permissions.WORLD.isAuthorized(
				user, user.getPlayer().getWorld().getName()))
		{
			throw new Exception(_("noPerm", "essentials.world." + player.getPlayer().getWorld().getName()));
		}
		if (!player.isIgnoringPlayer(user))
		{
			player.requestTeleport(user, false);
			player.sendMessage(_("teleportRequest", user.getPlayer().getDisplayName()));
			player.sendMessage(_("typeTpaccept"));
			player.sendMessage(_("typeTpdeny"));
			int tpaAcceptCancellation = 0;
			tpaAcceptCancellation = settings.getData().getCommands().getTeleport().getRequestTimeout();
			if (tpaAcceptCancellation != 0)
			{
				player.sendMessage(_("teleportRequestTimeoutInfo", tpaAcceptCancellation));
			}
		}
		user.sendMessage(_("requestSent", player.getPlayer().getDisplayName()));
	}
}
