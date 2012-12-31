package net.ess3.commands;

import static net.ess3.I18n._;
import org.bukkit.entity.Player;
import net.ess3.api.IUser;


public class Commandsuicide extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final Player player = user.getPlayer();
		ess.getPlugin().callSuicideEvent(player);
		user.getPlayer().damage(player.getHealth());
		user.sendMessage(_("suicideMessage"));
		ess.broadcastMessage(user, _("suicideSuccess", player.getDisplayName()));
	}
}
