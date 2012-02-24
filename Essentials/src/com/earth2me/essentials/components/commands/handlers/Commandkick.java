package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Console;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.I18nComponent._;
import com.earth2me.essentials.components.users.IUser;
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

		final IUser user = getPlayer(args, 0);
		if (Permissions.KICK_EXEMPT.isAuthorized(user))
		{
			throw new Exception(_("kickExempt"));
		}
		final String kickReason = args.length > 1 ? getFinalArg(args, 1) : _("kickDefault");
		user.kickPlayer(kickReason);
		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;

		for (Player onlinePlayer : getServer().getOnlinePlayers())
		{
			final IUser player = getContext().getUser(onlinePlayer);
			if (Permissions.KICK_NOTIFY.isAuthorized(player))
			{
				onlinePlayer.sendMessage(_("playerKicked", senderName, user.getName(), kickReason));
			}
		}
	}
}
