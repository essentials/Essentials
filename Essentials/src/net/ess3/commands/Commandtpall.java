package net.ess3.commands;

import static net.ess3.I18n._;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandtpall extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			if (isUser(sender))
			{
				teleportAllPlayers(sender, ess.getUserMap().getUser((Player)sender));
				return;
			}
			throw new NotEnoughArgumentsException();
		}

		final IUser player = ess.getUserMap().matchUserExcludingHidden(args[0], getPlayerOrNull(sender));
		teleportAllPlayers(sender, player);
	}

	private void teleportAllPlayers(CommandSender sender, IUser user)
	{
		sender.sendMessage(_("teleportAll"));
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final IUser player = ess.getUserMap().getUser(onlinePlayer);
			if (user == player)
			{
				continue;
			}

			ISettings settings = ess.getSettings();

			if (user.getPlayer().getWorld() != player.getPlayer().getWorld() && settings.getData().getGeneral().isWorldTeleportPermissions() && !Permissions.WORLD.isAuthorized(
					user, user.getPlayer().getWorld().getName()))
			{
				continue;
			}
			try
			{
				player.getTeleport().now(user.getPlayer(), false, TeleportCause.COMMAND);
			}
			catch (Exception ex)
			{
				ess.getCommandHandler().showCommandError(sender, commandName, ex);
			}

		}
	}
}