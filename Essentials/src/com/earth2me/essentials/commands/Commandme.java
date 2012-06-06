package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.utils.Util;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;


public class Commandme extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (user.getData().isMuted())
		{
			throw new Exception(_("voiceSilenced"));
		}

		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		String message = getFinalArg(args, 0);
		if (Permissions.CHAT_COLOR.isAuthorized(user))
		{
			message = Util.replaceFormat(message);
		}
		else {
			message = Util.stripColor(message);
		}
		

		user.setDisplayNick();
		ess.broadcastMessage(user, _("action", user.getDisplayName(), message));
	}
}
