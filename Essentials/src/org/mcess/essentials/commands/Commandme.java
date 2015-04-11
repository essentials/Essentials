package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import static org.mcess.essentials.I18n.tl;
import org.mcess.essentials.User;
import org.mcess.essentials.utils.FormatUtil;
import org.bukkit.Server;


public class Commandme extends EssentialsCommand
{
	public Commandme()
	{
		super("me");
	}

	@Override
	public void run(Server server, User user, String commandLabel, String[] args) throws Exception
	{
		if (user.isMuted())
		{
			throw new Exception(tl("voiceSilenced"));
		}

		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		String message = getFinalArg(args, 0);
		message = FormatUtil.formatMessage(user, "essentials.chat", message);

		user.setDisplayNick();
		ess.broadcastMessage(user, tl("action", user.getDisplayName(), message));
	}

	@Override
	public void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		String message = getFinalArg(args, 0);
		message = FormatUtil.replaceFormat(message);

		ess.getServer().broadcastMessage(tl("action", "@", message));
	}
}
