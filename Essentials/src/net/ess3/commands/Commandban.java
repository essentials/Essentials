package net.ess3.commands;

import net.ess3.Console;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.user.Ban;
import net.ess3.user.UserData;
import net.ess3.utils.FormatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandban extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		final IUser user = ess.getUserMap().matchUser(args[0], true);
		if (!user.isOnline())
		{
			if (isUser(sender) && Permissions.BAN_OFFLINE.isAuthorized(sender))
			{
				sender.sendMessage(_("You can not ban that player."));
				return;
			}
		}
		else
		{
			if (isUser(sender) && Permissions.BAN_EXEMPT.isAuthorized(user))
			{
				sender.sendMessage(_("You can not ban that player."));
				return;
			}
		}

		final String senderName = isUser(sender) ? getPlayer(sender).getDisplayName() : Console.NAME;
		final String banReason;
		final UserData userData = user.getData();
		userData.setBan(new Ban());
		if (args.length > 1)
		{

			banReason = _("Banned: {0}", FormatUtil.replaceFormat(getFinalArg(args, 1).replace("\\n", "\n").replace("|", "\n")), senderName);
			userData.getBan().setReason(banReason);
		}
		else
		{
			banReason = _("Banned: {0}", _("The Ban Hammer has spoken!"), senderName);
			userData.getBan().setReason("");
		}

		user.setBanned(true);
		user.queueSave();
		user.getData().getBan().setTimeout(0);
		user.getPlayer().kickPlayer(banReason);
		for (Player player : server.getOnlinePlayers())
		{
			if (Permissions.BAN_NOTIFY.isAuthorized(player))
			{
				player.sendMessage(_("Player {0} banned {1} for {2}.", senderName, user.getName(), banReason));
			}
		}
	}
}
