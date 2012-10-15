package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;


public class Commandsuicide extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		ess.getPlugin().callSuicideEvent(user.getPlayer());
		user.getPlayer().damage(Short.MAX_VALUE);
		user.sendMessage(_("suicideMessage"));
		ess.broadcastMessage(user,_("suicideSuccess", user.getPlayer().getDisplayName()));		
	}
}
