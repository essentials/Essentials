package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.Mob;
import org.mcess.essentials.SpawnMob;
import org.mcess.essentials.User;
import org.mcess.essentials.utils.StringUtil;
import java.util.List;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandspawnmob extends EssentialsCommand
{
	public Commandspawnmob()
	{
		super("spawnmob");
	}
	
	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			final String mobList = SpawnMob.mobList(user);
			throw new NotEnoughArgumentsException(I18n.tl("mobsAvailable", mobList));
		}
		
		List<String> mobParts = SpawnMob.mobParts(args[0]);
		List<String> mobData = SpawnMob.mobData(args[0]);
		
		int mobCount = 1;
		if (args.length >= 2)
		{
			mobCount = Integer.parseInt(args[1]);
		}
		
		if (mobParts.size() > 1 && !user.isAuthorized("essentials.spawnmob.stack"))
		{
			throw new Exception(I18n.tl("cannotStackMob"));
		}
		
		if (args.length >= 3)
		{
			final User target = getPlayer(ess.getServer(), user, args, 2);
			SpawnMob.spawnmob(ess, server, user.getSource(), target, mobParts, mobData, mobCount);
			return;
		}
		
		SpawnMob.spawnmob(ess, server, user, mobParts, mobData, mobCount);
	}
	
	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 3)
		{
			final String mobList = StringUtil.joinList(Mob.getMobList());
			throw new NotEnoughArgumentsException(I18n.tl("mobsAvailable", mobList));
		}
		
		List<String> mobParts = SpawnMob.mobParts(args[0]);
		List<String> mobData = SpawnMob.mobData(args[0]);
		int mobCount = Integer.parseInt(args[1]);
		
		final User target = getPlayer(ess.getServer(), args, 2, true, false);
		SpawnMob.spawnmob(ess, server, sender, target, mobParts, mobData, mobCount);
	}
}
