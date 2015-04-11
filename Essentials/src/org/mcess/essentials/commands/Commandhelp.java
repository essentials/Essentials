package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import org.mcess.essentials.textreader.*;
import org.mcess.essentials.utils.NumberUtil;
import java.util.Locale;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandhelp extends EssentialsCommand
{
	public Commandhelp()
	{
		super("help");
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		IText output;
		String pageStr = args.length > 0 ? args[0] : null;
		String chapterPageStr = args.length > 1 ? args[1] : null;
		String command = commandLabel;
		final IText input = new TextInput(user.getSource(), "help", false, ess);

		if (input.getLines().isEmpty())
		{
			if (NumberUtil.isInt(pageStr) || pageStr == null)
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
			user.setDisplayNick();
			output = new KeywordReplacer(input, user.getSource(), ess);
		}
		final TextPager pager = new TextPager(output);
		pager.showPage(pageStr, chapterPageStr, command, user.getSource());
	}

	@Override
	protected void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		sender.sendMessage(I18n.tl("helpConsole"));
	}
}
