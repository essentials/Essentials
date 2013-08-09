package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.utils.FormatUtil;


public class Commandping extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			
			if (commandLabel.equalsIgnoreCase("pong") || commandLabel.equalsIgnoreCase("epong") ) 
			{
				user.sendMessage(_("Ping!"));
			} 
			else 
			{
				user.sendMessage(_("Pong!"));
			}
		}
		else
		{
			user.sendMessage(FormatUtil.replaceFormat(getFinalArg(args, 0)));
		}
	}
}
