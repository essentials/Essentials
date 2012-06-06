package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.utils.Util;
import com.earth2me.essentials.utils.textreader.HelpInput;
import com.earth2me.essentials.utils.textreader.IText;
import com.earth2me.essentials.utils.textreader.KeywordReplacer;
import com.earth2me.essentials.utils.textreader.TextInput;
import com.earth2me.essentials.utils.textreader.TextPager;
import java.util.Locale;
import org.bukkit.command.CommandSender;


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
