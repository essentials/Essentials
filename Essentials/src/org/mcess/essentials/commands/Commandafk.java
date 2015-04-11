package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandafk extends EssentialsCommand
{
	public Commandafk()
	{
		super("afk");
	}

	@Override
	public void run(Server server, User user, String commandLabel, String[] args) throws Exception
	{
		if (args.length > 0 && user.isAuthorized("essentials.afk.others"))
		{
			User afkUser = getPlayer(server, user, args, 0);
			toggleAfk(afkUser);
		}
		else
		{
			toggleAfk(user);
		}
	}
	
	@Override
	public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception
	{
		if (args.length > 0)
		{
			User afkUser = getPlayer(server, args, 0, true, false);
			toggleAfk(afkUser);
		}
		else
		{
			throw new NotEnoughArgumentsException();
		}
	}

	private void toggleAfk(User user)
	{
		user.setDisplayNick();
		String msg = "";
		if (!user.toggleAfk())
		{
			//user.sendMessage(_("markedAsNotAway"));
			if (!user.isHidden())
			{
				msg = I18n.tl("userIsNotAway", user.getDisplayName());
			}
			user.updateActivity(false);
		}
		else
		{
			//user.sendMessage(_("markedAsAway"));
			if (!user.isHidden())
			{
				msg = I18n.tl("userIsAway", user.getDisplayName());
			}
		}
		if (!msg.isEmpty())
		{
			ess.broadcastMessage(user, msg);
		}
	}
}

