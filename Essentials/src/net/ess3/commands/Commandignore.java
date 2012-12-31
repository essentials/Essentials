package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.utils.Util;


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
			player = ess.getUserMap().matchUserExcludingHidden(args[0], user.getPlayer());
		}
		catch (Exception e)
		{
			if (args[0].equalsIgnoreCase("list"))
			{
				user.sendMessage(Util.joinList(user.getData().getIgnore()));
				throw new NoChargeException();
			}
			else
			{
				throw e;
			}
		}

		if (user.isIgnoringPlayer(player))
		{
			user.setIgnoredPlayer(player, false);
			user.sendMessage(_("unignorePlayer", player.getName()));
		}
		else
		{
			user.setIgnoredPlayer(player, true);
			user.sendMessage(_("ignorePlayer", player.getName()));
		}
		user.queueSave();
	}
}
