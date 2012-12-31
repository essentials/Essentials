package net.ess3.commands;

import static net.ess3.I18n._;
import java.util.Set;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandfeed extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && Permissions.FEED_OTHERS.isAuthorized(user))
		{
			feedOtherPlayers(user, args[0]);
		}
		else
		{
			final Player player = user.getPlayer();
			player.setFoodLevel(20);
			player.setSaturation(10);
			user.sendMessage(_("feed"));
		}
	}

	private void feedOtherPlayers(final CommandSender sender, final String name)
	{
		final Set<IUser> users = ess.getUserMap().matchUsersExcludingHidden(name, getPlayerOrNull(sender));
		if (users.isEmpty())
		{
			sender.sendMessage(_("playerNotFound"));
			return;
		}
		for (IUser player : users)
		{
			final Player realPlayer = player.getPlayer();
			realPlayer.setFoodLevel(20);
			realPlayer.setSaturation(10);
			sender.sendMessage(_("feedOther", realPlayer.getDisplayName()));
		}
	}
}
