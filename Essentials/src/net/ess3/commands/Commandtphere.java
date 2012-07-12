package net.ess3.commands;

import lombok.Cleanup;
import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.WorldPermissions;
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
		@Cleanup
		ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		if (user.getWorld() != player.getWorld() && settings.getData().getGeneral().isWorldTeleportPermissions()
			&& !WorldPermissions.getPermission(user.getWorld().getName()).isAuthorized(user))
		{
			throw new Exception(_("noPerm", "essentials.world." + user.getWorld().getName()));
		}
		player.getTeleport().teleport(user, new Trade(this.commandName, ess), TeleportCause.COMMAND);
		user.sendMessage(_("teleporting"));
		player.sendMessage(_("teleporting"));
		throw new NoChargeException();
	}
}
