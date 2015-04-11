package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import java.util.Locale;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


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
		if (args[1].toLowerCase(Locale.ENGLISH).startsWith("c:"))
		{
			if (user.isAuthorized("essentials.sudo.exempt") && sender.isPlayer())
			{
				throw new Exception(I18n.tl("sudoExempt"));
			}
			user.getBase().chat(getFinalArg(args, 1).substring(2));
			return;
		}
		final String[] arguments = new String[args.length - 1];
		if (arguments.length > 0)
		{
			System.arraycopy(args, 1, arguments, 0, args.length - 1);
		}

		if (user.isAuthorized("essentials.sudo.exempt") && sender.isPlayer())
		{
			throw new Exception(I18n.tl("sudoExempt"));
		}

		final String command = getFinalArg(arguments, 0);

		sender.sendMessage(I18n.tl("sudoRun", user.getDisplayName(), command, ""));

		if (command != null && command.length() > 0)
		{
			class SudoCommandTask implements Runnable
			{
				@Override
				public void run()
				{
					try
					{
						ess.getServer().dispatchCommand(user.getBase(), command);
					}
					catch (Exception e)
					{
						sender.sendMessage(I18n.tl("errorCallingCommand", command));
					}
				}
			}
			ess.scheduleSyncDelayedTask(new SudoCommandTask());
		}
	}
}
