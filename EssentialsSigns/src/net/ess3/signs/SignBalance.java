package net.ess3.signs;

import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.utils.FormatUtil;


public class SignBalance extends EssentialsSign
{
	public SignBalance()
	{
		super("Balance");
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException
	{
		player.sendMessage(_("balance", FormatUtil.displayCurrency(player.getMoney(), ess)));
		return true;
	}
}
