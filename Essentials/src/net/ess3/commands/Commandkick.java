package net.ess3.commands;

import net.ess3.Console;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.utils.FormatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandkick extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final IUser user = ess.getUserMap().matchUserExcludingHidden(args[0], getPlayerOrNull(sender));
		if (Permissions.KICK_EXEMPT.isAuthorized(user))
		{
			throw new Exception(_("kickExempt"));
		}
		String kickReason = args.length > 1 ? getFinalArg(args, 1) : _("kickDefault");
		kickReason = FormatUtil.replaceFormat(kickReason.replace("\\n", "\n"));
		user.getPlayer().kickPlayer(kickReason);
		final String senderName = sender instanceof IUser ? ((IUser)sender).getPlayer().getDisplayName() : Console.NAME;

		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			if (Permissions.KICK_NOTIFY.isAuthorized(onlinePlayer))
			{
				onlinePlayer.sendMessage(_("playerKicked", senderName, user.getName(), kickReason));
			}
		}
	}
}
