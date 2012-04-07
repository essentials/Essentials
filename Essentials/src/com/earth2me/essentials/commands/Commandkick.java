package com.earth2me.essentials.commands;

import com.earth2me.essentials.Console;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.api.server.ICommandSender;
import com.earth2me.essentials.api.server.Player;
import com.earth2me.essentials.permissions.Permissions;
import org.bukkit.entity.Player;


public class Commandkick extends EssentialsCommand
{
	@Override
	protected void run(final ICommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final IUser user = getPlayer(args, 0);
		if (Permissions.KICK_EXEMPT.isAuthorized(user))
		{
			throw new Exception(_("kickExempt"));
		}
		final String kickReason = args.length > 1 ? getFinalArg(args, 1) : _("kickDefault");
		user.kickPlayer(kickReason);
		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;

		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final IUser player = onlinePlayer.getUser();
			if (Permissions.KICK_NOTIFY.isAuthorized(player))
			{
				onlinePlayer.sendMessage(_("playerKicked", senderName, user.getName(), kickReason));
			}
		}
	}
}
