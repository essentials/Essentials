package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.craftbukkit.SetExpFix;
import net.ess3.permissions.Permissions;
import net.ess3.utils.Util;
import org.bukkit.command.CommandSender;


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
				expMatch(user, args[1], args[2], false);
			}
			else
			{
				setExp(user, user, args[1], false);
			}
		}
		else if (args[0].equalsIgnoreCase("give") && Permissions.EXP_GIVE.isAuthorized(user))
		{
			if (args.length == 3 && Permissions.EXP_GIVE_OTHERS.isAuthorized(user))
			{
				expMatch(user, args[1], args[2], true);
			}
			else
			{
				setExp(user, user, args[1], true);
			}
		}
		else
		{
			String match = args[0].trim();
			if (args.length == 2)
			{
				match = args[1].trim();
			}
			if (match.equalsIgnoreCase("show") || !Permissions.EXP_OTHERS.isAuthorized(user))
			{
				showExp(user, user);
			}
			else
			{
				showMatch(user, match);
			}
		}
	}

	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		else if (args.length > 2 && args[0].equalsIgnoreCase("set"))
		{
			expMatch(sender, args[1], args[2], false);
		}
		else if (args.length > 2 && args[0].equalsIgnoreCase("give"))
		{
			expMatch(sender, args[1], args[2], true);
		}
		else
		{
			String match = args[0].trim();
			if (args.length == 2)
			{
				match = args[1].trim();
			}
			showMatch(sender, match);
		}
	}

	private void showMatch(final CommandSender sender, final String match) throws NotEnoughArgumentsException
	{
		boolean foundUser = false;
		for (IUser matchPlayer : ess.getUserMap().matchUsers(match, false, false))
		{
			foundUser = true;			
			showExp(sender, matchPlayer);
		}
		if (!foundUser)
		{
			throw new NotEnoughArgumentsException(_("playerNotFound"));
		}
	}

	private void expMatch(final CommandSender sender, final String match, final String amount, final boolean toggle) throws NotEnoughArgumentsException
	{
		boolean foundUser = false;
		for (IUser matchPlayer : ess.getUserMap().matchUsers(match, false, false))
		{			
			setExp(sender, matchPlayer, amount, toggle);
			foundUser = true;
		}
		if (!foundUser)
		{
			throw new NotEnoughArgumentsException(_("playerNotFound"));
		}
	}

	private void showExp(final CommandSender sender, final IUser target)
	{
		final int totalExp = SetExpFix.getTotalExperience(target);
		final int expLeft = (int)Util.roundDouble(((((3.5 * target.getLevel()) + 6.7) - (totalExp - ((1.75 * (target.getLevel() * target.getLevel())) + (5.00 * target.getLevel())))) + 1));
		sender.sendMessage(_("exp", target.getDisplayName(), SetExpFix.getTotalExperience(target), target.getLevel(), expLeft));
	}

	private void setExp(final CommandSender sender, final IUser target, final String strAmount, final boolean give)
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
		sender.sendMessage(_("expSet", target.getDisplayName(), amount));
	}
}