package net.ess3.commands;

import java.util.List;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


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

	private void feedOtherPlayers(final CommandSender sender, final String name)
	{
		final List<Player> players = server.matchPlayer(name);
		if (players.isEmpty())
		{
			sender.sendMessage(_("playerNotFound"));
			return;
		}
		for (Player player : players)
		{
			if (ess.getUser(player).isHidden())
			{
				continue;
			}
			player.setFoodLevel(20);
			player.setSaturation(10);
			sender.sendMessage(_("feedOther", player.getDisplayName()));
		}
	}
}
