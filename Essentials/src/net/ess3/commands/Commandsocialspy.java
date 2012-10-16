package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.command.CommandSender;


public class Commandsocialspy extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		socialspyOtherPlayers(sender, args);
	}

	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && !args[0].trim().isEmpty() && Permissions.SOCIALSPY_OTHERS.isAuthorized(user))
		{
			socialspyOtherPlayers(user, args);
			return;
		}

		user.getData().setSocialspy(!user.getData().isSocialspy());
		user.queueSave();
		user.sendMessage("§7SocialSpy " + (user.getData().isSocialspy() ? _("enabled") : _("disabled")));
	}

	private void socialspyOtherPlayers(final CommandSender sender, final String[] args)
	{
		for (IUser player : ess.getUserMap().matchUsers(args[0], true))
		{
			if (player.isOnline()
				? Permissions.SOCIALSPY_EXEMPT.isAuthorized(player)
				: !Permissions.SOCIALSPY_OFFLINE.isAuthorized(sender))
			{
				sender.sendMessage("Can't change socialspy for player " + player.getName()); //TODO: I18n
				continue;
			}
			if (args.length > 1)
			{
				if (args[1].contains("on") || args[1].contains("ena") || args[1].equalsIgnoreCase("1"))
				{
					player.getData().setSocialspy(true);
					player.queueSave();
				}
				else
				{
					player.getData().setSocialspy(false);
					player.queueSave();
				}
			}
			else
			{
				player.getData().setSocialspy(!player.getData().isSocialspy());
				player.queueSave();
			}

			final boolean enabled = player.getData().isSocialspy();
			player.sendMessage("§7SocialSpy " + (enabled ? _("enabled") : _("disabled"))); //TODO:I18n
			sender.sendMessage("§7SocialSpy " + (enabled ? _("enabled") : _("disabled")));
		}
	}
}
