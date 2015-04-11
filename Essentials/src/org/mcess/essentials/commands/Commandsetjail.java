package org.mcess.essentials.commands;

import org.mcess.essentials.User;
import org.mcess.essentials.utils.StringUtil;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandsetjail extends EssentialsCommand
{
	public Commandsetjail()
	{
		super("setjail");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		ess.getJails().setJail(args[0], user.getLocation());
		user.sendMessage(I18n.tl("jailSet", StringUtil.sanitizeString(args[0])));

	}
}
