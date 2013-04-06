package net.ess3.signs.signs;

import java.util.List;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.signs.EssentialsSign;


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
			player.sendMessage(_("§6You have no new mail."));
			return false;
		}
		for (String s : mail)
		{
			player.sendMessage(s);
		}
		player.sendMessage(_("§6To mark your mail as read, type§c /mail clear."));
		return true;
	}
}
