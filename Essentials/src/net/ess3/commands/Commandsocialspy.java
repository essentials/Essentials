package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.command.CommandSender;


public class Commandsocialspy extends EssentialsToggleCommand
{

	@Override
	protected void setValue(final IUser player, final boolean value)
	{
		player.getData().setSocialspy(value);
		player.queueSave();
	}

	@Override
	protected boolean getValue(final IUser player)
	{
		return player.getData().isSocialspy();
	}

	@Override
	protected void informSender(final CommandSender sender, final boolean value, final IUser player)
	{
		if (value)
		{
			sender.sendMessage(_("socialSpyMode", _(getValue(player) ? "enabled" : "disabled"), player.getPlayer().getDisplayName()));
		}
		else
		{
			sender.sendMessage(_("socialSpyFailed", player.getName()));
		}
	}

	@Override
	protected void informPlayer(final IUser player)
	{
		final String message = _("socialSpyMode", _(getValue(player) ? "enabled" : "disabled"), player.getPlayer().getDisplayName());
		player.sendMessage(message);
	}

	@Override
	protected boolean canEditOthers(final IUser user)
	{
		return Permissions.SOCIALSPY_OTHERS.isAuthorized(user);
	}

	@Override
	protected boolean isExempt(final CommandSender sender, final IUser player)
	{
		return (player.isOnline() ? Permissions.SOCIALSPY_EXEMPT.isAuthorized(player) : !Permissions.SOCIALSPY_OFFLINE.isAuthorized(sender));
	}
}
