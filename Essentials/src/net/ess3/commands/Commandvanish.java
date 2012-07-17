package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;


public class Commandvanish extends EssentialsCommand
{
	@Override
	protected void run(IUser user, String commandLabel, String[] args) throws Exception
	{
		if (args.length < 1)
		{
			user.toggleVanished();
			if (user.isVanished())
			{
				user.sendMessage(_("vanished"));
			}
			else
			{
				user.sendMessage(_("unvanished"));
			}
		}
		else
		{
			if (args[0].contains("on") || args[0].contains("ena") || args[0].equalsIgnoreCase("1"))
			{
				user.setVanished(true);
			}
			else
			{
				user.setVanished(false);
			}
			user.sendMessage(user.isVanished() ? _("vanished") : _("unvanished"));
		}
	}		
}
