package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Trade;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NoChargeException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import lombok.Cleanup;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtphere extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		@Cleanup
		final IUserComponent player = getPlayer(args, 0);
		player.acquireReadLock();
		if (!player.getData().isTeleportEnabled())
		{
			throw new Exception($("teleportDisabled", player.getDisplayName()));
		}
		player.getTeleporter().teleport(user, new Trade(getCommandName(), getContext()), TeleportCause.COMMAND);
		user.sendMessage($("teleporting"));
		player.sendMessage($("teleporting"));
		throw new NoChargeException();
	}
}
