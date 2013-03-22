package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandmute extends EssentialsCommand
{
	public Commandmute()
	{
		super("mute");
	}

	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final User target = getPlayer(server, sender, args, 0);
		if (sender instanceof Player && !target.isMuted() && target.isAuthorized("essentials.mute.exempt"))
		{
			throw new Exception(_("muteExempt"));
		}
		long muteTimestamp = 0;

		if (args.length > 1)
		{
			final String time = getFinalArg(args, 1);
			muteTimestamp = Util.parseDateDiff(time, true);
			target.setMuted(true);
		}
		else
		{
			target.setMuted(!target.getMuted());
		}
		target.setMuteTimeout(muteTimestamp);
		final boolean muted = target.getMuted();
		if (muted)
		{
			if (muteTimestamp > 0)
			{
				sender.sendMessage(_("mutedPlayerFor", target.getDisplayName(), Util.formatDateDiff(muteTimestamp)));
				target.sendMessage(_("playerMutedFor", Util.formatDateDiff(muteTimestamp)));
			}
			else
			{
				sender.sendMessage(_("mutedPlayer", target.getDisplayName()));
				target.sendMessage(_("playerMuted"));
			}
			for (Player onlinePlayer : server.getOnlinePlayers())
			{
				final User user = ess.getUser(onlinePlayer);
				if (onlinePlayer != sender && user.isAuthorized("essentials.mute.notify"))
				{
					onlinePlayer.sendMessage(_("muteNotify", sender.getName(), target.getName()));
				}
			}
		}
		else
		{
			sender.sendMessage(_("unmutedPlayer", target.getDisplayName()));
			target.sendMessage(_("playerUnmuted"));
		}
	}
}
