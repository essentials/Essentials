package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.textreader.ArrayListInput;
import com.earth2me.essentials.textreader.TextPager;
import java.text.DateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.bukkit.command.CommandSender;


public class Commandbalancetop extends EssentialsCommand
{
	private static final int CACHETIME = 2 * 60 * 1000;
	public static final int MINUSERS = 50;
	private static ArrayListInput cache = new ArrayListInput();
	private static long cacheage = 0;
	private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		int page = 0;
		boolean force = false;
		if (args.length > 0)
		{
			try
			{
				page = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException ex)
			{
				if (args[0].equalsIgnoreCase("force") && sender.isOp())
				{
					force = true;
				}
			}
		}

		if (!force && lock.readLock().tryLock())
		{
			try
			{
				if (cacheage > System.currentTimeMillis() - CACHETIME)
				{
					outputCache(sender, page);
					return;
				}
				if (getContext().getUsers().getUniqueUsers() > MINUSERS)
				{
					sender.sendMessage($("orderBalances", getContext().getUsers().getUniqueUsers()));
				}
			}
			finally
			{
				lock.readLock().unlock();
			}
			getContext().getScheduler().scheduleAsyncDelayedTask(new Viewer(sender, page, force));
		}
		else
		{
			if (getContext().getUsers().getUniqueUsers() > MINUSERS)
			{
				sender.sendMessage($("orderBalances", getContext().getUsers().getUniqueUsers()));
			}
			getContext().getScheduler().scheduleAsyncDelayedTask(new Viewer(sender, page, force));
		}

	}

	private static void outputCache(final CommandSender sender, int page)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(cacheage);
		final DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		sender.sendMessage($("balanceTop", format.format(cal.getTime())));
		new TextPager(cache).showPage(Integer.toString(page), "", "balancetop", sender);
	}


	private class Calculator implements Runnable
	{
		private final transient Viewer viewer;
		private final boolean force;

		public Calculator(final Viewer viewer, final boolean force)
		{
			this.viewer = viewer;
			this.force = force;
		}

		@Override
		public void run()
		{
			lock.writeLock().lock();
			try
			{
				if (force || cacheage <= System.currentTimeMillis() - CACHETIME)
				{
					cache.getLines().clear();
					final Map<String, Double> balances = new HashMap<String, Double>();
					for (String u : getContext().getUsers().getAllUniqueUsers())
					{
						final IUserComponent user = getContext().getUsers().getUser(u);
						if (user != null)
						{
							balances.put(user.getDisplayName(), user.getMoney());
						}
					}

					final List<Map.Entry<String, Double>> sortedEntries = new ArrayList<Map.Entry<String, Double>>(balances.entrySet());
					Collections.sort(sortedEntries, new Comparator<Map.Entry<String, Double>>()
					{
						@Override
						public int compare(final Entry<String, Double> entry1, final Entry<String, Double> entry2)
						{
							return -entry1.getValue().compareTo(entry2.getValue());
						}
					});
					int pos = 1;
					for (Map.Entry<String, Double> entry : sortedEntries)
					{
						cache.getLines().add(pos + ". " + entry.getKey() + ", " + Util.formatCurrency(entry.getValue(), getContext()));
						pos++;
					}
					cacheage = System.currentTimeMillis();
				}
			}
			finally
			{
				lock.writeLock().unlock();
			}
			getContext().getScheduler().scheduleAsyncDelayedTask(viewer);
		}
	}


	private class Viewer implements Runnable
	{
		private final transient CommandSender sender;
		private final transient int page;
		private final transient boolean force;

		public Viewer(final CommandSender sender, final int page, final boolean force)
		{
			this.sender = sender;
			this.page = page;
			this.force = force;
		}

		@Override
		public void run()
		{
			lock.readLock().lock();
			try
			{
				if (!force && cacheage > System.currentTimeMillis() - CACHETIME)
				{
					outputCache(sender, page);
					return;
				}
			}
			finally
			{
				lock.readLock().unlock();
			}
			getContext().getScheduler().scheduleAsyncDelayedTask(new Calculator(new Viewer(sender, page, force), force));
		}
	}
}
