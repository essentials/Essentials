package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.utils.Util;


public class Commandping extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			user.sendMessage(_("pong"));
		}
		else
		{
			user.sendMessage(Util.replaceFormat(getFinalArg(args, 0)));
		}
	}
}
