package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandafk extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && Permissions.AFK_OTHERS.isAuthorized(user))
		{
			IUser afkUser = ess.getUserMap().matchUserExcludingHidden(args[0], user.getPlayer());
			if (afkUser != null)
			{
				toggleAfk(afkUser);
			}
		}
		else
		{
			toggleAfk(user);
		}
	}

	private void toggleAfk(IUser user)
	{
		if (!user.toggleAfk())
		{
			//user.sendMessage(_("markedAsNotAway"));
			ess.broadcastMessage(user, _("userIsNotAway", user.getPlayer().getDisplayName()));
			user.updateActivity(false);
		}
		else
		{
			//user.sendMessage(_("markedAsAway"));
			ess.broadcastMessage(user, _("userIsAway", user.getPlayer().getDisplayName()));
		}
	}	
}
