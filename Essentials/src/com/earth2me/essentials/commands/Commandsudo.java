package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import static com.earth2me.essentials.I18n.tl;
import com.earth2me.essentials.User;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;


public class Commandsudo extends EssentialsCommand
{
	public Commandsudo()
	{
		super("sudo");
	}
	private static final Logger LOGGER = Logger.getLogger("Essentials");

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		final User user = getPlayer(server, sender, args, 0);
		if(args[1].toLowerCase(Locale.ENGLISH).startsWith("c:"))
		{
			if (user.isAuthorized("essentials.sudo.exempt") && sender.isPlayer())
			{
				throw new Exception(tl("sudoExempt"));
			}
			user.chat(getFinalArg(args, 1).substring(2));
			return;
		}
		final String command = args[1];
		final String[] arguments = new String[args.length - 2];
		if (arguments.length > 0)
		{
			System.arraycopy(args, 2, arguments, 0, args.length - 2);
		}

		if (user.isAuthorized("essentials.sudo.exempt") && sender.isPlayer())
		{
			throw new Exception(tl("sudoExempt"));
		}

		sender.sendMessage(tl("sudoRun", user.getDisplayName(), command, getFinalArg(arguments, 0)));

		final PluginCommand execCommand = ess.getServer().getPluginCommand(command);
		if (execCommand != null)
		{
			ess.scheduleSyncDelayedTask(
					new Runnable()
					{
						@Override
						public void run()
						{
							LOGGER.log(Level.INFO, String.format("[Sudo] %s issued server command: /%s %s", user.getName(), command, getFinalArg(arguments, 0)));
							execCommand.execute(user.getBase(), command, arguments);
						}
					});
		}
		else {
			sender.sendMessage(tl("errorCallingCommand", command));
		}
	}
}
