package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtphere extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final IUser player = ess.getUserMap().matchUserExcludingHidden(args[0], user.getPlayer());
		if (!player.getData().isTeleportEnabled())
		{
			throw new Exception(_("teleportDisabled", player.getPlayer().getDisplayName()));
		}
		ISettings settings = ess.getSettings();
		if (settings.getData().getGeneral().isPerGroupTeleport() && !Permissions.PERGROUPTELEPORT.isAuthorized(
					user, ess.getRanks().getMainGroup(player)))
			{
				throw new Exception(_("noPerm", "essentials.teleport.groups." + ess.getRanks().getMainGroup(player)));
			}

		user.getTeleport().teleportToMe(player, new Trade(commandName, ess), TeleportCause.COMMAND);
		user.sendMessage(_("teleporting"));
		player.sendMessage(_("teleporting"));
		throw new NoChargeException();
	}
}
