package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.Console;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.perm.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandkick extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final IUserComponent user = getPlayer(args, 0);
		if (Permissions.KICK_EXEMPT.isAuthorized(user))
		{
			throw new Exception($("kickExempt"));
		}
		final String kickReason = args.length > 1 ? getFinalArg(args, 1) : $("kickDefault");
		user.kickPlayer(kickReason);
		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;

		for (Player onlinePlayer : getServer().getOnlinePlayers())
		{
			final IUserComponent player = getContext().getUser(onlinePlayer);
			if (Permissions.KICK_NOTIFY.isAuthorized(player))
			{
				onlinePlayer.sendMessage($("playerKicked", senderName, user.getName(), kickReason));
			}
		}
	}
}
