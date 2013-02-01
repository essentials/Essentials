package net.ess3.commands;

import java.text.DateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.user.UserData;
import net.ess3.utils.FormatUtil;
import net.ess3.utils.textreader.ArrayListInput;
import net.ess3.utils.textreader.TextPager;
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
				if (args[0].equalsIgnoreCase("hide"))
				{
					if (args.length == 1 && isUser(sender) && Permissions.BALANCETOP_HIDE.isAuthorized(sender))
					{
						final IUser user = getUser(sender);
						user.getData().setBalancetopHide(!user.getData().isBalancetopHide());
						user.queueSave();
						sender.sendMessage(
								user.getData().isBalancetopHide() ? _("baltopHidden") : _("baltopShown"));
					}
					else if (args.length == 2 && Permissions.BALANCETOP_HIDE_OTHERS.isAuthorized(sender))
					{
						final IUser user = ess.getUserMap().matchUser(args[1], true);
						final UserData userData = user.getData();
						userData.setBalancetopHide(!userData.isBalancetopHide());
						user.queueSave();
						sender.sendMessage(
								userData.isBalancetopHide() ? user.getName() + _("userBaltopHidden") : user.getName() + _("userBaltopShown"));
					}
					else
					{
						throw new NotEnoughArgumentsException();
					}
				}
				if (args[0].equalsIgnoreCase("force") && Permissions.BALANCETOP_FORCE.isAuthorized(sender))
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
				if (ess.getUserMap().getUniqueUsers() > MINUSERS)
				{
					sender.sendMessage(_("orderBalances", ess.getUserMap().getUniqueUsers()));
				}
			}
			finally
			{
				lock.readLock().unlock();
			}
			ess.getPlugin().runTaskAsynchronously(new Viewer(sender, page, force));
		}
		else
		{
			if (ess.getUserMap().getUniqueUsers() > MINUSERS)
			{
				sender.sendMessage(_("orderBalances", ess.getUserMap().getUniqueUsers()));
			}
			ess.getPlugin().runTaskAsynchronously(new Viewer(sender, page, force));
		}

	}

	private static void outputCache(final CommandSender sender, int page)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(cacheage);
		final DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		sender.sendMessage(_("balanceTop", format.format(cal.getTime())));
		new TextPager(cache).showPage(Integer.toString(page), "", "balancetop", sender);
	}


	private class Calculator implements Runnable
	{
		private final Viewer viewer;
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
					double totalMoney = 0d;
					for (String u : ess.getUserMap().getAllUniqueUsers())
					{
						final IUser user = ess.getUserMap().getUser(u);
						if (user != null)
						{
							final double userMoney = user.getMoney();
							user.updateMoneyCache(userMoney);
							if (!user.getData().isBalancetopHide())
							{
								totalMoney += userMoney;
								balances.put(user.getName() /* TODO: Can use 'u' var? */, userMoney);
							}
						}
					}

					final List<Map.Entry<String, Double>> sortedEntries = new ArrayList<Map.Entry<String, Double>>(balances.entrySet());
					Collections.sort(
							sortedEntries, new Comparator<Map.Entry<String, Double>>()
					{
						@Override
						public int compare(final Entry<String, Double> entry1, final Entry<String, Double> entry2)
						{
							return -entry1.getValue().compareTo(entry2.getValue());
						}
					});

					cache.getLines().add(_("serverTotal", FormatUtil.displayCurrency(totalMoney, ess)));
					int pos = 1;
					for (Map.Entry<String, Double> entry : sortedEntries)
					{
						cache.getLines().add(pos + ". " + entry.getKey() + ", " + FormatUtil.displayCurrency(entry.getValue(), ess));
						pos++;
					}
					cacheage = System.currentTimeMillis();
				}
			}
			finally
			{
				lock.writeLock().unlock();
			}
			ess.getPlugin().runTaskAsynchronously(viewer);
		}
	}


	private class Viewer implements Runnable
	{
		private final CommandSender sender;
		private final int page;
		private final boolean force;

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
			ess.getPlugin().runTaskAsynchronously(new Calculator(new Viewer(sender, page, force), force));
		}
	}
}
