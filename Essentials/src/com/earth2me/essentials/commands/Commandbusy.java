package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import org.bukkit.command.CommandSender;
import org.bukkit.Server;


public class Commandbusy extends EssentialsCommand
{
	public Commandbusy()
	{
		super("busy");
	}

	@Override
	public void run(Server server, User user, String commandLabel, String[] args) throws Exception
	{
		if (args.length > 0 && user.isAuthorized("essentials.busy.others"))
		{
			User busyUser = getPlayer(server, user, args, 0);
			toggleBusy(busyUser);
		}
		else
		{
			toggleBusy(user);
		}
	}

	@Override
	public void run(Server server, CommandSender sender, String commandLabel, String[] args) throws Exception
	{
		if (args.length > 0)
		{
			User busyUser = getPlayer(server, args, 0, true, false);
			toggleBusy(busyUser);
		}
		else
		{
			throw new NotEnoughArgumentsException();
		}
	}

	private void toggleBusy(User user)
	{
		user.setDisplayNick();
		String msg = "";
		if (!user.toggleBusy())
		{
			//user.sendMessage(_("markedAsNotAway"));
			if (!user.isHidden())
			{
				msg = _("userIsBusy", user.getDisplayName());
			}
			user.updateActivity(false);
		}
		else
		{
			//user.sendMessage(_("markedAsAway"));
			if (user.isHidden())
			{
				msg = _("userIsAway", user.getDisplayName());
			}
		}
		if (!msg.isEmpty())
		{
			ess.broadcastMessage(user, msg);
		}
	}
}
