package net.ess3.commands;

import net.ess3.Console;
import static net.ess3.I18n._;
import net.ess3.economy.Trade;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import lombok.Cleanup;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtp extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		switch (args.length)
		{
		case 0:
			throw new NotEnoughArgumentsException();

		case 1:
			@Cleanup
			final IUser player = getPlayer(args, 0);
			player.acquireReadLock();
			if (!player.getData().isTeleportEnabled())
			{
				throw new Exception(_("teleportDisabled", player.getDisplayName()));
			}
			user.sendMessage(_("teleporting"));
			final Trade charge = new Trade(commandName, ess);
			charge.isAffordableFor(user);
			user.getTeleport().teleport(player, charge, TeleportCause.COMMAND);
			throw new NoChargeException();

		default:
			if (!Permissions.TPOHERE.isAuthorized(user))
			{
				throw new Exception(_("needTpohere"));
			}
			user.sendMessage(_("teleporting"));
			final IUser target = getPlayer(args, 0);
			final IUser toPlayer = getPlayer(args, 1);
			target.getTeleport().now(toPlayer, false, TeleportCause.COMMAND);
			target.sendMessage(_("teleportAtoB", user.getDisplayName(), toPlayer.getDisplayName()));
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
		final IUser target = getPlayer(args, 0);
		final IUser toPlayer = getPlayer(args, 1);
		target.getTeleport().now(toPlayer, false, TeleportCause.COMMAND);
		target.sendMessage(_("teleportAtoB", Console.NAME, toPlayer.getDisplayName()));
	}
}
