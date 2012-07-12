package net.ess3.commands;

import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandgamemode extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		gamemodeOtherPlayers(sender, args);
	}

	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && !args[0].trim().isEmpty() && Permissions.GAMEMODE_OTHERS.isAuthorized(user))
		{
			gamemodeOtherPlayers(user, args);
			return;
		}

		user.setGameMode(user.getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL);
		user.sendMessage(_("gameMode", _(user.getGameMode().toString().toLowerCase(Locale.ENGLISH)), user.getDisplayName()));
	}

	private void gamemodeOtherPlayers(final CommandSender sender, final String args[])
	{
		for (Player matchPlayer : server.matchPlayer(args[0]))
		{
			final IUser player = ess.getUser(matchPlayer);
			if (player.isHidden())
			{
				continue;
			}

			if (args.length > 1)
			{
				if (args[1].contains("creat") || args[1].equalsIgnoreCase("1"))
				{
					player.setGameMode(GameMode.CREATIVE);
				}
				else
				{
					player.setGameMode(GameMode.SURVIVAL);
				}
			}
			else
			{
				player.setGameMode(player.getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL);
			}
			sender.sendMessage(_("gameMode", _(player.getGameMode().toString().toLowerCase(Locale.ENGLISH)), player.getDisplayName()));
		}
	}
}
