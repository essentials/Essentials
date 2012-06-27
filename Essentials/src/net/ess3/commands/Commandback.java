package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.WorldPermissions;


public class Commandback extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final String worldName = user.getData().getLastLocation().getWorldName();
		if (user.getWorld() != user.getData().getLastLocation().getBukkitLocation().getWorld() && ess.getSettings().getData().getGeneral().isWorldTeleportPermissions()
			&& WorldPermissions.getPermission(worldName).isAuthorized(user))
		{
			throw new Exception(_("noPerm", "essentials.world." + worldName));
		}
		//tod - verify
		final Trade charge = new Trade(this.toString(), ess);
		charge.isAffordableFor(user);
		user.sendMessage(_("backUsageMsg"));
		user.getTeleport().back(charge);
		throw new NoChargeException();
	}
}
