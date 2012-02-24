package com.earth2me.essentials.signs;

import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.users.IUser;


public class SignBalance extends EssentialsSign
{
	public SignBalance()
	{
		super("Balance");
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUser player, final String username, final IContext ess) throws SignException
	{
		player.sendMessage(_("balance", player.getMoney()));
		return true;
	}
}
