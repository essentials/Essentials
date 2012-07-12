package net.ess3;

import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.user.UserData.TimestampType;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.entity.Player;


public class EssentialsTimer implements Runnable
{
	private final transient IEssentials ess;
	private final transient Set<IUser> onlineUsers = new HashSet<IUser>();
	private transient long lastPoll = System.currentTimeMillis();
	private final transient LinkedList<Float> history = new LinkedList<Float>();

	EssentialsTimer(final IEssentials ess)
	{
		this.ess = ess;
	}

	@Override
	public void run()
	{
		final long currentTime = System.currentTimeMillis();
		long timeSpent = (currentTime - lastPoll) / 1000;
		if (timeSpent == 0)
		{
			timeSpent = 1;
		}
		if (history.size() > 10)
		{
			history.remove();
		}
		final float tps = 100f / timeSpent;
		if (tps <= 20)
		{
			history.add(tps);
		}
		lastPoll = currentTime;
		for (Player player : ess.getServer().getOnlinePlayers())
		{

			try
			{
				final IUser user = ess.getUser(player);
				if (user == null) {
					continue;
				}
				onlineUsers.add(user);
				user.setLastOnlineActivity(currentTime);
				user.checkActivity();

				boolean mailDisabled = false;
				ISettings settings = ess.getSettings();
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
				if (!mailDisabled && Permissions.MAIL.isAuthorized(user) && !user.gotMailInfo())
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
				ess.getLogger().log(Level.WARNING, "EssentialsTimer Error:", e);
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
			user.resetInvulnerabilityAfterTeleport();
		}
	}

	public float getAverageTPS()
	{
		float avg = 0;
		for (Float f : history)
		{
			if (f != null)
			{
				avg += f;
			}
		}
		return avg / history.size();
	}
}
