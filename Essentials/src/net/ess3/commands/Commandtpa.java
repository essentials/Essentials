package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandtpa extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}


		IUser player = ess.getUserMap().matchUserExcludingHidden(args[0], user.getPlayer());
		if (!player.getData().isTeleportEnabled())
		{
			throw new Exception(_("{0} has teleportation disabled.", player.getPlayer().getDisplayName()));
		}
		ISettings settings = ess.getSettings();
		if (settings.getData().getGeneral().isPerGroupTeleport() && !Permissions.PERGROUPTELEPORT.isAuthorized(
					user, ess.getRanks().getMainGroup(player)))
			{
				throw new Exception(_("You do not have the {0} permission.", "essentials.teleport.groups." + ess.getRanks().getMainGroup(player)));
			}
		if (user.getPlayer().getWorld() != player.getPlayer().getWorld() && ess.getSettings().getData().getGeneral().isWorldTeleportPermissions() && !Permissions.WORLD.isAuthorized(
				user, user.getPlayer().getWorld().getName()))
		{
			throw new Exception(_("You do not have the {0} permission.", "essentials.world." + player.getPlayer().getWorld().getName()));
		}
		if (!player.isIgnoringPlayer(user))
		{
			player.requestTeleport(user, false);
			player.sendMessage(_("{0} has requested to teleport to you.", user.getPlayer().getDisplayName()));
			player.sendMessage(_("To teleport, type /tpaccept."));
			player.sendMessage(_("To deny this request, type /tpdeny."));
			int tpaAcceptCancellation = 0;
			tpaAcceptCancellation = settings.getData().getCommands().getTeleport().getRequestTimeout();
			if (tpaAcceptCancellation != 0)
			{
				player.sendMessage(_("This request will timeout after {0} seconds.", tpaAcceptCancellation));
			}
		}
		user.sendMessage(_("Request sent to {0}.", player.getPlayer().getDisplayName()));
	}
}
