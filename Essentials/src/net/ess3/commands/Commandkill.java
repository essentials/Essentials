package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.EntityDamageEvent;


public class Commandkill extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		for (IUser matchPlayer : ess.getUserMap().matchUsersExcludingHidden(args[0], getPlayerOrNull(sender)))
		{
			final EntityDamageEvent ede = new EntityDamageEvent(matchPlayer.getPlayer(), sender instanceof IUser && sender.getName().equals(matchPlayer.getName()) ? EntityDamageEvent.DamageCause.SUICIDE : EntityDamageEvent.DamageCause.CUSTOM, Short.MAX_VALUE);
			server.getPluginManager().callEvent(ede);
			if (ede.isCancelled() && Permissions.KILL_FORCE.isAuthorized(sender))
			{
				continue;
			}

			matchPlayer.getPlayer().damage(Short.MAX_VALUE);
			sender.sendMessage(_("kill", matchPlayer.getPlayer().getDisplayName()));
		}
	}
}
