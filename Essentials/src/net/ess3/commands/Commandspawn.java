package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import net.ess3.settings.SpawnsHolder;
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
			final IUser otherUser = getPlayer(args, 0);
			respawn(otherUser, null);
			if (!otherUser.equals(user))
			{
				otherUser.sendMessage(_("teleportAtoB", user.getDisplayName(), "spawn"));
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
		final IUser user = getPlayer(args, 0);
		respawn(user, null);
		user.sendMessage(_("teleportAtoB", user.getDisplayName(), "spawn"));
		sender.sendMessage(_("teleporting"));
	}

	private void respawn(final IUser user, final Trade charge) throws Exception
	{
		final SpawnsHolder spawns = (SpawnsHolder)this.module;
		final Location spawn = spawns.getSpawn(ess.getRanks().getMainGroup(user));
		user.getTeleport().teleport(spawn, charge, TeleportCause.COMMAND);
	}
}
