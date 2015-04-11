package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.utils.FormatUtil;
import org.bukkit.Server;
import org.mcess.essentials.I18n;

// This command can be used to echo messages to the users screen, mostly useless but also an #EasterEgg
public class Commandping extends EssentialsCommand
{
	public Commandping()
	{
		super("ping");
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{

			sender.sendMessage(I18n.tl("pong"));
		}
		else
		{
			sender.sendMessage(FormatUtil.replaceFormat(getFinalArg(args, 0)));
		}
	}
}
