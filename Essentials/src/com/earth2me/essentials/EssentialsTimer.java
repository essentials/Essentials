package com.earth2me.essentials;

import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.components.users.UserData.TimestampType;
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
	private final transient Set<IUser> onlineUsers = new HashSet<IUser>();

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
				final IUser user = context.getUser(player);
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
						user.sendMessage(_("youHaveNewMail", mail.size()));
					}
				}
			}
			catch (Exception e)
			{
				context.getLogger().log(Level.WARNING, "EssentialsTimer Error:", e);
			}
		}

		final Iterator<IUser> iterator = onlineUsers.iterator();
		while (iterator.hasNext())
		{
			final IUser user = iterator.next();
			if (user.getLastOnlineActivity() < currentTime && user.getLastOnlineActivity() > user.getTimestamp(TimestampType.LOGOUT))
			{
				user.setTimestamp(TimestampType.LOGOUT, user.getLastOnlineActivity());
				iterator.remove();
				continue;
			}
			user.checkMuteTimeout(currentTime);
			user.checkJailTimeout(currentTime);
		}
	}
}
