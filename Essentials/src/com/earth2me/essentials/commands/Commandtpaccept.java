package com.earth2me.essentials.commands;

import com.earth2me.essentials.Trade;
import org.bukkit.Server;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;


public class Commandtpaccept extends EssentialsCommand
{
	public Commandtpaccept()
	{
		super("tpaccept");
	}

	@Override
	public void run(Server server, User user, String commandLabel, String[] args) throws Exception
	{

		User p = user.getTeleportRequest();
		if (p == null)
		{
			throw new Exception(Util.i18n("noPendingRequest"));
		}

		Trade charge = new Trade(this.getName(), ess);
		if (user.isTeleportRequestHere())
		{
			charge.isAffordableFor(user);
		}
		else
		{
			charge.isAffordableFor(p);
		}
		user.sendMessage(Util.i18n("requestAccepted"));
		p.sendMessage(Util.format("requestAcceptedFrom", user.getDisplayName()));
		
		if (user.isTeleportRequestHere())
		{
			user.getTeleport().teleport(p, charge);
		}
		else
		{
			p.getTeleport().teleport(user, charge);
		}
		user.requestTeleport(null, false);
	}
}
