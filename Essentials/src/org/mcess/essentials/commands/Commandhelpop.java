package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.Console;
import org.mcess.essentials.User;
import org.mcess.essentials.utils.FormatUtil;
import java.util.logging.Level;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandhelpop extends EssentialsCommand
{
	public Commandhelpop()
	{
		super("helpop");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		user.setDisplayNick();
		final String message = sendMessage(server, user.getSource(), user.getDisplayName(), args);
		if (!user.isAuthorized("essentials.helpop.receive"))
		{
			user.sendMessage(message);
		}
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		sendMessage(server, sender, Console.NAME, args);
	}

	private String sendMessage(final Server server, final CommandSource sender, final String from, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		final String message = I18n.tl("helpOp", from, FormatUtil.stripFormat(getFinalArg(args, 0)));
		server.getLogger().log(Level.INFO, message);
		ess.broadcastMessage("essentials.helpop.receive", message);
		return message;
	}
}
