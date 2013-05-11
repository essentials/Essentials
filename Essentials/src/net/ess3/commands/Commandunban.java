package net.ess3.commands;

import net.ess3.Console;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandunban extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}


		final IUser player = ess.getUserMap().matchUser(args[0], true);
		player.getData().setBan(null);
		player.setBanned(false);
		player.getData().getBan().setTimeout(0);
		player.queueSave();
		sender.sendMessage(_("Unbanned player."));
		
		final String senderName = isUser(sender) ? getPlayer(sender).getDisplayName() : Console.NAME;

		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final IUser user = ess.getUserMap().getUser(onlinePlayer);
			if (Permissions.UNBAN_NOTIFY.isAuthorized(user))
			{
				onlinePlayer.sendMessage(_("§c{0} §6unbanned §c{1}", senderName, player.getName()));
			}
		}
	}
}
