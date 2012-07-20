package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.api.server.CommandSender;
import net.ess3.api.server.Player;
import net.ess3.api.server.IServer;
import net.ess3.permissions.Permissions;




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
		user.setAllowFlight(!user.getAllowFlight());
		if (!user.getAllowFlight())
		{
			user.setFlying(false);
		}
		user.sendMessage(_("flyMode", _(user.getAllowFlight() ? "enabled" : "disabled"), user.getDisplayName()));
	}

	private void flyOtherPlayers(final IServer server, final CommandSender sender, final String[] args)
	{
		for (Player matchPlayer : ess.getUserMap().matchUsers(args[0],true,true))
		{	
			if (args.length > 1)
			{
				if (args[1].contains("on") || args[1].contains("ena") || args[1].equalsIgnoreCase("1"))
				{
					matchPlayer.setAllowFlight(true);
				}
				else
				{
					matchPlayer.setAllowFlight(false);
				}
			}
			else
			{
				matchPlayer.setAllowFlight(!matchPlayer.getAllowFlight());
			}
			
			if (!matchPlayer.getAllowFlight())
			{
				matchPlayer.setFlying(false);
			}
			sender.sendMessage(_("flyMode", _(matchPlayer.getAllowFlight() ? "enabled" : "disabled"), matchPlayer.getDisplayName()));
		}
	}
}
