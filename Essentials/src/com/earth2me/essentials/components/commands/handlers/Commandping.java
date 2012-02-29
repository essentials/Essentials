package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;


public class Commandping extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			user.sendMessage($("pong"));
		}
		else
		{
			user.sendMessage(Util.replaceColor(getFinalArg(args, 0)));
		}
	}
}
