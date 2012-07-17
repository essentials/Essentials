package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import java.util.List;
import net.ess3.api.server.Player;


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
			user.setFoodLevel(20);
			user.setSaturation(10);
			user.sendMessage(_("feed"));
		}
	}

	private void feedOtherPlayers(final ICommandSender sender, final String name)
			// shouldn't this be CommandSender? ^
	{
		final List<Player> players = server.matchPlayer(name);
		if (players.isEmpty())
		{
			sender.sendMessage(_("playerNotFound"));
			return;
		}
		for (Player player : players)
		{
			if (player.getUser(player).isHidden())
			{
				continue;
			}
			player.setFoodLevel(20);
			player.setSaturation(10);
			sender.sendMessage(_("feedOther", player.getDisplayName()));
		}
	}
}
