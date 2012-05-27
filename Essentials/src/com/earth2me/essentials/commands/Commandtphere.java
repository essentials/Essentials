package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.economy.Trade;
import com.earth2me.essentials.api.IUser;
import lombok.Cleanup;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtphere extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		@Cleanup
		final IUser player = getPlayer(args, 0);
		player.acquireReadLock();
		if (!player.getData().isTeleportEnabled())
		{
			throw new Exception(_("teleportDisabled", player.getDisplayName()));
		}
		if (user.getWorld() != player.getWorld() && ess.getSettings().isWorldTeleportPermissions()
			&& !user.isAuthorized("essentials.world." + user.getWorld().getName()))
		{
			throw new Exception(_("noPerm", "essentials.world." + user.getWorld().getName()));
		}
		player.getTeleport().teleport(user, new Trade(this.getName(), ess), TeleportCause.COMMAND);
		user.sendMessage(_("teleporting"));
		player.sendMessage(_("teleporting"));
		throw new NoChargeException();
	}
}
