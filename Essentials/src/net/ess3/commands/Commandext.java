package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import org.bukkit.command.CommandSender;


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
			user.getPlayer().setFireTicks(0);
			user.sendMessage(_("extinguish"));
			return;
		}

		extinguishPlayers(user, args[0]);
	}

	private void extinguishPlayers(final CommandSender sender, final String name) throws Exception
	{
		for (IUser matchPlayer : ess.getUserMap().matchUsers(name, false, false))
		{
			matchPlayer.getPlayer().setFireTicks(0);
			sender.sendMessage(_("extinguishOthers", matchPlayer.getPlayer().getDisplayName()));
		}
	}
}
