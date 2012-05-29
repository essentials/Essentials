package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.economy.Trade;
import com.earth2me.essentials.permissions.WorldPermissions;


public class Commandback extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final String worldName = user.getData().getLastLocation().getWorldName();
		if (user.getWorld() != user.getData().getLastLocation().getBukkitLocation().getWorld() && ess.getSettings().getData().getGeneral().isWorldTeleportPermissions()
			&& WorldPermissions.getPermission(worldName))
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
