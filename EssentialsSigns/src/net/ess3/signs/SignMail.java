package net.ess3.signs;

import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import java.util.List;


public class SignMail extends EssentialsSign
{
	public SignMail()
	{
		super("Mail");
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException
	{
		final List<String> mail = player.getData().getMails();
		if (mail == null || mail.isEmpty())
		{
			player.sendMessage(_("noNewMail"));
			return false;
		}
		for (String s : mail)
		{
			player.sendMessage(s);
		}
		player.sendMessage(_("markMailAsRead"));
		return true;
	}
}
