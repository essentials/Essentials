package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n.tl_;
import com.earth2me.essentials.User;
import org.bukkit.Server;


public class Commandtpahere extends EssentialsCommand
{
	public Commandtpahere()
	{
		super("tpahere");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final User player = getPlayer(server, user, args, 0);
		if (user.getName().equalsIgnoreCase(player.getName()))
		{
			throw new NotEnoughArgumentsException();
		}
		if (!player.isTeleportEnabled())
		{
			throw new Exception(tl_("teleportDisabled", player.getDisplayName()));
		}
		if (user.getWorld() != player.getWorld() && ess.getSettings().isWorldTeleportPermissions()
			&& !user.isAuthorized("essentials.worlds." + user.getWorld().getName()))
		{
			throw new Exception(tl_("noPerm", "essentials.worlds." + user.getWorld().getName()));
		}
		if (!player.isIgnoredPlayer(user))
		{
			player.requestTeleport(user, true);
			player.sendMessage(tl_("teleportHereRequest", user.getDisplayName()));
			player.sendMessage(tl_("typeTpaccept"));
			player.sendMessage(tl_("typeTpdeny"));
			if (ess.getSettings().getTpaAcceptCancellation() != 0)
			{
				player.sendMessage(tl_("teleportRequestTimeoutInfo", ess.getSettings().getTpaAcceptCancellation()));
			}
		}
		user.sendMessage(tl_("requestSent", player.getDisplayName()));
	}
}
