package org.mcess.essentials.signs;

import org.mcess.essentials.User;
import java.util.List;
import net.ess3.api.IEssentials;
import org.mcess.essentials.I18n;


public class SignMail extends EssentialsSign
{
	public SignMail()
	{
		super("Mail");
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException
	{
		final List<String> mail = player.getMails();
		if (mail.isEmpty())
		{
			player.sendMessage(I18n.tl("noNewMail"));
			return false;
		}
		for (String s : mail)
		{
			player.sendMessage(s);
		}
		player.sendMessage(I18n.tl("markMailAsRead"));
		return true;
	}
}
