package com.earth2me.essentials.components.commands.handlers;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NoChargeException;
import com.earth2me.essentials.components.users.IUser;


public class Commandback extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final Trade charge = new Trade(getCommandName(), getContext());
		charge.isAffordableFor(user);
		user.sendMessage(_("backUsageMsg"));
		user.getTeleport().back(charge);
		throw new NoChargeException();
	}
}
