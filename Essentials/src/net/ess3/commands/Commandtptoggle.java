package net.ess3.commands;

import static net.ess3.I18n._;
import org.bukkit.command.CommandSender;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandtptoggle extends EssentialsToggleCommand
{

	protected void setValue(final IUser player, final boolean value)
	{
		player.getData().setTeleportEnabled(value);
		player.queueSave();
	}

	protected boolean getValue(final IUser player)
	{
		return player.getData().isTeleportEnabled();
	}

	protected void informSender(final CommandSender sender, final boolean value, final IUser player)
	{
		if (value)
		{
			sender.sendMessage(_("teleportMode", _(getValue(player) ? "enabled" : "disabled"), player.getPlayer().getDisplayName()));
		}
		else
		{
			sender.sendMessage(_("tptoggleFailed", player.getName()));
		}
	}

	protected void informPlayer(final IUser player)
	{
		final String message = _("teleportMode", _(getValue(player) ? "enabled" : "disabled"), player.getPlayer().getDisplayName());
		player.sendMessage(message);
	}

	protected boolean canEditOthers(final IUser user)
	{
		return Permissions.TPTOGGLE_OTHERS.isAuthorized(user);
	}

	protected boolean isExempt(final CommandSender sender, final IUser player)
	{
		return Permissions.TPTOGGLE_EXEMPT.isAuthorized(player);
	}
}
