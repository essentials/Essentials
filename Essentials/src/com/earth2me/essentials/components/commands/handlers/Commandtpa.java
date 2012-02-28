package com.earth2me.essentials.components.commands.handlers;

import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.components.settings.users.IUserComponent;
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
			throw new Exception(_("teleportDisabled", player.getDisplayName()));
		}
		if (!player.isIgnoringPlayer(user.getName()))
		{
			player.requestTeleport(user, false);
			player.sendMessage(_("teleportRequest", user.getDisplayName()));
			player.sendMessage(_("typeTpaccept"));
			player.sendMessage(_("typeTpdeny"));
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
				player.sendMessage(_("teleportRequestTimeoutInfo", tpaAcceptCancellation));
			}
		}
		user.sendMessage(_("requestSent", player.getDisplayName()));
	}
}
