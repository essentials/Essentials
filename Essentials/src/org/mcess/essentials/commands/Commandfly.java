package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandfly extends EssentialsToggleCommand
{
	public Commandfly()
	{
		super("fly", "essentials.fly.others");
	}

	@Override
	protected void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		toggleOtherPlayers(server, sender, args);
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length == 1)
		{
			Boolean toggle = matchToggleArgument(args[0]);
			if (toggle == null && user.isAuthorized(othersPermission))
			{
				toggleOtherPlayers(server, user.getSource(), args);
			}
			else
			{
				togglePlayer(user.getSource(), user, toggle);
			}
		}
		else if (args.length == 2 && user.isAuthorized(othersPermission))
		{
			toggleOtherPlayers(server, user.getSource(), args);
		}
		else
		{
			togglePlayer(user.getSource(), user, null);
		}
	}

	@Override
	void togglePlayer(CommandSource sender, User user, Boolean enabled)
	{
		if (enabled == null)
		{
			enabled = !user.getBase().getAllowFlight();
		}

		user.getBase().setFallDistance(0f);
		user.getBase().setAllowFlight(enabled);
		
		if (!user.getBase().getAllowFlight())
		{
			user.getBase().setFlying(false);
		}

		user.sendMessage(I18n.tl("flyMode", I18n.tl(enabled ? "enabled" : "disabled"), user.getDisplayName()));
		if (!sender.isPlayer() || !sender.getPlayer().equals(user.getBase()))
		{
			sender.sendMessage(I18n.tl("flyMode", I18n.tl(enabled ? "enabled" : "disabled"), user.getDisplayName()));
		}
	}
}
