package net.ess3.commands;

import lombok.Cleanup;
import net.ess3.Console;
import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import net.ess3.permissions.WorldPermissions;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtp extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		@Cleanup
		ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		switch (args.length)
		{
		case 0:
			throw new NotEnoughArgumentsException();

		case 1:
			@Cleanup
			final IUser player = ess.getUserMap().matchUser(args[0], false, false);
			player.acquireReadLock();
			if (!player.getData().isTeleportEnabled())
			{
				throw new Exception(_("teleportDisabled", player.getPlayer().getDisplayName()));
			}
			if (user.getPlayer().getWorld() != player.getPlayer().getWorld() && settings.getData().getGeneral().isWorldTeleportPermissions()
				&& !WorldPermissions.getPermission(player.getPlayer().getWorld().getName()).isAuthorized(user))
			{
				throw new Exception(_("noPerm", "essentials.world." + player.getPlayer().getWorld().getName()));
			}
			user.sendMessage(_("teleporting"));
			final Trade charge = new Trade(commandName, ess);
			charge.isAffordableFor(user);
			user.getTeleport().teleport(player.getPlayer(), charge, TeleportCause.COMMAND);
			throw new NoChargeException();

		default:
			if (!Permissions.TPOHERE.isAuthorized(user))
			{
				throw new Exception(_("needTpohere"));
			}
			user.sendMessage(_("teleporting"));
			@Cleanup
			final IUser target = ess.getUserMap().matchUser(args[0], false, false);
			@Cleanup
			final IUser toPlayer = ess.getUserMap().matchUser(args[1], false, false);
			target.acquireReadLock();
			toPlayer.acquireReadLock();

			if (!target.getData().isTeleportEnabled())
			{
				throw new Exception(_("teleportDisabled", target.getPlayer().getDisplayName()));
			}
			if (!toPlayer.getData().isTeleportEnabled())
			{
				throw new Exception(_("teleportDisabled", toPlayer.getPlayer().getDisplayName()));
			}
			if (target.getPlayer().getWorld() != toPlayer.getPlayer().getWorld() && settings.getData().getGeneral().isWorldTeleportPermissions()
				&& !WorldPermissions.getPermission(toPlayer.getPlayer().getWorld().getName()).isAuthorized(user))
			{
				throw new Exception(_("noPerm", "essentials.world." + toPlayer.getPlayer().getWorld().getName()));
			}
			target.getTeleport().now(toPlayer.getPlayer(), false, TeleportCause.COMMAND);
			target.sendMessage(_("teleportAtoB", user.getPlayer().getDisplayName(), toPlayer.getPlayer().getDisplayName()));
			break;
		}
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		sender.sendMessage(_("teleporting"));
		final IUser target = ess.getUserMap().getUser(args[0]);
		final IUser toPlayer =ess.getUserMap().getUser(args[1]);
		target.getTeleport().now(toPlayer.getPlayer(), false, TeleportCause.COMMAND);
		target.sendMessage(_("teleportAtoB", Console.NAME, toPlayer.getPlayer().getDisplayName()));
	}
}
