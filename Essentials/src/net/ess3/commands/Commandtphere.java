package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtphere extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{	
		final IUser player = ess.getUserMap().matchUser(args[0], false, false);
		if (!player.getData().isTeleportEnabled())
		{
			throw new Exception(_("teleportDisabled", player.getPlayer().getDisplayName()));
		}
		player.getTeleport().teleport(user.getPlayer(), new Trade(commandName, ess), TeleportCause.COMMAND);
		user.sendMessage(_("teleporting"));
		player.sendMessage(_("teleporting"));
		throw new NoChargeException();
	}
}
