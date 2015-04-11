package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.mcess.essentials.I18n;


public class Commandtpall extends EssentialsCommand
{
	public Commandtpall()
	{
		super("tpall");
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			if (sender.isPlayer())
			{
				teleportAllPlayers(server, sender, ess.getUser(sender.getPlayer()));
				return;
			}
			throw new NotEnoughArgumentsException();
		}

		final User target = getPlayer(server, sender, args, 0);
		teleportAllPlayers(server, sender, target);
	}

	private void teleportAllPlayers(Server server, CommandSource sender, User target)
	{
		sender.sendMessage(I18n.tl("teleportAll"));
		final Location loc = target.getLocation();
		for (User player : ess.getOnlineUsers())
		{
			if (target == player)
			{
				continue;
			}
			if (sender.equals(target.getBase())
				&& target.getWorld() != player.getWorld() && ess.getSettings().isWorldTeleportPermissions()
				&& !target.isAuthorized("essentials.worlds." + target.getWorld().getName()))
			{
				continue;
			}
			try
			{
				player.getTeleport().now(loc, false, TeleportCause.COMMAND);
			}
			catch (Exception ex)
			{
				ess.showError(sender, ex, getName());
			}
		}
	}
}
