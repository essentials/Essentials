package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.utils.Util;
import com.earth2me.essentials.craftbukkit.SetExpFix;
import com.earth2me.essentials.permissions.Permissions;
import org.bukkit.entity.Player;


public class Commandexp extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length == 0)
		{
			showExp(user, user);
		}
		else if (args[0].equalsIgnoreCase("set") && Permissions.EXP_SET.isAuthorized(user))
		{
			if (args.length == 3 && Permissions.EXP_SET_OTHERS.isAuthorized(user))
			{
				Boolean foundUser = false;
				for (Player matchPlayer : server.matchPlayer(args[1]))
				{
					IUser target = ess.getUser(matchPlayer);
					setExp(user, target, args[2], false);
					foundUser = true;
				}
				if (foundUser == false)
				{
					throw new NoSuchFieldException(_("playerNotFound"));
				}
				return;
			}
			setExp(user, user, args[1], false);
		}
		else if (args[0].equalsIgnoreCase("give") && Permissions.EXP_GIVE.isAuthorized(user))
		{
			if (args.length == 3 && Permissions.EXP_GIVE_OTHERS.isAuthorized(user))
			{
				Boolean foundUser = false;
				for (Player matchPlayer : server.matchPlayer(args[1]))
				{
					IUser target = ess.getUser(matchPlayer);
					setExp(user, target, args[2], true);
					foundUser = true;
				}
				if (foundUser == false)
				{
					throw new NoSuchFieldException(_("playerNotFound"));
				}
				return;
			}
			setExp(user, user, args[1], true);
		}
		else
		{
			String search = args[0].trim();
			if (args.length == 2)
			{
				search = args[1].trim();
			}
			if (search.equalsIgnoreCase("show") || !Permissions.EXP_OTHERS.isAuthorized(user))
			{
				showExp(user, user);
				return;
			}
			for (Player matchPlayer : server.matchPlayer(search))
			{
				IUser target = ess.getUser(matchPlayer);
				showExp(user, target);
			}
		}
	}

	private void showExp(final IUser user, final IUser target)
	{
		final int totalExp = SetExpFix.getTotalExperience(target);
		final int expLeft = (int)Util.roundDouble(((((3.5 * target.getLevel()) + 6.7) - (totalExp - ((1.75 * (target.getLevel() * target.getLevel())) + (5.00 * target.getLevel())))) + 1));
		user.sendMessage(_("exp", target.getDisplayName(), SetExpFix.getTotalExperience(target), target.getLevel(), expLeft));
	}

	private void setExp(final IUser user, final IUser target, final String strAmount, final boolean give)
	{
		Long amount = Long.parseLong(strAmount);
		if (give)
		{
			amount += SetExpFix.getTotalExperience(target);
		}
		if (amount > Integer.MAX_VALUE)
		{
			amount = (long)Integer.MAX_VALUE;
		}
		SetExpFix.setTotalExperience(target, amount.intValue());
		user.sendMessage(_("expSet", target.getDisplayName(), amount));
	}
}
