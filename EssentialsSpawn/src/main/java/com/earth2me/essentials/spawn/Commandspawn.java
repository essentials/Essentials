package com.earth2me.essentials.spawn;

import com.earth2me.essentials.CommandSource;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.Console;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandspawn extends EssentialsCommand
{
	public Commandspawn()
	{
		super("spawn");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		final Trade charge = new Trade(this.getName(), ess);
		charge.isAffordableFor(user);
		if (args.length > 0 && user.isAuthorized("essentials.spawn.others"))
		{
			final User otherUser = getPlayer(server, user, args, 0);
			respawn(user, otherUser, charge);
			if (!otherUser.equals(user))
			{
				otherUser.sendMessage(_("teleportAtoB", user.getDisplayName(), "spawn"));
				user.sendMessage(_("teleporting"));
			}
		}
		else
		{
			respawn(user, user, charge);
		}
		throw new NoChargeException();
	}

	@Override
	protected void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		final User user = getPlayer(server, args, 0, true, false);
		respawn(null, user, null);
		user.sendMessage(_("teleportAtoB", Console.NAME, "spawn"));
		sender.sendMessage(_("teleporting"));
	}

	private void respawn(final User teleportOwner, final User teleportee, final Trade charge) throws Exception
	{
		final SpawnStorage spawns = (SpawnStorage)this.module;
		final Location spawn = spawns.getSpawn(teleportee.getGroup());
		if (teleportOwner == null)
		{
			teleportee.getTeleport().now(spawn, false, TeleportCause.COMMAND);
		}
		else
		{
			teleportOwner.getTeleport().teleportPlayer(teleportee, spawn, charge, TeleportCause.COMMAND);
		}
	}
}
