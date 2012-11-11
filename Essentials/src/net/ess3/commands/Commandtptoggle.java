package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandtptoggle extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		IUser otherUser = null;
		if (args.length > 0 && Permissions.TPTOGGLE_OTHERS.isAuthorized(user))
		{
			otherUser = ess.getUserMap().getUser(server.getPlayer(args[0]));
				if (otherUser == null)
				{
					throw new Exception(_("playerNotFound"));
				}
				else
				{
				ess.getUserMap().getUser(server.getPlayer(args[0])).sendMessage(user.toggleTeleportEnabled()
						? _("teleportationEnabled")
						: _("teleportationDisabled"));
				}
		}
		else 
		{
		user.sendMessage(user.toggleTeleportEnabled()
						 ? _("teleportationEnabled")
						 : _("teleportationDisabled"));
		}
	}
}
