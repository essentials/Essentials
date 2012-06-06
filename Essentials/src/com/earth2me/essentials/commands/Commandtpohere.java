package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;
import com.earth2me.essentials.permissions.WorldPermissions;
import lombok.Cleanup;
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
		final IUser player = getPlayer(args, 0, true);

		// Check if user is offline
		if (!player.isOnline())
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}
		@Cleanup
		ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		//todo - common method
		if (user.getWorld() != player.getWorld() && settings.getData().getGeneral().isWorldTeleportPermissions()
			&& !WorldPermissions.getPermission(user.getWorld().getName()).isAuthorized(player))
		{
			throw new Exception(_("noPerm", "essentials.world." + user.getWorld().getName()));
		}


		// Verify permission
		if (!player.isHidden() || Permissions.TELEPORT_HIDDEN.isAuthorized(user))
		{
			player.getTeleport().now(user, false, TeleportCause.COMMAND);
			user.sendMessage(_("teleporting"));
		}
		else
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}
	}
}
