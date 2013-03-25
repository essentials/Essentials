package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import org.bukkit.command.CommandSender;
import org.bukkit.Server;


public class Commandsocialspy extends EssentialsCommand
{
	public Commandsocialspy()
	{
		super("socialspy");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 1 && user.isAuthorized("essentials.socialspy.others"))
		{
			User target = getPlayer(server, user, args, 0);
			user.sendMessage("§7SocialSpy " + (target.toggleSocialSpy() ? _("enabled") : _("disabled")));
			
		}
		else
		{
			user.sendMessage("§7SocialSpy " + (user.toggleSocialSpy() ? _("enabled") : _("disabled")));
		}
	}
	
	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 1)
		{
			User target = getPlayer(server, args, 0, true, false);
			sender.sendMessage("§7SocialSpy " + (target.toggleSocialSpy() ? _("enabled") : _("disabled")));
			
		}
		else
		{
			throw new NotEnoughArgumentsException();
		}
	}
}
