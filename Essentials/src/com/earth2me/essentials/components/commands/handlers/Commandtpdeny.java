package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;


public class Commandtpdeny extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		final IUserComponent player = user.getTeleportRequester();
		if (player == null)
		{
			throw new Exception($("noPendingRequest"));
		}

		user.sendMessage($("requestDenied"));
		player.sendMessage($("requestDeniedFrom", user.getDisplayName()));
		user.requestTeleport(null, false);
	}
}
