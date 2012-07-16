package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import lombok.Cleanup;
import org.bukkit.entity.Player;


public class Commandtpaall extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			if (sender instanceof Player)
			{
				teleportAAllPlayers(sender, ess.getUser((Player)sender));
				return;
			}
			throw new NotEnoughArgumentsException();
		}

		final IUser player = getPlayer(args, 0);
		teleportAAllPlayers(sender, player);
	}

	private void teleportAAllPlayers(final CommandSender sender, final IUser user)
	{
		sender.sendMessage(_("teleportAAll"));
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			@Cleanup
			final IUser player = ess.getUser(onlinePlayer);
			player.acquireReadLock();
			if (user == player)
			{
				continue;
			}
			if (!player.getData().isTeleportEnabled())
			{
				continue;
			}
			try
			{
				player.requestTeleport(user, true);
				player.sendMessage(_("teleportHereRequest", user.getDisplayName()));
				player.sendMessage(_("typeTpaccept"));
				int tpaAcceptCancellation = 0;
				ISettings settings = ess.getSettings();
				settings.acquireReadLock();
				try
				{
					tpaAcceptCancellation = settings.getData().getCommands().getTpa().getTimeout();
				}
				finally
				{
					settings.unlock();
				}
				if (tpaAcceptCancellation != 0)
				{
					player.sendMessage(_("teleportRequestTimeoutInfo", tpaAcceptCancellation));
				}
			}
			catch (Exception ex)
			{
				ess.getCommandHandler().showCommandError(sender, commandName, ex);
			}
		}
	}
}
