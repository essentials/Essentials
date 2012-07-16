package net.ess3.commands;

import net.ess3.utils.textreader.TextInput;
import net.ess3.utils.textreader.IText;
import net.ess3.utils.textreader.TextPager;
import net.ess3.utils.textreader.HelpInput;
import net.ess3.utils.textreader.KeywordReplacer;
import static net.ess3.I18n._;
import net.ess3.utils.Util;
import net.ess3.api.IUser;


public class Commandhelp extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		IText output;
		String pageStr = args.length > 0 ? args[0] : null;
		String chapterPageStr = args.length > 1 ? args[1] : null;
		final IText input = new TextInput(user, "help", false, ess);

		if (input.getLines().isEmpty())
		{
			if (Util.isInt(pageStr) || pageStr == null)
			{
				output = new HelpInput(user, "", ess);
			}
			else
			{
				output = new HelpInput(user, pageStr, ess);
				pageStr = chapterPageStr;
			}
			chapterPageStr = null;
		}
		else
		{
			output = new KeywordReplacer(input, user, ess);
		}
		final TextPager pager = new TextPager(output);
		pager.showPage(pageStr, chapterPageStr, commandLabel, user);
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		sender.sendMessage(_("helpConsole"));
	}
}
