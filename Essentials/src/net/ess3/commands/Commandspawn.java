package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandspawn extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final Trade charge = new Trade(commandName, ess);
		charge.isAffordableFor(user);
		if (args.length > 0 && Permissions.SPAWN_OTHERS.isAuthorized(user))
		{
			final IUser otherUser = ess.getUserMap().matchUserExcludingHidden(args[0], user.getPlayer());
			respawn(otherUser, null);
			if (!otherUser.equals(user))
			{
				otherUser.sendMessage(_("teleportAtoB", user.getPlayer().getDisplayName(), "spawn"));
				user.sendMessage(_("teleporting"));
			}
		}
		else
		{
			respawn(user, null);
		}
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		final IUser user = ess.getUserMap().matchUser(args[0], false);
		respawn(user, null);
		user.sendMessage(_("teleportAtoB", user.getPlayer().getDisplayName(), "spawn"));
		sender.sendMessage(_("teleporting"));
	}

	private void respawn(final IUser user, final Trade charge) throws Exception
	{
		final Location spawn = ess.getSpawns().getSpawn(ess.getRanks().getMainGroup(user));
		user.getTeleport().teleport(spawn, charge, TeleportCause.COMMAND);
	}
}
