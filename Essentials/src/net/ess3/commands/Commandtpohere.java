package net.ess3.commands;

import lombok.Cleanup;
import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtpohere extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		//Just basically the old tphere command
		final IUser player = ess.getUserMap().matchUser(args[0], false, true);

		// Check if user is offline
		if (!player.isOnline())
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}
		@Cleanup
		ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		//todo - common method
		if (user.getPlayer().getWorld() != player.getPlayer().getWorld() && settings.getData().getGeneral().isWorldTeleportPermissions()
			&& !Permissions.WORLD.isAuthorized(player, user.getPlayer().getWorld().getName()))
		{
			throw new Exception(_("noPerm", "essentials.world." + user.getPlayer().getWorld().getName()));
		}


		// Verify permission
		if (!player.isHidden() || Permissions.TELEPORT_HIDDEN.isAuthorized(user))
		{
			player.getTeleport().now(user.getPlayer(), false, TeleportCause.COMMAND);
			user.sendMessage(_("teleporting"));
		}
		else
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}
	}
}
