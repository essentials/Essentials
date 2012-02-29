package com.earth2me.essentials;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.ISettingsComponent;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.users.TimeStampType;
import com.earth2me.essentials.perm.Permissions;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.entity.Player;


public class EssentialsTimer implements Runnable
{
	private final transient IContext context;
	private final transient Set<IUserComponent> onlineUsers = new HashSet<IUserComponent>();

	EssentialsTimer(final IContext context)
	{
		this.context = context;
	}

	@Override
	public void run()
	{
		final long currentTime = System.currentTimeMillis();
		for (Player player : context.getServer().getOnlinePlayers())
		{

			try
			{
				final IUserComponent user = context.getUser(player);
				onlineUsers.add(user);
				user.setLastOnlineActivity(currentTime);
				user.checkActivity();

				boolean mailDisabled = false;
				ISettingsComponent settings = context.getSettings();
				settings.acquireReadLock();
				try
				{
					mailDisabled = settings.getData().getCommands().isDisabled("mail");
				}
				finally
				{
					settings.unlock();
				}
				// New mail notification
				if (user != null && !mailDisabled && Permissions.MAIL.isAuthorized(user) && !user.gotMailInfo())
				{
					final List<String> mail = user.getMails();
					if (mail != null && !mail.isEmpty())
					{
						user.sendMessage($("youHaveNewMail", mail.size()));
					}
				}
			}
			catch (Exception e)
			{
				context.getLogger().log(Level.WARNING, "EssentialsTimer Error:", e);
			}
		}

		// This cannot be a for-each loop, as we use iterator.remove().
		final Iterator<IUserComponent> iterator = onlineUsers.iterator();
		while (iterator.hasNext())
		{
			final IUserComponent user = iterator.next();

			if (user.getLastOnlineActivity() < currentTime && user.getLastOnlineActivity() > user.getTimeStamp(TimeStampType.LOGOUT))
			{
				user.setTimeStamp(TimeStampType.LOGOUT, user.getLastOnlineActivity());
				iterator.remove();
				continue;
			}

			user.checkMuteTimeout(currentTime);
			user.checkJailTimeout(currentTime);
		}
	}
}
