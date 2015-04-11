package org.mcess.essentials.commands;

import static org.mcess.essentials.I18n.tl;
import org.mcess.essentials.User;
import org.bukkit.Server;
import org.bukkit.event.entity.EntityDamageEvent;


public class Commandsuicide extends EssentialsCommand
{
	public Commandsuicide()
	{
		super("suicide");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		EntityDamageEvent ede = new EntityDamageEvent(user.getBase(), EntityDamageEvent.DamageCause.SUICIDE, Short.MAX_VALUE);
		server.getPluginManager().callEvent(ede);
		user.getBase().damage(Short.MAX_VALUE);
		if (user.getBase().getHealth() > 0)
		{
			user.getBase().setHealth(0);
		}
		user.sendMessage(tl("suicideMessage"));
		user.setDisplayNick();
		ess.broadcastMessage(user, tl("suicideSuccess", user.getDisplayName()));
	}
}
