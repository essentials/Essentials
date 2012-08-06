package net.ess3.signs;


import java.io.IOException;
import net.ess3.api.ChargeException;
import net.ess3.api.IEssentials;
import net.ess3.economy.Trade;
import net.ess3.user.User;
import net.ess3.utils.textreader.IText;
import net.ess3.utils.textreader.KeywordReplacer;
import net.ess3.utils.textreader.TextInput;
import net.ess3.utils.textreader.TextPager;


public class SignInfo extends EssentialsSign
{
	public SignInfo()
	{
		super("Info");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException
	{
		validateTrade(sign, 3, ess);
		return true;
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException
	{
		final Trade charge = getTrade(sign, 3, ess);
		charge.isAffordableFor(player);

		String chapter = sign.getLine(1);
		String page = sign.getLine(2);

		final IText input;
		try
		{
			input = new TextInput(player, "info", true, ess);
			final IText output = new KeywordReplacer(input, player, ess);
			final TextPager pager = new TextPager(output);
			pager.showPage(chapter, page, null, player);

		}
		catch (IOException ex)
		{
			throw new SignException(ex.getMessage(), ex);
		}

		charge.charge(player);
		return true;
	}
}



