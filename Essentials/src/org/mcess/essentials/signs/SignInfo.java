package org.mcess.essentials.signs;

import org.mcess.essentials.ChargeException;
import org.mcess.essentials.Trade;
import org.mcess.essentials.User;
import org.mcess.essentials.textreader.IText;
import org.mcess.essentials.textreader.KeywordReplacer;
import org.mcess.essentials.textreader.TextInput;
import org.mcess.essentials.textreader.TextPager;
import java.io.IOException;
import net.ess3.api.IEssentials;


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
			player.setDisplayNick();
			input = new TextInput(player.getSource(), "info", true, ess);
			final IText output = new KeywordReplacer(input, player.getSource(), ess);
			final TextPager pager = new TextPager(output);
			pager.showPage(chapter, page, null, player.getSource());

		}
		catch (IOException ex)
		{
			throw new SignException(ex.getMessage(), ex);
		}

		charge.charge(player);
		Trade.log("Sign", "Info", "Interact", username, null, username, charge, sign.getBlock().getLocation(), ess);
		return true;
	}
}
