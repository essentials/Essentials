package com.earth2me.essentials.spawn;

import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.components.settings.users.IUserComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.perm.Permissions;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandspawn extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		final Trade charge = new Trade(getCommandName(), getContext());
		charge.isAffordableFor(user);
		if (args.length > 0 && Permissions.SPAWN_OTHERS.isAuthorized(user))
		{
			final IUserComponent otherUser = getPlayer(args, 0);
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
		final IUserComponent user = getPlayer(args, 0);
		respawn(user, null);
		user.sendMessage(_("teleportAtoB", user.getDisplayName(), "spawn"));
		sender.sendMessage(_("teleporting"));
	}

	private void respawn(final IUserComponent user, final Trade charge) throws Exception
	{
		final SpawnStorageComponent spawns = (SpawnStorageComponent)this.getComponent();
		final Location spawn = spawns.getSpawn(getContext().getGroups().getMainGroup(user));
		user.getTeleport().teleport(spawn, charge, TeleportCause.COMMAND);
	}
}
