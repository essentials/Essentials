package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Trade;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NoChargeException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;


public class Commandback extends EssentialsCommand
{
	@Override
	protected void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		final Trade charge = new Trade(getCommandName(), getContext());
		charge.isAffordableFor(user);
		user.sendMessage($("backUsageMsg"));
		user.getTeleporter().back(charge);
		throw new NoChargeException();
	}
}
