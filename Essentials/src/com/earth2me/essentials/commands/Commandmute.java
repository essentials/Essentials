package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.DateUtil;
import org.bukkit.Server;

import static com.earth2me.essentials.I18n._;


public class Commandmute extends EssentialsLoopCommand
{
	public Commandmute()
	{
		super("mute");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		if ((args[0].equalsIgnoreCase("*") || args[0].equalsIgnoreCase("**")) && user.isAuthorized("essentials.mute.all"))
		{
			loopOfflinePlayers(server, user.getSource(), true, args[0], args);
		}
		else
		{
			long muteTimestamp = 0;
			if (args.length > 1)
			{
				final String time = getFinalArg(args, 1);
				muteTimestamp = DateUtil.parseDateDiff(time, true);
			}

			User player = getPlayer(server, args, 0, true, true);
			mutePlayer(user.getSource(), player, args, muteTimestamp);
		}
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		if ((args[0].equalsIgnoreCase("*") || args[0].equalsIgnoreCase("**")))
		{
			loopOfflinePlayers(server, sender, true, args[0], args);
		}
		else
		{
			long muteTimestamp = 0;
			if (args.length > 1)
			{
				final String time = getFinalArg(args, 1);
				muteTimestamp = DateUtil.parseDateDiff(time, true);
			}

			User player = getPlayer(server, args, 0, true, true);
			mutePlayer(sender, player, args, muteTimestamp);
		}
	}

	@Override
	protected void updatePlayer(final Server server, final CommandSource sender, final User user, final String[] args) throws PlayerExemptException
	{
		long muteTimestamp = 0;
		if (args.length > 1)
		{
			final String time = getFinalArg(args, 1);
			muteTimestamp = DateUtil.parseDateDiff(time, true);
		}

		mutePlayer(sender, user, args, muteTimestamp);
	}

	private void mutePlayer(final CommandSource sender, final User user, final String[] args, long muteTimestamp) throws PlayerExemptException
	{
		if (!user.isOnline())
		{
			if (sender.isPlayer() && !ess.getUser(sender.getPlayer()).isAuthorized("essentials.mute.offline"))
			{
				throw new PlayerExemptException(_("muteExemptOffline"));
			}
		}
		else
		{
			if (user.isAuthorized("essentials.mute.exempt") && sender.isPlayer())
			{
				throw new PlayerExemptException(_("muteExempt"));
			}
		}

		user.setMuted(true);
		user.setMuteTimeout(muteTimestamp);
		String muteTime = DateUtil.formatDateDiff(muteTimestamp);

		if (muteTimestamp > 0)
		{
			sender.sendMessage(_("mutedPlayerFor", user.getDisplayName(), muteTime));
			user.sendMessage(_("playerMutedFor", muteTime));
		}
		else
		{
			sender.sendMessage(_("mutedPlayer", user.getDisplayName()));
			user.sendMessage(_("playerMuted"));
		}

		ess.broadcastMessage("essentials.mute.notify", _("muteNotify", sender.getSender().getName(), user.getName(), muteTime));
	}
}
