package net.ess3.commands;

import java.util.List;
import java.util.Set;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.api.server.CommandSender;
import net.ess3.api.server.Player;
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
			user.setFoodLevel(20);
			user.setSaturation(10);
			user.sendMessage(_("feed"));
		}
	}

	private void feedOtherPlayers(final CommandSender sender, final String name)
	{
		final Set<IUser> users = ess.getUserMap().matchUsers(name, false, true);
		if (users.isEmpty())
		{
			sender.sendMessage(_("playerNotFound"));
			return;
		}
		for (Player player : users)
		{			
			player.setFoodLevel(20);
			player.setSaturation(10);
			sender.sendMessage(_("feedOther", player.getDisplayName()));
		}
	}
}
