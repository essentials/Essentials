package net.ess3.commands;

import static net.ess3.I18n._;
import org.bukkit.command.CommandSender;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandfly extends EssentialsToggleCommand
{
	protected void setValue(final IUser player, final boolean value)
	{
		player.getPlayer().setAllowFlight(value);
		if (!player.getPlayer().getAllowFlight())
		{
			player.getPlayer().setFlying(false);
		}
	}

	protected boolean getValue(final IUser player)
	{
		return player.getPlayer().getAllowFlight();
	}

	protected void informSender(final CommandSender sender, final boolean value, final IUser player)
	{
		if (value)
		{
			sender.sendMessage(_("flyMode", _(getValue(player) ? "enabled" : "disabled"), player.getPlayer().getDisplayName()));
		}
		else
		{
			//TODO: TL this
			sender.sendMessage("Can't change fly mode for player " + player.getName());
		}
	}

	protected void informPlayer(final IUser player)
	{
		final String message = _("flyMode", _(getValue(player) ? "enabled" : "disabled"), player.getPlayer().getDisplayName());
		player.sendMessage(message);
	}

	protected boolean canEditOthers(final IUser user)
	{
		return Permissions.FLY_OTHERS.isAuthorized(user);
	}

	protected boolean isExempt(final CommandSender sender, final IUser player)
	{
		return Permissions.FLY_EXEMPT.isAuthorized(player);
	}

	protected boolean toggleOfflinePlayers()
	{
		return false;
	}
}
