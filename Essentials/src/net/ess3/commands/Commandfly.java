package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandfly extends EssentialsToggleCommand
{
	@Override
	protected void setValue(final IUser player, final boolean value)
	{
		final Player realPlayer = player.getPlayer();
		realPlayer.setAllowFlight(value);
		if (!realPlayer.getAllowFlight())
		{
			realPlayer.setFlying(false);
		}
	}

	@Override
	protected boolean getValue(final IUser player)
	{
		return player.getPlayer().getAllowFlight();
	}

	@Override
	protected void informSender(final CommandSender sender, final boolean value, final IUser player)
	{
		if (value)
		{
			sender.sendMessage(_("flyMode", _(getValue(player) ? "enabled" : "disabled"), player.getPlayer().getDisplayName()));
		}
		else
		{
			sender.sendMessage(_("flyFailed", player.getName()));
		}
	}

	@Override
	protected void informPlayer(final IUser player)
	{
		final String message = _("flyMode", _(getValue(player) ? "enabled" : "disabled"), player.getPlayer().getDisplayName());
		player.sendMessage(message);
	}

	@Override
	protected boolean canEditOthers(final IUser user)
	{
		return Permissions.FLY_OTHERS.isAuthorized(user);
	}

	@Override
	protected boolean isExempt(final CommandSender sender, final IUser player)
	{
		return Permissions.FLY_EXEMPT.isAuthorized(player);
	}

	@Override
	protected boolean toggleOfflinePlayers()
	{
		return false;
	}
}