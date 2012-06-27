package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import org.bukkit.event.entity.EntityDamageEvent;


public class Commandsuicide extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		EntityDamageEvent ede = new EntityDamageEvent(user.getBase(), EntityDamageEvent.DamageCause.SUICIDE, 1000);
		server.getPluginManager().callEvent(ede);
		user.damage(Short.MAX_VALUE);
		user.sendMessage(_("suicideMessage"));
		user.setDisplayNick();
		ess.broadcastMessage(user,_("suicideSuccess", user.getDisplayName()));		
	}
}
