package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.components.users.IUser;
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
				teleportAllPlayers(sender, getContext().getUser((Player)sender));
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
		for (Player onlinePlayer : getServer().getOnlinePlayers())
		{
			final IUser player = getContext().getUser(onlinePlayer);
			if (user == player)
			{
				continue;
			}
			try
			{
				player.getTeleport().now(user, false, TeleportCause.COMMAND);
			}
			catch (Exception ex)
			{
				getContext().getCommands().showCommandError(sender, getCommandName(), ex);
			}
		}
	}
}
