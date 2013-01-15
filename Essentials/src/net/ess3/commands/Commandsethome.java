package net.ess3.commands;

import java.util.Locale;
import java.util.regex.Pattern;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandsethome extends EssentialsCommand
{
	private final transient Pattern colon = Pattern.compile(":");

	@Override
	public void run(final IUser user, final String commandLabel, String[] args) throws Exception
	{
		if (args.length > 0)
		{
			//Allowing both formats /sethome khobbits house | /sethome khobbits:house
			final String[] nameParts = colon.split(args[0]);
			if (nameParts[0].length() != args[0].length())
			{
				args = nameParts;
			}

			if (args.length < 2)
			{
				if (Permissions.SETHOME_MULTIPLE.isAuthorized(user))
				{
					if ("bed".equals(args[0].toLowerCase(Locale.ENGLISH)))
					{
						throw new NotEnoughArgumentsException();
					}
					if ((user.getHomes().size() < ess.getRanks().getHomeLimit(user)) || (user.getHomes().contains(args[0].toLowerCase(Locale.ENGLISH))))
					{
						user.getData().addHome(args[0].toLowerCase(Locale.ENGLISH), user.getPlayer().getLocation());
						user.queueSave();
					}
					else
					{
						throw new Exception(_("maxHomes", ess.getRanks().getHomeLimit(user)));
					}

				}
				else
				{
					throw new Exception(_("maxHomes", 1));
				}
			}
			else
			{
				if (Permissions.SETHOME_OTHERS.isAuthorized(user))
				{
					IUser usersHome = ess.getUserMap().getUser(ess.getServer().getPlayer(args[0]));
					if (usersHome == null)
					{
						throw new NoSuchFieldException(_("playerNotFound"));
					}
					String name = args[1].toLowerCase(Locale.ENGLISH);
					if (!Permissions.SETHOME_MULTIPLE.isAuthorized(user))
					{
						name = "home";
					}
					if ("bed".equals(name.toLowerCase(Locale.ENGLISH)))
					{
						throw new NoSuchFieldException(_("invalidHomeName"));
					}

					usersHome.getData().addHome(name, user.getPlayer().getLocation());
					usersHome.queueSave();
				}
			}
		}
		else
		{
			user.getData().addHome("home", user.getPlayer().getLocation());
			user.queueSave();
		}
		user.sendMessage(
				_(
						"homeSet", user.getPlayer().getLocation().getWorld().getName(), user.getPlayer().getLocation().getBlockX(),
						user.getPlayer().getLocation().getBlockY(), user.getPlayer().getLocation().getBlockZ()));

	}
}
