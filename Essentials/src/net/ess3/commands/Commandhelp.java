package net.ess3.commands;

import static net.ess3.I18n._;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import net.ess3.api.IUser;
import net.ess3.utils.Util;
import net.ess3.utils.textreader.*;


public class Commandhelp extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		IText output;
		String pageStr = args.length > 0 ? args[0] : null;
		String chapterPageStr = args.length > 1 ? args[1] : null;
		String command = commandLabel;
		final IText input = new TextInput(user, "help", false, ess);

		if (input.getLines().isEmpty())
		{
			if (Util.isInt(pageStr) || pageStr == null)
			{
				output = new HelpInput(user, "", ess);
			}
			else
			{
				if (pageStr.length() > 26)
				{
					pageStr = pageStr.substring(0, 25);
				}
				output = new HelpInput(user, pageStr.toLowerCase(Locale.ENGLISH), ess);
				command = command.concat(" ").concat(pageStr);
				pageStr = chapterPageStr;
			}
			chapterPageStr = null;
		}
		else
		{
			output = new KeywordReplacer(input, user, ess);
		}
		final TextPager pager = new TextPager(output);
		pager.showPage(pageStr, chapterPageStr, command, user);
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		sender.sendMessage(_("helpConsole"));
	}
}
