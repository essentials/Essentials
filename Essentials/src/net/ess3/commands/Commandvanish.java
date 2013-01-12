package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.command.CommandSender;


public class Commandvanish extends EssentialsToggleCommand
{
	@Override
	protected void setValue(final IUser player, final boolean value)
	{
		player.setVanished(value);
	}

	@Override
	protected boolean getValue(final IUser player)
	{
		return player.isVanished();
	}

	@Override
	protected void informSender(final CommandSender sender, final boolean value, final IUser player)
	{
		if (value)
		{
			sender.sendMessage(_("vanishMode", _(getValue(player) ? "enabled" : "disabled"), player.getPlayer().getDisplayName()));
		}
		else
		{
			sender.sendMessage(_("vanishFailed", player.getName()));
		}
	}

	@Override
	protected void informPlayer(final IUser player)
	{
		player.sendMessage(getValue(player) ? _("vanished") : _("unvanished"));
	}

	@Override
	protected boolean canEditOthers(final IUser user)
	{
		return Permissions.VANISH_OTHERS.isAuthorized(user);
	}

	@Override
	protected boolean isExempt(final CommandSender sender, final IUser player)
	{
		return Permissions.VANISH_EXEMPT.isAuthorized(player);
	}

	@Override
	protected boolean toggleOfflinePlayers()
	{
		return false;
	}
}
