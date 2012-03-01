package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IUserComponent;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandtpaall extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			if (sender instanceof Player)
			{
				teleportAAllPlayers(sender, getContext().getUser((Player)sender));
				return;
			}
			throw new NotEnoughArgumentsException();
		}

		final IUserComponent player = getPlayer(args, 0);
		teleportAAllPlayers(sender, player);
	}

	private void teleportAAllPlayers(final CommandSender sender, final IUserComponent user)
	{
		sender.sendMessage(_("teleportAAll"));
		for (Player onlinePlayer : getServer().getOnlinePlayers())
		{
			@Cleanup
			final IUserComponent player = getContext().getUser(onlinePlayer);
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
				ISettingsComponent settings = getContext().getSettings();
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
				getContext().getCommands().showCommandError(sender, getCommandName(), ex);
			}
		}
	}
}
