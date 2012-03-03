package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.perm.Permissions;
import java.util.HashMap;
import java.util.Locale;
import lombok.Cleanup;


public class Commandsethome extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		String[] arguments = args;
		if (arguments.length > 0)
		{
			//Allowing both formats /sethome khobbits house | /sethome khobbits:house
			final String[] nameParts = arguments[0].split(":");
			if (nameParts[0].length() != arguments[0].length())
			{
				arguments = nameParts;
			}

			if (arguments.length < 2)
			{
				if (Permissions.SETHOME_MULTIPLE.isAuthorized(user))
				{
					if ("bed".equals(arguments[0].toLowerCase(Locale.ENGLISH))) {
						throw new NotEnoughArgumentsException();
					}
					if ((user.getHomes().size() < getContext().getGroups().getHomeLimit(user))
						|| (user.getHome(arguments[0].toLowerCase(Locale.ENGLISH)) != null))
					{
						user.acquireWriteLock();
						if (user.getData().getHomes() == null)
						{
							user.getData().setHomes(new HashMap<String, com.earth2me.essentials.storage.LocationData>());
						}
						user.getData().getHomes().put(arguments[0].toLowerCase(Locale.ENGLISH), new com.earth2me.essentials.storage.LocationData(user.getLocation()));
					}
					else
					{
						throw new Exception(_("maxHomes", getContext().getGroups().getHomeLimit(user)));
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
					@Cleanup
					IUserComponent usersHome = getContext().getUser(getContext().getServer().getPlayer(arguments[0]));
					if (usersHome == null)
					{
						throw new Exception(_("playerNotFound"));
					}
					String name = arguments[1].toLowerCase(Locale.ENGLISH);
					if (!Permissions.SETHOME_MULTIPLE.isAuthorized(user))
					{
						name = "home";
					}
					if ("bed".equals(name.toLowerCase(Locale.ENGLISH))) {
						throw new NotEnoughArgumentsException();
					}

					usersHome.acquireWriteLock();
					if (usersHome.getData().getHomes() == null)
					{
						usersHome.getData().setHomes(new HashMap<String, com.earth2me.essentials.storage.LocationData>());
					}
					usersHome.getData().getHomes().put(name, new com.earth2me.essentials.storage.LocationData(user.getLocation()));
				}
			}
		}
		else
		{
			user.acquireWriteLock();
			if (user.getData().getHomes() == null)
			{
				user.getData().setHomes(new HashMap<String, com.earth2me.essentials.storage.LocationData>());
			}
			user.getData().getHomes().put("home", new com.earth2me.essentials.storage.LocationData(user.getLocation()));
		}
		user.sendMessage(_("homeSet"));

	}
}
