package net.ess3.user;

import java.lang.ref.WeakReference;
import static net.ess3.I18n._;
import net.ess3.Teleport;
import net.ess3.api.*;
import net.ess3.economy.register.Method;
import net.ess3.permissions.Permissions;
import net.ess3.utils.DateUtil;
import net.ess3.utils.Util;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import net.ess3.Console;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class User extends UserBase implements IUser
{
	private CommandSender replyTo = null;
	@Getter
	private transient IUser teleportRequester;
	@Getter
	private transient boolean tpRequestHere;
	@Getter
	private transient final ITeleport teleport;
	@Getter
	private transient long teleportRequestTime;
	@Getter
	@Setter
	private transient long lastOnlineActivity;
	private transient long lastActivity = System.currentTimeMillis();
	@Getter
	@Setter
	private boolean hidden = false;
	@Getter
	private transient boolean vanished;
	@Getter
	@Setter
	private boolean invSee = false;
	private transient Location afkPosition;
	private AtomicBoolean gotMailInfo = new AtomicBoolean(false);
	private WeakReference<Player> playerCache;

	public User(final OfflinePlayer base, final IEssentials ess)
	{
		super(base, ess);
		teleport = new Teleport(this, ess);
	}

	@Override
	public void setPlayerCache(final Player player)
	{
		playerCache = new WeakReference<Player>(player);
	}

	private void destroyPlayerCache()
	{
		playerCache = null;
	}

	@Override
	public void close()
	{
		super.close();
		destroyPlayerCache();
	}

	@Override
	public Player getPlayer()
	{
		Player player = playerCache == null ? null : playerCache.get();
		if (player == null)
		{
			player = super.getPlayer();
		}
		return player;
	}

	public void example()
	{
		// Cleanup will call close at the end of the function
		@Cleanup
		final User user = this;

		// read lock allows to read data from the user
		user.acquireReadLock();
		final double money = user.getData().getMoney();

		// write lock allows only one thread to modify the data
		user.acquireWriteLock();
		user.getData().setMoney(10 + money);
	}

	@Override
	public void finishRead()
	{
	}

	@Override
	public void finishWrite()
	{
	}

	@Override
	public void checkCooldown(final UserData.TimestampType cooldownType, final double cooldown, final boolean set, final IPermission bypassPermission) throws CooldownException
	{
		final Calendar now = new GregorianCalendar();
		if (getTimestamp(cooldownType) > 0)
		{
			final Calendar cooldownTime = new GregorianCalendar();
			cooldownTime.setTimeInMillis(getTimestamp(cooldownType));
			cooldownTime.add(Calendar.SECOND, (int)cooldown);
			cooldownTime.add(Calendar.MILLISECOND, (int)((cooldown * 1000.0) % 1000.0));
			if (cooldownTime.after(now) && !bypassPermission.isAuthorized(this))
			{
				throw new CooldownException(DateUtil.formatDateDiff(cooldownTime.getTimeInMillis()));
			}
		}
		if (set)
		{
			setTimestamp(cooldownType, now.getTimeInMillis());
		}
	}

	@Override
	public void giveMoney(final double value)
	{
		giveMoney(value, null);
	}

	@Override
	public void giveMoney(final double value, final CommandSender initiator)
	{

		if (value == 0)
		{
			return;
		}
		acquireWriteLock();
		try
		{
			setMoney(getMoney() + value);
			sendMessage(_("addedToAccount", Util.displayCurrency(value, ess)));
			if (initiator != null)
			{
				initiator.sendMessage(_("addedToOthersAccount", Util.displayCurrency(value, ess), this.getPlayer().getDisplayName()));
			}
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void payUser(final IUser reciever, final double value) throws Exception
	{
		if (value == 0)
		{
			return;
		}
		if (canAfford(value))
		{
			setMoney(getMoney() - value);
			reciever.setMoney(reciever.getMoney() + value);
			sendMessage(_("moneySentTo", Util.displayCurrency(value, ess), reciever.getPlayer().getDisplayName()));
			reciever.sendMessage(_("moneyRecievedFrom", Util.displayCurrency(value, ess), getPlayer().getDisplayName()));
		}
		else
		{
			throw new Exception(_("notEnoughMoney"));
		}
	}

	@Override
	public void takeMoney(final double value)
	{
		takeMoney(value, null);
	}

	@Override
	public void takeMoney(final double value, final CommandSender initiator)
	{
		if (value == 0)
		{
			return;
		}
		setMoney(getMoney() - value);
		sendMessage(_("takenFromAccount", Util.displayCurrency(value, ess)));
		if (initiator != null)
		{
			initiator.sendMessage(_("takenFromOthersAccount", Util.displayCurrency(value, ess), this.getPlayer().getDisplayName()));
		}
	}

	public void setHome()
	{
		setHome("home", getPlayer().getLocation());
	}

	public void setHome(final String name)
	{
		setHome(name, getPlayer().getLocation());
	}

	@Override
	public void setLastLocation()
	{
		acquireWriteLock();
		try
		{
			getData().setLastLocation(new net.ess3.storage.StoredLocation(getPlayer().getLocation()));
		}
		finally
		{
			unlock();
		}
	}

	public String getNick(boolean addprefixsuffix)
	{
		acquireReadLock();
		try
		{
			final String nick = getData().getNickname();
			@Cleanup
			final ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			final IRanks groups = ess.getRanks();
			// default: {PREFIX}{NICKNAMEPREFIX}{NAME}{SUFFIX}
			String displayname = settings.getData().getChat().getDisplaynameFormat();
			if (settings.getData().getCommands().isDisabled("nick") || nick == null || nick.isEmpty() || nick.equals(getName()))
			{
				displayname = displayname.replace("{NAME}", getName());
				displayname = displayname.replace("{NICKNAMEPREFIX}", "");
			}
			else
			{
				displayname = displayname.replace("{NAME}", nick);
				displayname = displayname.replace("{NICKNAMEPREFIX}", settings.getData().getChat().getNicknamePrefix());
			}

			if (displayname.contains("{PREFIX}"))
			{
				displayname = displayname.replace("{PREFIX}", groups.getPrefix(this));
			}
			if (displayname.contains("{SUFFIX}"))
			{
				displayname = displayname.replace("{SUFFIX}", groups.getSuffix(this));
			}
			displayname = displayname.replace("{WORLDNAME}", this.getPlayer().getWorld().getName());
			displayname = displayname.replace('&', '\u00a7');
			displayname = displayname.concat("\u00a7f");

			return displayname;
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setDisplayNick()
	{
		String name = getNick(true);
		getPlayer().setDisplayName(name);
		if (name.length() > 16)
		{
			name = getNick(false);
		}
		if (name.length() > 16)
		{
			name = name.substring(0, name.charAt(15) == '\u00a7' ? 15 : 16);
		}
		try
		{
			getPlayer().setPlayerListName(name);
		}
		catch (IllegalArgumentException e)
		{
			ess.getLogger().info("Playerlist for " + name + " was not updated. Use a shorter displayname prefix.");
		}
	}

	@Override
	public void updateDisplayName()
	{
		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		if (isOnline() && settings.getData().getChat().getChangeDisplayname())
		{
			setDisplayNick();
		}
	}

	@Override
	public double getMoney()
	{
		if (ess.getPaymentMethod().hasMethod())
		{
			try
			{
				final Method method = ess.getPaymentMethod().getMethod();
				if (!method.hasAccount(this.getName()))
				{
					throw new Exception();
				}
				final Method.MethodAccount account = ess.getPaymentMethod().getMethod().getAccount(this.getName());
				return account.balance();
			}
			catch (Throwable ex)
			{
			}
		}
		return super.getMoney();
	}

	@Override
	public void setMoney(final double value)
	{
		if (ess.getPaymentMethod().hasMethod())
		{
			try
			{
				final Method method = ess.getPaymentMethod().getMethod();
				if (!method.hasAccount(this.getName()))
				{
					throw new Exception();
				}
				final Method.MethodAccount account = ess.getPaymentMethod().getMethod().getAccount(this.getName());
				account.set(value);
			}
			catch (Throwable ex)
			{
			}
		}
		super.setMoney(value);
	}

	public void setAfk(final boolean set)
	{
		acquireWriteLock();
		try
		{
			this.getPlayer().setSleepingIgnored(Permissions.SLEEPINGIGNORED.isAuthorized(this) ? true : set);
			if (set && !getData().isAfk())
			{
				afkPosition = getPlayer().getLocation();
			}
			getData().setAfk(set);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean toggleAfk()
	{
		final boolean now = super.toggleAfk();
		this.getPlayer().setSleepingIgnored(Permissions.SLEEPINGIGNORED.isAuthorized(this) ? true : now);
		return now;
	}

	//Returns true if status expired during this check
	@Override
	public boolean checkJailTimeout(final long currentTime)
	{
		acquireReadLock();
		try
		{
			if (getTimestamp(UserData.TimestampType.JAIL) > 0 && getTimestamp(UserData.TimestampType.JAIL) < currentTime && getData().isJailed())
			{
				acquireWriteLock();

				setTimestamp(UserData.TimestampType.JAIL, 0);
				getData().setJailed(false);
				sendMessage(_("haveBeenReleased"));
				getData().setJail(null);

				try
				{
					teleport.back();
				}
				catch (Exception ex)
				{
				}
				return true;
			}
			return false;
		}
		finally
		{
			unlock();
		}
	}

	//Returns true if status expired during this check
	@Override
	public boolean checkMuteTimeout(final long currentTime)
	{
		acquireReadLock();
		try
		{
			if (getTimestamp(UserData.TimestampType.MUTE) > 0 && getTimestamp(UserData.TimestampType.MUTE) < currentTime && getData().isMuted())
			{
				acquireWriteLock();
				setTimestamp(UserData.TimestampType.MUTE, 0);
				sendMessage(_("canTalkAgain"));
				getData().setMuted(false);
				return true;
			}
			return false;
		}
		finally
		{
			unlock();
		}
	}

	//Returns true if status expired during this check
	@Override
	public boolean checkBanTimeout(final long currentTime)
	{
		acquireReadLock();
		try
		{
			if (getData().getBan() != null && getData().getBan().getTimeout() > 0 && getData().getBan().getTimeout() < currentTime && isBanned())
			{
				acquireWriteLock();
				getData().setBan(null);
				setBanned(false);
				return true;
			}
			return false;
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void updateActivity(final boolean broadcast)
	{
		acquireReadLock();
		try
		{
			if (getData().isAfk())
			{
				acquireWriteLock();
				getData().setAfk(false);
				if (broadcast && !hidden)
				{
					ess.broadcastMessage(this, _("userIsNotAway", getPlayer().getDisplayName()));
				}
			}
			lastActivity = System.currentTimeMillis();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void checkActivity()
	{
		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		final long autoafkkick = settings.getData().getCommands().getAfk().getAutoAFKKick();
		if (autoafkkick > 0 && lastActivity > 0 && (lastActivity + (autoafkkick * 1000)) < System.currentTimeMillis()
			&& !hidden
			&& !Permissions.KICK_EXEMPT.isAuthorized(this)
			&& !Permissions.AFK_KICKEXEMPT.isAuthorized(this))
		{
			final String kickReason = _("autoAfkKickReason", autoafkkick / 60.0);
			lastActivity = 0;
			getPlayer().kickPlayer(kickReason);


			for (Player player : ess.getServer().getOnlinePlayers())
			{
				if (Permissions.KICK_NOTIFY.isAuthorized(player))
				{
					player.sendMessage(_("playerKicked", Console.NAME, getName(), kickReason));
				}
			}
		}
		final long autoafk = settings.getData().getCommands().getAfk().getAutoAFK();
		acquireReadLock();
		try
		{
			if (!getData().isAfk() && autoafk > 0 && lastActivity + autoafk * 1000 < System.currentTimeMillis() && Permissions.AFK.isAuthorized(this))
			{
				setAfk(true);
				if (!hidden)
				{
					ess.broadcastMessage(this, _("userIsAway", getPlayer().getDisplayName()));
				}
			}
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public Location getAfkPosition()
	{
		return afkPosition;
	}

	@Override
	public boolean isGodModeEnabled()
	{
		acquireReadLock();
		try
		{
			@Cleanup
			final ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			return (getData().isGodmode()
					&& !settings.getData().getWorldOptions(getPlayer().getLocation().getWorld().getName()).isGodmode())
				   || (getData().isAfk() && settings.getData().getCommands().getAfk().isFreezeAFKPlayers());
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void updateCompass()
	{
		try
		{
			Location loc = getHome(getPlayer().getLocation());
			if (loc == null)
			{
				loc = getBedSpawnLocation();
			}
			if (loc != null)
			{
				getPlayer().setCompassTarget(loc);
			}
		}
		catch (Exception ex)
		{
			// Ignore
		}
	}

	@Override
	public int compareTo(final IUser t)
	{
		return Util.stripColor(this.getPlayer().getDisplayName()).compareTo(Util.stripColor(t.getPlayer().getDisplayName()));
	}

	@Override
	public void requestTeleport(IUser player, boolean here)
	{
		teleportRequestTime = System.currentTimeMillis();
		teleportRequester = player;
		tpRequestHere = here;
	}

	@Override
	public void setReplyTo(CommandSender user)
	{
		replyTo = user;
	}

	@Override
	public CommandSender getReplyTo()
	{
		return replyTo;
	}

	@Override
	public boolean gotMailInfo()
	{
		return gotMailInfo.getAndSet(true);
	}

	@Override
	public void addMail(String mail)
	{
		super.addMail(mail);
		gotMailInfo.set(false);
	}

	@Override
	public void giveItems(ItemStack itemStack, Boolean canSpew) throws ChargeException
	{
		if (giveItemStack(itemStack, canSpew))
		{
			sendMessage(_("InvFull"));
		}
		getPlayer().updateInventory();
	}

	@Override
	public void giveItems(List<ItemStack> itemStacks, Boolean canSpew) throws ChargeException
	{
		boolean spew = false;
		for (ItemStack itemStack : itemStacks)
		{
			if (giveItemStack(itemStack, canSpew))
			{
				spew = true;
			}
		}
		if (spew)
		{
			sendMessage(_("InvFull"));
		}
		getPlayer().updateInventory();
	}

	private boolean giveItemStack(ItemStack itemStack, Boolean canSpew) throws ChargeException
	{
		boolean spew = false;

		if (itemStack == null || itemStack.getTypeId() == 0)
		{
			return spew;
		}

		final Map<Integer, ItemStack> overfilled;
		if (Permissions.OVERSIZEDSTACKS.isAuthorized(this))
		{
			@Cleanup
			final ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			int oversizedStackSize = settings.getData().getGeneral().getOversizedStacksize();

			overfilled = getPlayer().getInventory().addItem(true, oversizedStackSize, itemStack);
		}
		else
		{
			overfilled = getPlayer().getInventory().addItem(true, itemStack);
		}
		if (canSpew)
		{
			for (ItemStack overflowStack : overfilled.values())
			{
				getPlayer().getWorld().dropItemNaturally(getPlayer().getLocation(), overflowStack);
				spew = true;
			}
		}
		else
		{
			if (!overfilled.isEmpty())
			{
				throw new ChargeException("Inventory full");
			}
		}
		return spew;
	}

	@Override
	public boolean canAfford(final double cost)
	{
		final double mon = getMoney();
		if (Permissions.ECO_LOAN.isAuthorized(this))
		{
			@Cleanup
			final ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			return (mon - cost) >= settings.getData().getEconomy().getMinMoney();
		}
		return cost <= mon;
	}

	@Override
	public void updateMoneyCache(double userMoney)
	{
		if (super.getMoney() != userMoney)
		{
			super.setMoney(userMoney);
		}
	}

	@Override
	public boolean canAfford(double amount, boolean b)
	{
		return true;
	}
	private transient long teleportInvulnerabilityTimestamp = 0;

	public void enableInvulnerabilityAfterTeleport()
	{
		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();

		final long time = settings.getData().getGeneral().getTeleportInvulnerability();
		if (time > 0)
		{
			teleportInvulnerabilityTimestamp = System.currentTimeMillis() + time;
		}
	}

	@Override
	public void resetInvulnerabilityAfterTeleport()
	{
		if (teleportInvulnerabilityTimestamp != 0
			&& teleportInvulnerabilityTimestamp < System.currentTimeMillis())
		{
			teleportInvulnerabilityTimestamp = 0;
		}
	}

	@Override
	public boolean hasInvulnerabilityAfterTeleport()
	{
		return teleportInvulnerabilityTimestamp != 0 && teleportInvulnerabilityTimestamp >= System.currentTimeMillis();
	}

	@Override
	public void setVanished(boolean set)
	{
		vanished = set;
		if (set)
		{
			for (Player p : ess.getServer().getOnlinePlayers())
			{
				if (!Permissions.VANISH_SEE_OTHERS.isAuthorized(ess.getUserMap().getUser(p)))
				{
					p.hidePlayer(getPlayer());
				}
			}
			setHidden(true);
			ess.getVanishedPlayers().add(getName());
		}
		else
		{
			for (Player p : ess.getServer().getOnlinePlayers())
			{
				p.showPlayer(getPlayer());
			}
			setHidden(false);
			ess.getVanishedPlayers().remove(getName());
		}
	}

	@Override
	public void toggleVanished()
	{
		final boolean set = !vanished;
		this.setVanished(set);
	}
}
