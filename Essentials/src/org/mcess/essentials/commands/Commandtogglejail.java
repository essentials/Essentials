package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import org.mcess.essentials.utils.DateUtil;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandtogglejail extends EssentialsCommand
{
	public Commandtogglejail()
	{
		super("togglejail");
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final User player = getPlayer(server, args, 0, true, true);

		if (args.length >= 2 && !player.isJailed())
		{
			if (!player.getBase().isOnline())
			{
				if (sender.isPlayer()
					&& !ess.getUser(sender.getPlayer()).isAuthorized("essentials.togglejail.offline"))
				{
					sender.sendMessage(I18n.tl("mayNotJailOffline"));
					return;
				}
			}
			else
			{
				if (player.isAuthorized("essentials.jail.exempt"))
				{
					sender.sendMessage(I18n.tl("mayNotJail"));
					return;
				}
			}
			if (player.getBase().isOnline())
			{
				ess.getJails().sendToJail(player, args[1]);
			}
			else
			{
				// Check if jail exists
				ess.getJails().getJail(args[1]);
			}
			player.setJailed(true);
			player.sendMessage(I18n.tl("userJailed"));
			player.setJail(null);
			player.setJail(args[1]);
			long timeDiff = 0;
			if (args.length > 2)
			{
				final String time = getFinalArg(args, 2);
				timeDiff = DateUtil.parseDateDiff(time, true);
				player.setJailTimeout(timeDiff);
			}
			sender.sendMessage((timeDiff > 0
								? I18n.tl("playerJailedFor", player.getName(), DateUtil.formatDateDiff(timeDiff))
								: I18n.tl("playerJailed", player.getName())));
			return;
		}

		if (args.length >= 2 && player.isJailed() && !args[1].equalsIgnoreCase(player.getJail()))
		{
			sender.sendMessage(I18n.tl("jailAlreadyIncarcerated", player.getJail()));
			return;
		}

		if (args.length >= 2 && player.isJailed() && args[1].equalsIgnoreCase(player.getJail()))
		{
			final String time = getFinalArg(args, 2);
			final long timeDiff = DateUtil.parseDateDiff(time, true);
			player.setJailTimeout(timeDiff);
			sender.sendMessage(I18n.tl("jailSentenceExtended", DateUtil.formatDateDiff(timeDiff)));
			return;
		}

		if (args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase(player.getJail())))
		{
			if (!player.isJailed())
			{
				throw new NotEnoughArgumentsException();
			}
			player.setJailed(false);
			player.setJailTimeout(0);
			player.sendMessage(I18n.tl("jailReleasedPlayerNotify"));
			player.setJail(null);
			if (player.getBase().isOnline())
			{
				player.getTeleport().back();
			}
			sender.sendMessage(I18n.tl("jailReleased", player.getName()));
		}
	}
}
