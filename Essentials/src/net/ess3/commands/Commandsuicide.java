package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import org.bukkit.entity.Player;


public class Commandsuicide extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final Player player = user.getPlayer();
		ess.getPlugin().callSuicideEvent(player);
		user.getPlayer().damage(player.getHealth());
		user.sendMessage(_("§6Goodbye cruel world..."));
		ess.broadcastMessage(user, _("§6{0} §6took their own life.", player.getDisplayName()));
	}
}
