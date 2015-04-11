package org.mcess.essentials.commands;

import org.mcess.essentials.Trade;
import org.mcess.essentials.User;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandback extends EssentialsCommand
{
	public Commandback()
	{
		super("back");
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (user.getLastLocation() == null)
		{
			throw new Exception(I18n.tl("noLocationFound"));
		}
		if (user.getWorld() != user.getLastLocation().getWorld() && ess.getSettings().isWorldTeleportPermissions()
			&& !user.isAuthorized("essentials.worlds." + user.getLastLocation().getWorld().getName()))
		{
			throw new Exception(I18n.tl("noPerm", "essentials.worlds." + user.getLastLocation().getWorld().getName()));
		}
		final Trade charge = new Trade(this.getName(), ess);
		charge.isAffordableFor(user);
		user.getTeleport().back(charge);
		throw new NoChargeException();
	}
}
