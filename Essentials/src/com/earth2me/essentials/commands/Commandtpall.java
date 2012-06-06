package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.WorldPermissions;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtpall extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
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