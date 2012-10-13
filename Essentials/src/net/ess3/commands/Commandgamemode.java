package net.ess3.commands;

import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;


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

		user.getPlayer().setGameMode(user.getPlayer().getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL);
		user.sendMessage(_("gameMode", _(user.getPlayer().getGameMode().toString().toLowerCase(Locale.ENGLISH)), user.getPlayer().getDisplayName()));
	}

	private void gamemodeOtherPlayers(final CommandSender sender, final String args[])
	{
		for (IUser player : ess.getUserMap().matchUsersExcludingHidden(args[0], getPlayerOrNull(sender)))
		{		
			if (args.length > 1)
			{
				if (args[1].contains("creat") || args[1].equalsIgnoreCase("1"))
				{
					player.getPlayer().setGameMode(GameMode.CREATIVE);
				}
				else
				{
					player.getPlayer().setGameMode(GameMode.SURVIVAL);
				}
			}
			else
			{
				player.getPlayer().setGameMode(player.getPlayer().getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL);
			}
			sender.sendMessage(_("gameMode", _(player.getPlayer().getGameMode().toString().toLowerCase(Locale.ENGLISH)), player.getPlayer().getDisplayName()));
		}
	}
}
