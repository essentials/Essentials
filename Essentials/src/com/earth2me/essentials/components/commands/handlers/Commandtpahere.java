package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import lombok.Cleanup;


public class Commandtpahere extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		@Cleanup
		final IUserComponent player = getPlayer(args, 0);
		player.acquireReadLock();
		if (!player.getData().isTeleportEnabled())
		{
			throw new Exception($("teleportDisabled", player.getDisplayName()));
		}
		player.requestTeleport(user, true);
		player.sendMessage($("teleportHereRequest", user.getDisplayName()));
		player.sendMessage($("typeTpaccept"));
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
		if (tpaAcceptCancellation != 0)
		{
			player.sendMessage($("teleportRequestTimeoutInfo", tpaAcceptCancellation));
		}
		user.sendMessage($("requestSent", player.getDisplayName()));
	}
}
