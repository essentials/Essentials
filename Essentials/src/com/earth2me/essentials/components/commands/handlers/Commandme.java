package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.perm.Permissions;


public class Commandme extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (user.getData().isMuted())
		{
			throw new Exception($("voiceSilenced"));
		}

		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		String message = getFinalArg(args, 0);
		if (Permissions.CHAT_COLOR.isAuthorized(user))
		{
			message = Util.replaceColor(message);
		}
		else {
			message = Util.stripColor(message);
		}


		getContext().getMessager().broadcastMessage(user, $("action", user.getDisplayName(), message));
	}
}
