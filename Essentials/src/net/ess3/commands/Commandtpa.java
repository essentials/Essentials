package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
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
		if (!player.isIgnoringPlayer(user.getName()))
		{
			player.requestTeleport(user, false);
			player.sendMessage(_("teleportRequest", user.getDisplayName()));
			player.sendMessage(_("typeTpaccept"));
			player.sendMessage(_("typeTpdeny"));
			int tpaAcceptCancellation = 0;
			ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			try {
				tpaAcceptCancellation = settings.getData().getCommands().getTpa().getTimeout();
			} finally {
				settings.unlock();
			}
			if (tpaAcceptCancellation != 0)
			{
				player.sendMessage(_("teleportRequestTimeoutInfo", tpaAcceptCancellation));
			}
		}
		user.sendMessage(_("requestSent", player.getDisplayName()));
	}
}
