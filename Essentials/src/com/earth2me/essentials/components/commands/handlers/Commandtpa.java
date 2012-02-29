package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import lombok.Cleanup;


public class Commandtpa extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		@Cleanup
		IUserComponent player = getPlayer(args, 0);
		player.acquireReadLock();
		if (!player.getData().isTeleportEnabled())
		{
			throw new Exception($("teleportDisabled", player.getDisplayName()));
		}
		if (!player.isIgnoringPlayer(user.getName()))
		{
			player.requestTeleport(user, false);
			player.sendMessage($("teleportRequest", user.getDisplayName()));
			player.sendMessage($("typeTpaccept"));
			player.sendMessage($("typeTpdeny"));
			int tpaAcceptCancellation = 0;
			ISettingsComponent settings = getContext().getSettings();
			settings.acquireReadLock();
			try {
				tpaAcceptCancellation = settings.getData().getCommands().getTpa().getTimeout();
			} finally {
				settings.unlock();
			}
			if (tpaAcceptCancellation != 0)
			{
				player.sendMessage($("teleportRequestTimeoutInfo", tpaAcceptCancellation));
			}
		}
		user.sendMessage($("requestSent", player.getDisplayName()));
	}
}
