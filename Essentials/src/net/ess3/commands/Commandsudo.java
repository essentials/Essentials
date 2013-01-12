package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;


public class Commandsudo extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		final IUser user = ess.getUserMap().matchUserExcludingHidden(args[0], getPlayerOrNull(sender));
		final String command = args[1];
		final String[] arguments = new String[args.length - 2];
		if (arguments.length > 0)
		{
			System.arraycopy(args, 2, arguments, 0, args.length - 2);
		}

		if (Permissions.SUDO_EXEMPT.isAuthorized(user))
		{
			throw new Exception(_("sudoExempt"));
		}

		final Player player = user.getPlayer();
		sender.sendMessage(_("sudoRun", player.getDisplayName(), command, getFinalArg(arguments, 0)));

		final Server server = ess.getServer();
		server.dispatchCommand(user, command);
		final PluginCommand execCommand = server.getPluginCommand(command);
		if (execCommand != null)
		{
			execCommand.execute(player, command, arguments);
		}

	}
}
