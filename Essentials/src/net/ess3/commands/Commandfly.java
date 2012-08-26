package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;




public class Commandfly extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		flyOtherPlayers(server, sender, args);
	}

	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && args[0].trim().length() > 2 && Permissions.FLY_OTHERS.isAuthorized(user))
		{
			flyOtherPlayers(server, user, args);
			return;
		}
		user.getPlayer().setAllowFlight(!user.getPlayer().getAllowFlight());
		if (!user.getPlayer().getAllowFlight())
		{
			user.getPlayer().setFlying(false);
		}
		user.sendMessage(_("flyMode", _(user.getPlayer().getAllowFlight() ? "enabled" : "disabled"), user.getPlayer().getDisplayName()));
	}

	private void flyOtherPlayers(final Server server, final CommandSender sender, final String[] args)
	{
		for (IUser matchPlayer : ess.getUserMap().matchUsers(args[0],false,false))
		{	
			if (args.length > 1)
			{
				if (args[1].contains("on") || args[1].contains("ena") || args[1].equalsIgnoreCase("1"))
				{
					matchPlayer.getPlayer().setAllowFlight(true);
				}
				else
				{
					matchPlayer.getPlayer().setAllowFlight(false);
				}
			}
			else
			{
				matchPlayer.getPlayer().setAllowFlight(!matchPlayer.getPlayer().getAllowFlight());
			}
			
			if (!matchPlayer.getPlayer().getAllowFlight())
			{
				matchPlayer.getPlayer().setFlying(false);
			}
			sender.sendMessage(_("flyMode", _(matchPlayer.getPlayer().getAllowFlight() ? "enabled" : "disabled"), matchPlayer.getPlayer().getDisplayName()));
		}
	}
}
