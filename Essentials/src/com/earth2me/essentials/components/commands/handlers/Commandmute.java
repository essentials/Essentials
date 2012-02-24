package com.earth2me.essentials.components.commands.handlers;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.perm.Permissions;
import com.earth2me.essentials.components.users.UserData.TimestampType;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;


public class Commandmute extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		@Cleanup
		final IUser player = getPlayer(args, 0, true);
		player.acquireReadLock();
		if (!player.getData().isMuted() && Permissions.MUTE_EXEMPT.isAuthorized(player))
		{
			throw new Exception(_("muteExempt"));
		}
		long muteTimestamp = 0;
		if (args.length > 1)
		{
			String time = getFinalArg(args, 1);
			muteTimestamp = Util.parseDateDiff(time, true);
		}
		player.setTimestamp(TimestampType.MUTE, muteTimestamp);
		final boolean muted = player.toggleMuted();
		sender.sendMessage(
				muted
				? (muteTimestamp > 0
				   ? _("mutedPlayerFor", player.getDisplayName(), Util.formatDateDiff(muteTimestamp))
				   : _("mutedPlayer", player.getDisplayName()))
				: _("unmutedPlayer", player.getDisplayName()));
		player.sendMessage(
				muted
				? (muteTimestamp > 0
				   ? _("playerMutedFor", Util.formatDateDiff(muteTimestamp))
				   : _("playerMuted"))
				: _("playerUnmuted"));
	}
}
