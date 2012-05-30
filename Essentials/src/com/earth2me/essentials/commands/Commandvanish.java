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
		if (user.isVanished())
		{
			for (Player p : server.getOnlinePlayers())
			{
				p.showPlayer(user);
			}
			user.sendMessage(_("vanished"));
		}
		else
		{
			for (Player p : server.getOnlinePlayers())
			{
				if (!Permissions.VANISH_SEE_OTHERS.isAuthorized(p))
				{
					p.hidePlayer(user);
				}
				user.sendMessage(_("unvanished"));
			}
		}
		user.toggleVanished();
	}
}
