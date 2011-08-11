package com.earth2me.essentials.commands;

import com.earth2me.essentials.OfflinePlayer;
import org.bukkit.Server;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;


public class Commandtpo extends EssentialsCommand
{
	public Commandtpo()
	{
		super("tpo");
	}

	@Override
	public void run(Server server, User user, String commandLabel, String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		//Just basically the old tp command
		User p = getPlayer(server, args, 0, true);
		// Check if user is offline
		if (p.getBase() instanceof OfflinePlayer)
		{
			throw new NoSuchFieldException(Util.i18n("playerNotFound"));
		}

		// Verify permission
		if (!p.isHidden() || user.isAuthorized("essentials.teleport.hidden"))
		{
			charge(user);
			user.getTeleport().now(p, false);
			user.sendMessage(Util.i18n("teleporting"));
		}
		else
		{
			throw new NoSuchFieldException(Util.i18n("playerNotFound"));
		}
	}
}
