package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtpo extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		//Just basically the old tp command
		final IUser player = ess.getUserMap().matchUser(args[0], false);

		ISettings settings = ess.getSettings();
		if (settings.getData().getGeneral().isPerGroupTeleport() && !Permissions.PERGROUPTELEPORT.isAuthorized(
					user, ess.getRanks().getMainGroup(player)))
			{
				throw new Exception(_("noPerm", "essentials.teleport.groups." + ess.getRanks().getMainGroup(player)));
			}
		// Verify permission
		if (user.getPlayer().canSee(player.getPlayer()) || Permissions.TELEPORT_HIDDEN.isAuthorized(user))
		{
			user.getTeleport().now(player.getPlayer(), false, TeleportCause.COMMAND);
			user.sendMessage(_("teleporting"));
		}
		else
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}
	}
}
