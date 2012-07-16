package net.ess3.commands;

import net.ess3.utils.textreader.IText;
import net.ess3.utils.textreader.KeywordReplacer;
import net.ess3.utils.textreader.TextInput;
import net.ess3.utils.textreader.TextPager;


public class Commandmotd extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		final IText input = new TextInput(sender, "motd", true, ess);
		final IText output = new KeywordReplacer(input, sender, ess);
		final TextPager pager = new TextPager(output);
		pager.showPage(args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : null, commandLabel, sender);
	}
}
