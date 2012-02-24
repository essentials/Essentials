package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IUser;


public class Commandsocialspy extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		user.sendMessage("§7SocialSpy " + (user.toggleSocialSpy() ? _("enabled") : _("disabled")));
	}
}
