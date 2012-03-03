package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.textreader.*;
import org.bukkit.command.CommandSender;


public class Commandhelp extends EssentialsCommand
{
	@Override
	protected void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		IText output;
		String pageStr = args.length > 0 ? args[0] : null;
		String chapterPageStr = args.length > 1 ? args[1] : null;
		final IText input = new TextInput(user, "help", false, getContext());

		if (input.getLines().isEmpty())
		{
			if (Util.isInt(pageStr) || pageStr == null)
			{
				output = new HelpInput(user, "", getContext());
			}
			else
			{
				output = new HelpInput(user, pageStr, getContext());
				pageStr = chapterPageStr;
			}
			chapterPageStr = null;
		}
		else
		{
			output = new KeywordReplacer(input, user, getContext());
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
