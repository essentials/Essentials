package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.user.UserData.TimestampType;
import net.ess3.utils.DateUtil;
import org.bukkit.command.CommandSender;


public class Commandmute extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final IUser player = ess.getUserMap().matchUser(args[0], true);
		if (!player.getData().isMuted() && Permissions.MUTE_EXEMPT.isAuthorized(player))
		{
			throw new Exception(_("muteExempt"));
		}
		long muteTimestamp = 0;

		if (args.length > 1)
		{
			final String time = getFinalArg(args, 1);
			muteTimestamp = DateUtil.parseDateDiff(time, true);
			player.setMuted(true);
		}
		else
		{
			player.setMuted(!player.getData().isMuted());
		}
		player.setTimestamp(TimestampType.MUTE, muteTimestamp);
		final boolean muted = player.getData().isMuted();
		sender.sendMessage(
				muted ? (muteTimestamp > 0 ? _("mutedPlayerFor", player.getPlayer().getDisplayName(), DateUtil.formatDateDiff(muteTimestamp)) : _(
						 "mutedPlayer", player.getPlayer().getDisplayName())) : _(
				"unmutedPlayer", player.getPlayer().getDisplayName()));
		player.sendMessage(
				muted ? (muteTimestamp > 0 ? _("playerMutedFor", DateUtil.formatDateDiff(muteTimestamp)) : _("playerMuted")) : _("playerUnmuted"));
	}
}
