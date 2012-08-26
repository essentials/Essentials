package net.ess3.commands;

import java.util.Set;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.command.CommandSender;


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
			user.getPlayer().setFoodLevel(20);
			user.getPlayer().setSaturation(10);
			user.sendMessage(_("feed"));
		}
	}

	private void feedOtherPlayers(final CommandSender sender, final String name)
	{
		final Set<IUser> users = ess.getUserMap().matchUsers(name, false, false);
		if (users.isEmpty())
		{
			sender.sendMessage(_("playerNotFound"));
			return;
		}
		for (IUser player : users)
		{			
			player.getPlayer().setFoodLevel(20);
			player.getPlayer().setSaturation(10);
			sender.sendMessage(_("feedOther", player.getPlayer().getDisplayName()));
		}
	}
}
