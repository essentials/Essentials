package net.ess3.commands;

import static net.ess3.I18n._;
import org.bukkit.command.CommandSender;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandvanish extends EssentialsToggleCommand
{
	protected void setValue(final IUser player, final boolean value)
	{
		player.setVanished(value);
	}

	protected boolean getValue(final IUser player)
	{
		return player.isVanished();
	}

	protected void informSender(final CommandSender sender, final boolean value, final IUser player)
	{
		if (value) {
			sender.sendMessage( _("vanishMode", _(getValue(player) ? "enabled" : "disabled"), player.getPlayer().getDisplayName()));
		}
		else {
			//TODO: TL this
			sender.sendMessage("Can't change vanish mode for player " + player.getName());
		}
	}

	protected void informPlayer(final IUser player)
	{
		player.sendMessage(getValue(player) ? _("vanished") : _("unvanished"));
	}

	protected boolean canToggleOthers(final IUser user)
	{
		return Permissions.VANISH_OTHERS.isAuthorized(user);
	}

	protected boolean isExempt(final CommandSender sender, final IUser player)
	{
		return Permissions.VANISH_EXEMPT.isAuthorized(player);
	}

	protected boolean toggleOfflinePlayers()
	{
		return false;
	}
}
