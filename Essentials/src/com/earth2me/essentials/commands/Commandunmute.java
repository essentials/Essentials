package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.User;
import org.bukkit.Server;

import static com.earth2me.essentials.I18n._;


public class Commandunmute extends EssentialsLoopCommand
{
	public Commandunmute()
	{
		super("unmute");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		if ((args[0].equals("*") || args[0].equals("**")) && user.isAuthorized("essentials.unmute.all"))
		{
			loopOfflinePlayers(server, user.getSource(), true, args[0], args);
		}
		else
		{
			User player = getPlayer(server, args, 0, true, true);
			unmutePlayer(user.getSource(), player, args);
		}
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		if ((args[0].equals("*") || args[0].equals("**")))
		{
			loopOfflinePlayers(server, sender, true, args[0], args);
		}
		else
		{
			User user = getPlayer(server, args, 0, true, true);
			unmutePlayer(sender, user, args);
		}
	}

	@Override
	protected void updatePlayer(final Server server, final CommandSource sender, final User user, final String[] args) throws PlayerExemptException
	{
		unmutePlayer(sender, user, args);
	}

	private void unmutePlayer(final CommandSource sender, final User user, final String[] args) throws PlayerExemptException
	{
		if (!user.isOnline())
		{
			if (sender.isPlayer() && !ess.getUser(sender.getPlayer()).isAuthorized("essentials.unmute.offline"))
			{
				throw new PlayerExemptException(_("unmuteExemptOffline"));
			}
		}

		user.setMuted(false);
		sender.sendMessage(_("unmutedPlayer", user.getDisplayName()));
		user.sendMessage(_("playerUnmuted"));
	}
}