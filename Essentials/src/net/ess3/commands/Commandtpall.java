package net.ess3.commands;

import lombok.Cleanup;
import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.WorldPermissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtpall extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			if (sender instanceof Player)
			{
				teleportAllPlayers(sender, ess.getUser((Player)sender));
				return;
			}
			throw new NotEnoughArgumentsException();
		}

		final IUser player = getPlayer(args, 0);
		teleportAllPlayers(sender, player);
	}

	private void teleportAllPlayers(CommandSender sender, IUser user)
	{
		sender.sendMessage(_("teleportAll"));
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final IUser player = ess.getUser(onlinePlayer);
			if (user == player)
			{
				continue;
			}
			@Cleanup
			ISettings settings = ess.getSettings();
			settings.acquireReadLock();

			if (user.getWorld() != player.getWorld() && settings.getData().getGeneral().isWorldTeleportPermissions()
				&& !WorldPermissions.getPermission(user.getWorld().getName()).isAuthorized(user))
			{
				continue;
			}
			try
			{
				player.getTeleport().now(user, false, TeleportCause.COMMAND);
			}
			catch (Exception ex)
			{
				ess.getCommandHandler().showCommandError(sender, commandName, ex);
			}

		}
	}
}