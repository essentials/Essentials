package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.textreader.IText;
import org.mcess.essentials.textreader.KeywordReplacer;
import org.mcess.essentials.textreader.TextInput;
import org.mcess.essentials.textreader.TextPager;
import org.bukkit.Server;


public class Commandinfo extends EssentialsCommand
{
	public Commandinfo()
	{
		super("info");
	}

	@Override
	protected void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (sender.isPlayer())
		{
			ess.getUser(sender.getPlayer()).setDisplayNick();
		}
		
		final IText input = new TextInput(sender, "info", true, ess);
		final IText output = new KeywordReplacer(input, sender, ess);
		final TextPager pager = new TextPager(output);
		pager.showPage(args.length > 0 ? args[0] : null, args.length > 1 ? args[1] : null, commandLabel, sender);
	}
}
