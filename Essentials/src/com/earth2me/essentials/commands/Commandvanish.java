package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IUser;


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
