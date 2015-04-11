package org.mcess.essentials.signs;

import org.mcess.essentials.User;
import org.mcess.essentials.utils.NumberUtil;
import net.ess3.api.IEssentials;
import org.mcess.essentials.I18n;


public class SignBalance extends EssentialsSign
{
	public SignBalance()
	{
		super("Balance");
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException
	{
		player.sendMessage(I18n.tl("balance", NumberUtil.displayCurrency(player.getMoney(), ess)));
		return true;
	}
}
