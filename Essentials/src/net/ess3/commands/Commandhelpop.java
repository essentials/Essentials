package net.ess3.commands;

import java.util.logging.Level;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.utils.FormatUtil;
import org.bukkit.entity.Player;


public class Commandhelpop extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		final String message = _("helpOp", user.getPlayer().getDisplayName(), FormatUtil.stripFormat(getFinalArg(args, 0)));
		logger.log(Level.INFO, message);
		for (Player player : server.getOnlinePlayers())
		{
			if (!Permissions.HELPOP_RECEIVE.isAuthorized(player))
			{
				continue;
			}
			player.sendMessage(message);
		}
	}
}
