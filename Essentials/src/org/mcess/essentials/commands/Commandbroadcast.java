package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import org.mcess.essentials.utils.FormatUtil;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandbroadcast extends EssentialsCommand
{
	public Commandbroadcast()
	{
		super("broadcast");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		sendBroadcast(user.getDisplayName(), args);
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		sendBroadcast(sender.getSender().getName(), args);
	}

	private void sendBroadcast(final String name, final String[] args) throws NotEnoughArgumentsException
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		ess.broadcastMessage(I18n.tl("broadcast", FormatUtil.replaceFormat(getFinalArg(args, 0)).replace("\\n", "\n"), name));
	}
}
