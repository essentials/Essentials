package net.ess3.commands;

import java.util.logging.Level;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.api.server.Player;
import net.ess3.permissions.Permissions;
import net.ess3.utils.Util;


public class Commandhelpop extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		user.setDisplayNick();
		final String message = _("helpOp", user.getDisplayName(), Util.stripFormat(getFinalArg(args, 0)));
		logger.log(Level.INFO, message);
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final IUser player = onlinePlayer.getUser();
			if (!Permissions.HELPOP_RECEIVE.isAuthorized(player))
			{
				continue;
			}
			player.sendMessage(message);
		}
	}
}
