package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.NumberUtil;
import java.util.Locale;
import org.bukkit.Location;
import org.bukkit.Server;


public class Commandsethome extends EssentialsCommand
{
	public Commandsethome()
	{
		super("sethome");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, String[] args) throws Exception
	{
		User usersHome = user;
		String name = "home";
		final Location location = user.getLocation();

		if (args.length > 0)
		{
			//Allowing both formats /sethome khobbits house | /sethome khobbits:house
			final String[] nameParts = args[0].split(":");
			if (nameParts[0].length() != args[0].length())
			{
				args = nameParts;
			}

			if (args.length < 2)
			{
				name = args[0].toLowerCase(Locale.ENGLISH);
			}
			else
			{
				name = args[1].toLowerCase(Locale.ENGLISH);
				if (user.isAuthorized("essentials.sethome.others"))
				{
					usersHome = ess.getUser(args[0]);
					if (usersHome == null)
					{
						throw new PlayerNotFoundException();
					}					
				}
			}
		}
		if (checkHomeLimit(user, usersHome, name))
		{
			name = "home";
		}
		if ("bed".equals(name) || NumberUtil.isInt(name))
		{
			throw new NoSuchFieldException(_("invalidHomeName"));
		}
		usersHome.setHome(name, location);
		user.sendMessage(_("homeSet", user.getLocation().getWorld().getName(), user.getLocation().getBlockX(), user.getLocation().getBlockY(), user.getLocation().getBlockZ(), name));

	}

	private boolean checkHomeLimit(final User user, final User usersHome, String name) throws Exception
	{
		if (!user.isAuthorized("essentials.sethome.multiple.unlimited"))
		{
			int limit = ess.getSettings().getHomeLimit(user);
			if (usersHome.getHomes().size() == limit && usersHome.getHomes().contains(name))
			{
				return false;
			}
			if (usersHome.getHomes().size() >= limit)
			{
				throw new Exception(_("maxHomes", ess.getSettings().getHomeLimit(user)));
			}
			if (limit == 1)
			{
				return true;
			}
		}
		return false;
	}
}
