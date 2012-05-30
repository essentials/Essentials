package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;
import com.earth2me.essentials.permissions.WorldPermissions;
import lombok.Cleanup;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtpo extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		@Cleanup
		ISettings settings = ess.getSettings();
		switch (args.length)
		{
		case 0:
			throw new NotEnoughArgumentsException();

		case 1:
			final IUser player = getPlayer(args, 0, true);
			if (!player.isOnline() || (player.isHidden() && !Permissions.TELEPORT_HIDDEN.isAuthorized(player)))
			{
				throw new NoSuchFieldException(_("playerNotFound"));
			}
			settings.acquireReadLock();
			if (user.getWorld() != player.getWorld() && settings.getData().getGeneral().isWorldTeleportPermissions()
				&& !WorldPermissions.getPermission(player.getWorld().getName()).isAuthorized(player))
			{
				throw new Exception(_("noPerm", "essentials.world." + player.getWorld().getName()));
			}

			user.sendMessage(_("teleporting"));
			user.getTeleport().now(player, false, TeleportCause.COMMAND);
			break;

		default:
			if (!Permissions.TELEPORT_OTHERS.isAuthorized(user))
			{
				throw new Exception(_("noPerm", "essentials.tp.others"));
			}
			user.sendMessage(_("teleporting"));
			final IUser target = getPlayer(args, 0, true);
			final IUser toPlayer = getPlayer(args, 1, true);

			if (!target.isOnline() || !toPlayer.isOnline()
				|| ((target.isHidden() || toPlayer.isHidden()) && !Permissions.TELEPORT_HIDDEN.isAuthorized(user)))
			{
				throw new NoSuchFieldException(_("playerNotFound"));
			}
			settings.acquireReadLock();
			if (target.getWorld() != toPlayer.getWorld() && settings.getData().getGeneral().isWorldTeleportPermissions()
				&& !WorldPermissions.getPermission(toPlayer.getWorld().getName()).isAuthorized(user))
			{
				throw new Exception(_("noPerm", "essentials.world." + toPlayer.getWorld().getName()));
			}

			target.getTeleport().now(toPlayer, false, TeleportCause.COMMAND);
			target.sendMessage(_("teleportAtoB", user.getDisplayName(), toPlayer.getDisplayName()));
			break;
		}
	}
}
