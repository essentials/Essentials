package net.ess3.api.ondemand;

import net.ess3.api.IUser;
import net.ess3.api.server.Player;
import net.ess3.api.server.Server;


public class UserOnDemand extends OnDemand<IUser>
{
	private final String name;
	private final Server server;

	public UserOnDemand(String name, Server server)
	{
		this.name = name;
		this.server = server;
	}

	@Override
	protected IUser getNew()
	{
		Player player = server.getPlayer(name);
		if (player == null)
		{
			return null;
		}
		return player.getUser();
	}
}
