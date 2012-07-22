package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.api.server.CommandSender;
import net.ess3.api.server.Player;


public class Commandext extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		extinguishPlayers(sender, args[0]);
	}

	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			user.setFireTicks(0);
			user.sendMessage(_("extinguish"));
			return;
		}

		extinguishPlayers(user, args[0]);
	}

	private void extinguishPlayers(final CommandSender sender, final String name) throws Exception
	{
		for (Player matchPlayer : ess.getUserMap().matchUsers(name, false, false))
		{
			matchPlayer.setFireTicks(0);
			sender.sendMessage(_("extinguishOthers", matchPlayer.getDisplayName()));
		}
	}
}
