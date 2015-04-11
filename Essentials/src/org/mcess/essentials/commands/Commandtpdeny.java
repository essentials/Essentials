package org.mcess.essentials.commands;

import static org.mcess.essentials.I18n.tl;
import org.mcess.essentials.User;
import org.bukkit.Server;


public class Commandtpdeny extends EssentialsCommand
{
	public Commandtpdeny()
	{
		super("tpdeny");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		final User player = ess.getUser(user.getTeleportRequest());
		if (player == null)
		{
			throw new Exception(tl("noPendingRequest"));
		}

		user.sendMessage(tl("requestDenied"));
		player.sendMessage(tl("requestDeniedFrom", user.getDisplayName()));
		user.requestTeleport(null, false);
	}
}
