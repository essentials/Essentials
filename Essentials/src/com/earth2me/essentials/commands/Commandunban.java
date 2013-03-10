package com.earth2me.essentials.commands;

import com.earth2me.essentials.Console;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandunban extends EssentialsCommand
{
	public Commandunban()
	{
		super("unban");
	}

	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		String name;
		try
		{
			final User user = getPlayer(server, args, 0, true);
			name = user.getName();
			user.setBanned(false);
			user.setBanTimeout(0);
		}
		catch (NoSuchFieldException e)
		{
			final OfflinePlayer player = server.getOfflinePlayer(args[0]);
			name = player.getName();
			if (!player.isBanned())
			{
				throw new Exception(_("playerNotFound"), e);
			}
			player.setBanned(false);
		}
		
		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final User onlineUser = ess.getUser(onlinePlayer);
			if (onlinePlayer == sender || onlineUser.isAuthorized("essentials.ban.notify"))
			{
				onlinePlayer.sendMessage(_("playerUnbanned", senderName, name));
			}
		}
	}
}
