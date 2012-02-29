package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.users.TimeStampType;
import com.earth2me.essentials.perm.Permissions;
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
		final IUserComponent player = getPlayer(args, 0, true);
		player.acquireReadLock();
		if (!player.getData().isMuted() && Permissions.MUTE_EXEMPT.isAuthorized(player))
		{
			throw new Exception($("muteExempt"));
		}
		long muteTimestamp = 0;
		if (args.length > 1)
		{
			String time = getFinalArg(args, 1);
			muteTimestamp = Util.parseDateDiff(time, true);
		}
		player.setTimeStamp(TimeStampType.MUTE, muteTimestamp);
		final boolean muted = player.toggleMuted();
		sender.sendMessage(
				muted
				? (muteTimestamp > 0
				   ? $("mutedPlayerFor", player.getDisplayName(), Util.formatDateDiff(muteTimestamp))
				   : $("mutedPlayer", player.getDisplayName()))
				: $("unmutedPlayer", player.getDisplayName()));
		player.sendMessage(
				muted
				? (muteTimestamp > 0
				   ? $("playerMutedFor", Util.formatDateDiff(muteTimestamp))
				   : $("playerMuted"))
				: $("playerUnmuted"));
	}
}
