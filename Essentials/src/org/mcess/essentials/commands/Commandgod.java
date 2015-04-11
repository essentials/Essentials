package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import net.ess3.api.events.GodStatusChangeEvent;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandgod extends EssentialsToggleCommand
{
	public Commandgod()
	{
		super("god", "essentials.god.others");
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
			enabled = !user.isGodModeEnabled();
		}

		final User controller = sender.isPlayer() ? ess.getUser(sender.getPlayer()) : null;
		final GodStatusChangeEvent godEvent = new GodStatusChangeEvent(controller, user, enabled);
		ess.getServer().getPluginManager().callEvent(godEvent);
		if (!godEvent.isCancelled())
		{
			user.setGodModeEnabled(enabled);

			if (enabled && user.getBase().getHealth() != 0)
			{
				user.getBase().setHealth(user.getBase().getMaxHealth());
				user.getBase().setFoodLevel(20);
			}

			user.sendMessage(I18n.tl("godMode", enabled ? I18n.tl("enabled") : I18n.tl("disabled")));
			if (!sender.isPlayer() || !sender.getPlayer().equals(user.getBase()))
			{
				sender.sendMessage(I18n.tl("godMode", I18n.tl(enabled ? "godEnabledFor" : "godDisabledFor", user.getDisplayName())));
			}
		}
	}
}
