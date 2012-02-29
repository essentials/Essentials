package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.perm.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;


public class Commandafk extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && Permissions.AFK_OTHERS.isAuthorized(user))
		{
			IUserComponent afkUser = getContext().getUser((Player)getContext().getServer().matchPlayer(args[0]));
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

	private void toggleAfk(IUserComponent user)
	{
		if (!user.toggleAfk())
		{
			//user.sendMessage($("markedAsNotAway"));
			if (!user.isHidden())
			{
				getContext().getMessager().broadcastMessage(user, $("userIsNotAway", user.getDisplayName()));
			}
			user.updateActivity(false);
		}
		else
		{
			//user.sendMessage($("markedAsAway"));
			if (!user.isHidden())
			{
				getContext().getMessager().broadcastMessage(user, $("userIsAway", user.getDisplayName()));
			}
		}
	}

	@Override
	public PermissionDefault getPermissionDefault()
	{
		return PermissionDefault.TRUE;
	}
}
