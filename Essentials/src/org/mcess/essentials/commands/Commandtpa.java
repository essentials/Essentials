package org.mcess.essentials.commands;

import org.mcess.essentials.User;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandtpa extends EssentialsCommand
{
	public Commandtpa()
	{
		super("tpa");
	}

	@Override
	public void run(Server server, User user, String commandLabel, String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		User player = getPlayer(server, user, args, 0);
		if (user.getName().equalsIgnoreCase(player.getName()))
		{
			throw new NotEnoughArgumentsException();
		}
		if (!player.isTeleportEnabled())
		{
			throw new Exception(I18n.tl("teleportDisabled", player.getDisplayName()));
		}
		if (user.getWorld() != player.getWorld() && ess.getSettings().isWorldTeleportPermissions()
			&& !user.isAuthorized("essentials.worlds." + player.getWorld().getName()))
		{
			throw new Exception(I18n.tl("noPerm", "essentials.worlds." + player.getWorld().getName()));
		}
		if (!player.isIgnoredPlayer(user))
		{
			player.requestTeleport(user, false);
			player.sendMessage(I18n.tl("teleportRequest", user.getDisplayName()));
			player.sendMessage(I18n.tl("typeTpaccept"));
			player.sendMessage(I18n.tl("typeTpdeny"));
			if (ess.getSettings().getTpaAcceptCancellation() != 0)
			{
				player.sendMessage(I18n.tl("teleportRequestTimeoutInfo", ess.getSettings().getTpaAcceptCancellation()));
			}
		}
		user.sendMessage(I18n.tl("requestSent", player.getDisplayName()));
	}
}
