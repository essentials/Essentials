package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.I18nComponent._;
import com.earth2me.essentials.components.users.IUser;


public class Commandtpdeny extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final IUser player = user.getTeleportRequester();
		if (player == null)
		{
			throw new Exception(_("noPendingRequest"));
		}

		user.sendMessage(_("requestDenied"));
		player.sendMessage(_("requestDeniedFrom", user.getDisplayName()));
		user.requestTeleport(null, false);
	}
}
