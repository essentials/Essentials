package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.SpawnMob;
import net.ess3.api.IUser;

import java.util.List;


public class Commandspawnmob extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			final String mobList = SpawnMob.mobList(user);
			throw new NotEnoughArgumentsException(_("§6Mobs:§r {0}", mobList));
		}

		List<String> mobParts = SpawnMob.mobParts(args[0]);
		List<String> mobData = SpawnMob.mobData(args[0]);

		int mobCount = 1;
		if (args.length >= 2)
		{
			mobCount = Integer.parseInt(args[1]);
		}

		if (args.length >= 3)
		{
			IUser target = ess.getUserMap().getUser(args[2]);
			SpawnMob.spawnmob(ess, server, user, target, mobParts, mobData, mobCount);
		}

		SpawnMob.spawnmob(ess, server, user, mobParts, mobData, mobCount);
	}
}
