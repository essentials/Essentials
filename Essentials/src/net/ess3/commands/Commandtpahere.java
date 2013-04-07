package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandtpahere extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}


		final IUser player = ess.getUserMap().matchUserExcludingHidden(args[0], user.getPlayer());
		if (!player.getData().isTeleportEnabled())
		{
			throw new Exception(_("§c{0} §4has teleportation disabled.", player.getPlayer().getDisplayName()));
		}

		ISettings settings = ess.getSettings();
		if (settings.getData().getGeneral().isPerGroupTeleport() && !Permissions.PERGROUPTELEPORT.isAuthorized(
				user, ess.getRanks().getMainGroup(player)))
		{
			throw new Exception(_("§4You do not have the §c{0}§4 permission.", "essentials.teleport.groups." + ess.getRanks().getMainGroup(player)));
		}
		if (user.getPlayer().getWorld() != player.getPlayer().getWorld() && settings.getData().getGeneral().isWorldTeleportPermissions() && !Permissions.WORLD.isAuthorized(
				user, user.getPlayer().getWorld().getName()))
		{
			throw new Exception(_("§4You do not have the §c{0}§4 permission.", "essentials.world." + user.getPlayer().getWorld().getName()));
		}
		player.requestTeleport(user, true);
		player.sendMessage(_("§c{0}§6 has requested that you teleport to them.", user.getPlayer().getDisplayName()));
		player.sendMessage(_("§6To teleport, type §c/tpaccept§6."));
		player.sendMessage(_("§6To deny this request, type §c/tpdeny§6."));
		int tpaAcceptCancellation = settings.getData().getCommands().getTeleport().getRequestTimeout();
		if (tpaAcceptCancellation != 0)
		{
			player.sendMessage(_("§6This request will timeout after§c {0} seconds§6.", tpaAcceptCancellation));
		}
		user.sendMessage(_("§6Request sent to§c {0}§6.", player.getPlayer().getDisplayName()));
	}
}
