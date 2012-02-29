package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.perm.Permissions;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandfeed extends EssentialsCommand
{
	@Override
	protected void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && Permissions.FEED_OTHERS.isAuthorized(user))
		{
			feedOtherPlayers(user, args[0]);
		}
		else
		{
			user.setFoodLevel(20);
			user.setSaturation(10);
			user.sendMessage($("feed"));
		}
	}

	private void feedOtherPlayers(final CommandSender sender, final String name)
	{
		final List<Player> players = getServer().matchPlayer(name);
		if (players.isEmpty())
		{
			sender.sendMessage($("playerNotFound"));
			return;
		}
		for (Player player : players)
		{
			if (getContext().getUser(player).isHidden())
			{
				continue;
			}
			player.setFoodLevel(20);
			player.setSaturation(10);
			sender.sendMessage($("feedOther", player.getDisplayName()));
		}
	}
}
