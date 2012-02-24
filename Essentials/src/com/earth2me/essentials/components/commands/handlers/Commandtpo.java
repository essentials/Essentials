package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.perm.Permissions;
import org.bukkit.OfflinePlayer;
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
		final IUser player = getPlayer(args, 0, true);
		// Check if user is offline
		if (player.getBase() instanceof OfflinePlayer)
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}

		// Verify permission
		if (!player.isHidden() || Permissions.TELEPORT_HIDDEN.isAuthorized(user))
		{
			user.getTeleport().now(player, false, TeleportCause.COMMAND);
			user.sendMessage(_("teleporting"));
		}
		else
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}
	}
}
