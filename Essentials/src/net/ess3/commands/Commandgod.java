package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.command.CommandSender;

public class Commandgod extends EssentialsSettingsCommand
{
	protected void setValue(final IUser player, final boolean value)
	{
		player.setGodModeEnabled(value);
	}

	protected boolean getValue(final IUser player)
	{
		return player.isGodModeEnabled();
	}

	protected void informSender(final CommandSender sender, final boolean value, final IUser player)
	{
		if (value) {
			sender.sendMessage( _("godMode", _(getValue(player) ? "enabled" : "disabled"), player.getPlayer().getDisplayName()));
		}
		else {
			//TODO: TL this
			sender.sendMessage("Can't change god mode for player " + player.getName());
		}
	}

	protected void informPlayer(final IUser player)
	{
		final String message = _("godMode", _(getValue(player) ? "enabled" : "disabled"), player.getPlayer().getDisplayName());
		player.sendMessage(message);
	}

	protected boolean canToggleOthers(final IUser user)
	{
		return Permissions.GOD_OTHERS.isAuthorized(user);
	}

	protected boolean isExempt(final CommandSender sender, final IUser player)
	{
		return (player.isOnline() ? Permissions.GOD_EXEMPT.isAuthorized(player) : !Permissions.GOD_OFFLINE.isAuthorized(sender));
	}
}
