package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.StringUtil;
import org.bukkit.Server;


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

		if (ess.getJails().getList().contains(args[0]))
		{
			ess.getJails().setJail(args[0], user.getLocation());
			user.sendMessage(_("jailSet", StringUtil.sanitizeString(args[0])));
		}
		else
		{
			user.sendMessage(_("jailNotExist"));
		}
	}
}
