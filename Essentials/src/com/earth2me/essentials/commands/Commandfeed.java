package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandfeed extends EssentialsCommand
{
	public Commandfeed()
	{
		super("feed");
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && user.isAuthorized("essentials.feed.others"))
		{
			feedOtherPlayers(server, user, args[0]);
		}
		else
		{
			user.setFoodLevel(20);
			user.setSaturation(10);
			user.sendMessage(_("feed"));
		}
	}

	private void feedOtherPlayers(final Server server, final CommandSender sender, final String name) throws NotEnoughArgumentsException
	{
		User target = getPlayer(server, args, 0, (!sender instanceof player || sender.isAuthorized("essentials.vanish.interact")), false);
		target.setFoodLevel(20);
		target.setSaturation(10);
		sender.sendMessage(_("feedOther", target.getDisplayName()));
	}
}
