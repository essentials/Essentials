package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.I18nComponent._;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.perm.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;


public class Commandafk extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && Permissions.AFK_OTHERS.isAuthorized(user))
		{
			IUser afkUser = getContext().getUser((Player)getContext().getServer().matchPlayer(args[0]));
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
			if (!user.isHidden())
			{
				getContext().broadcastMessage(user, _("userIsNotAway", user.getDisplayName()));
			}
			user.updateActivity(false);
		}
		else
		{
			//user.sendMessage(_("markedAsAway"));
			if (!user.isHidden())
			{
				getContext().broadcastMessage(user, _("userIsAway", user.getDisplayName()));
			}
		}
	}
	
	@Override
	public PermissionDefault getPermissionDefault()
	{
		return PermissionDefault.TRUE;
	}
}
