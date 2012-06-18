package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;
import org.bukkit.entity.Player;


public class Commandvanish extends EssentialsCommand
{
	@Override
	protected void run(IUser user, String commandLabel, String[] args) throws Exception
	{
		user.toggleVanished();
		if (user.isVanished())
		{
			user.sendMessage(_("unvanished"));
		}
		else
		{
			user.sendMessage(_("unvanished"));
		}
	}
}
