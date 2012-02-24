package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.I18nComponent._;
import com.earth2me.essentials.components.users.IUser;


public class Commandignore extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		IUser player;
		try
		{
			player = getPlayer(args, 0);
		}
		catch (NoSuchFieldException ex)
		{
			player = getContext().getUser(args[0]);
		}
		if (player == null)
		{
			throw new Exception(_("playerNotFound"));
		}
		final String name = player.getName();
		user.acquireWriteLock();
		if (user.isIgnoringPlayer(name))
		{
			user.setIgnoredPlayer(name, false);
			user.sendMessage(_("unignorePlayer", player.getName()));
		}
		else
		{
			user.setIgnoredPlayer(name, true);
			user.sendMessage(_("ignorePlayer", player.getName()));
		}
	}
}
