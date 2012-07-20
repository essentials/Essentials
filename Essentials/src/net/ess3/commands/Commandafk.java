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
			IUser afkUser = ess.getUserMap().getUser(args[0]);
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
		user.setDisplayNick();
		if (!user.toggleAfk())
		{
			//user.sendMessage(_("markedAsNotAway"));
			if (!user.isHidden())
			{
				ess.broadcastMessage(user, _("userIsNotAway", user.getDisplayName()));
			}
			user.updateActivity(false);
		}
		else
		{
			//user.sendMessage(_("markedAsAway"));
			if (!user.isHidden())
			{
				ess.broadcastMessage(user, _("userIsAway", user.getDisplayName()));
			}
		}
	}	
}
