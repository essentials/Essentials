package com.earth2me.essentials.components.users;

import com.earth2me.essentials.Console;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.api.*;
import com.earth2me.essentials.components.economy.ChargeException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.craftbukkit.InventoryWorkaround;
import com.earth2me.essentials.perm.Permissions;
import com.earth2me.essentials.register.payment.Method;
import com.earth2me.essentials.register.payment.PaymentMethods;
import com.earth2me.essentials.storage.LocationData;
import com.earth2me.essentials.storage.SubStorageComponent;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import lombok.Cleanup;
import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.ServerOperator;


@SuppressWarnings("deprecation")
public class UserComponent extends SubStorageComponent<UserSurrogate, IEssentials> implements IUserComponent
{
	@Delegate(types =
	{
		IStatelessPlayer.class, Player.class, LivingEntity.class, CommandSender.class, ServerOperator.class, ConfigurationSerializable.class
	})
	@Getter
	@Setter
	private transient IStatelessPlayer base;
	@Getter
	@Setter
	private transient CommandSender replyTo = null;
	@Getter
	private transient IUserComponent teleportRequester;
	@Getter
	private transient boolean teleportRequestHere;
	@Getter
	private transient final ITeleporter teleporter;
	@Getter
	private transient long teleportRequestTime;
	@Getter
	@Setter
	private transient long lastOnlineActivity;
	private transient long lastActivity = System.currentTimeMillis();
	@Getter
	@Setter
	private transient boolean hidden;
	private transient String lowerName;
	@Getter
	@Setter
	private transient Location afkPosition;
	private transient AtomicBoolean gotMailInfo = new AtomicBoolean(false);

	public UserComponent(final OfflinePlayer base, final IContext context)
	{
		this(new StatelessPlayer(base), context);
	}

	public UserComponent(final IStatelessPlayer base, final IContext context)
	{
		super(context, UserSurrogate.class, context.getEssentials());
		this.base = base;
		this.teleporter = new Teleporter(this, context);
		
		getName();
	}

	@Override
	public LocationData getLastLocation()
	{
		acquireReadLock();
		try
		{
			return getData().getLastLocation();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setLastLocation(final LocationData lastLocation)
	{
		acquireWriteLock();
		try
		{
			getData().setLastLocation(lastLocation);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public long getTimeStamp(final TimeStampType name)
	{
		acquireReadLock();
		try
		{
			if (getData().getTimeStamps() == null)
			{
				return 0;
			}
			Long ts = getData().getTimeStamps().get(name);
			return ts == null ? 0 : ts;
		}
		finally
		{
			unlock();
		}
	}

	@SuppressWarnings("MapReplaceableByEnumMap")
	@Override
	public void setTimeStamp(final TimeStampType name, final long value)
	{
		acquireWriteLock();
		try
		{
			if (getData().getTimeStamps() == null)
			{
				// TODO Replace this with an EnumSet.
				getData().setTimeStamps(new HashMap<TimeStampType, Long>());
			}
			getData().getTimeStamps().put(name, value);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setMoney(final double value)
	{
		if (PaymentMethods.hasMethod())
		{
			final Method method = PaymentMethods.getMethod();
			if (method.hasAccount(this.getName()))
			{
				final Method.MethodAccount account = PaymentMethods.getMethod().getAccount(this.getName());
				account.set(value);
			}
		}

		acquireWriteLock();
		try
		{
			final ISettingsComponent settings = getContext().getSettings();
			settings.acquireReadLock();
			if (Math.abs(value) > settings.getData().getEconomy().getMaxMoney())
			{
				getData().setMoney(value < 0 ? -settings.getData().getEconomy().getMaxMoney() : settings.getData().getEconomy().getMaxMoney());
			}
			else
			{
				getData().setMoney(value);
			}
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setHome(String name, Location location)
	{
		Map<String, LocationData> homes = getUnsafeHomes();
		if (homes == null)
		{
			homes = new HashMap<String, LocationData>();
			setHomes(homes);
		}

		acquireWriteLock();
		try
		{
			homes.put(Util.sanitizeKey(name.toLowerCase(Locale.ENGLISH)).intern(), new LocationData(location));
		}
		finally
		{
			unlock();
		}
	}

	private void setHomes(final Map<String, LocationData> homes)
	{
		acquireWriteLock();
		try
		{
			getData().setHomes(homes);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean toggleAfk()
	{
		acquireWriteLock();
		try
		{
			final boolean toggle = !getData().isAfk();
			getData().setAfk(toggle);
			return toggle;
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean toggleMuted()
	{
		acquireWriteLock();
		try
		{
			final boolean toggle = !getData().isMuted();
			getData().setMuted(toggle);
			return toggle;
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean toggleSocialSpy()
	{
		acquireWriteLock();
		try
		{
			final boolean toggle = !getData().isSocialSpy();
			getData().setSocialSpy(toggle);
			return toggle;
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean toggleTeleportEnabled()
	{
		acquireWriteLock();
		try
		{
			final boolean toggle = !getData().isTeleportEnabled();
			getData().setTeleportEnabled(toggle);
			return toggle;
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean isIgnoringPlayer(final String name)
	{
		acquireReadLock();
		try
		{
			return getData().getIgnore().contains(name.toLowerCase(Locale.ENGLISH).intern());
		}
		finally
		{
			unlock();
		}
	}

	private Set<String> getUnsafeIgnore()
	{
		acquireReadLock();
		try
		{
			return getData().getIgnore();
		}
		finally
		{
			unlock();
		}
	}

	private void setIgnore(Set<String> ignore)
	{
		acquireWriteLock();
		try
		{
			getData().setIgnore(ignore);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setIgnoredPlayer(final String name, final boolean set)
	{
		Set<String> ignore = getUnsafeIgnore();
		if (ignore == null)
		{
			ignore = new HashSet<String>();
			setIgnore(ignore);
		}

		final String lname = name.toLowerCase(Locale.ENGLISH).intern();
		acquireWriteLock();
		try
		{
			if (set)
			{
				ignore.add(lname);
			}
			else
			{
				ignore.remove(lname);
			}
		}
		finally
		{
			unlock();
		}
	}

	private void setMails(List<String> mails)
	{
		acquireWriteLock();
		try
		{
			getData().setMails(mails);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void addMail(String string)
	{
		List<String> mails = getUnsafeMails();
		if (mails == null)
		{
			mails = new ArrayList<String>();
			setMails(mails);
		}

		acquireWriteLock();
		try
		{
			mails.add(string);
		}
		finally
		{
			unlock();
		}

		gotMailInfo.set(false);
	}

	@Override
	public List<String> getMails()
	{
		return Collections.unmodifiableList(getUnsafeMails());
	}

	private List<String> getUnsafeMails()
	{
		acquireReadLock();
		try
		{
			return getData().getMails();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public Location getHome(Location loc)
	{
		acquireReadLock();
		try
		{
			if (getData().getHomes() == null)
			{
				return null;
			}
			ArrayList<Location> worldHomes = new ArrayList<Location>();
			for (LocationData location : getData().getHomes().values())
			{
				if (location.getWorldName().equals(loc.getWorld().getName()))
				{
					try
					{
						worldHomes.add(location.getBukkitLocation());
					}
					catch (LocationData.WorldNotLoadedException ex)
					{
						continue;
					}
				}
			}
			if (worldHomes.isEmpty())
			{
				return null;
			}
			if (worldHomes.size() == 1)
			{
				return worldHomes.get(0);
			}
			double distance = Double.MAX_VALUE;
			Location target = null;
			for (Location location : worldHomes)
			{
				final double d = loc.distanceSquared(location);
				if (d < distance)
				{
					target = location;
					distance = d;
				}
			}
			return target;
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void checkCooldown(final TimeStampType cooldownType, final double cooldown, final boolean set, final IPermissions bypassPermission) throws CooldownException
	{
		final Calendar now = new GregorianCalendar();
		if (getTimeStamp(cooldownType) > 0)
		{
			final Calendar cooldownTime = new GregorianCalendar();
			cooldownTime.setTimeInMillis(getTimeStamp(cooldownType));
			cooldownTime.add(Calendar.SECOND, (int)cooldown);
			cooldownTime.add(Calendar.MILLISECOND, (int)((cooldown * 1000.0) % 1000.0));
			if (cooldownTime.after(now) && !bypassPermission.isAuthorized(this))
			{
				throw new CooldownException(Util.formatDateDiff(cooldownTime.getTimeInMillis()));
			}
		}
		if (set)
		{
			setTimeStamp(cooldownType, now.getTimeInMillis());
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
			sendMessage($("addedToAccount", Util.formatCurrency(value, getContext())));
			if (initiator != null)
			{
				initiator.sendMessage($("addedToOthersAccount", Util.formatCurrency(value, getContext()), this.getDisplayName()));
			}
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void payUser(final IUserComponent receiver, final double value) throws Exception
	{
		if (value == 0)
		{
			return;
		}
		if (canAfford(value))
		{
			setMoney(getMoney() - value);
			receiver.setMoney(receiver.getMoney() + value);
			sendMessage($("moneySentTo", Util.formatCurrency(value, getContext()), receiver.getDisplayName()));
			receiver.sendMessage($("moneyRecievedFrom", Util.formatCurrency(value, getContext()), getDisplayName()));
		}
		else
		{
			throw new Exception($("notEnoughMoney"));
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
		sendMessage($("takenFromAccount", Util.formatCurrency(value, getContext())));
		if (initiator != null)
		{
			initiator.sendMessage($("takenFromOthersAccount", Util.formatCurrency(value, getContext()), this.getDisplayName()));
		}
	}

	@Override
	public boolean canAfford(final double cost)
	{
		final double mon = getMoney();
		return mon >= cost || Permissions.ECO_LOAN.isAuthorized(this);
	}

	@Override
	public void setHome()
	{
		setHome("home", getLocation());
	}

	@Override
	public void setHome(final String name)
	{
		setHome(name, getLocation());
	}

	@Override
	public void setLastLocation()
	{
		acquireWriteLock();
		try
		{
			getData().setLastLocation(new com.earth2me.essentials.storage.LocationData(getLocation()));
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void requestTeleport(final IUserComponent player, final boolean here)
	{
		teleportRequestTime = System.currentTimeMillis();
		teleportRequester = player;
		teleportRequestHere = here;
	}

	@Override
	public String getNick(boolean addprefixsuffix)
	{
		acquireReadLock();
		try
		{
			final String nick = getData().getNickName();
			@Cleanup(value = "unlock")
			final ISettingsComponent settings = getContext().getSettings();
			settings.acquireReadLock();
			final IGroupsComponent groups = getContext().getGroups();
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
			displayname = displayname.replace("{WORLDNAME}", this.getWorld().getName());
			displayname = displayname.replace('&', '§');
			displayname = displayname.concat("§f");

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
		setDisplayName(name);
		if (name.length() > 16)
		{
			name = getNick(false);
		}
		if (name.length() > 16)
		{
			name = name.substring(0, name.charAt(15) == '§' ? 15 : 16);
		}
		try
		{
			setPlayerListName(name);
		}
		catch (IllegalArgumentException e)
		{
			getContext().getLogger().log(Level.INFO, "Playerlist for {0} was not updated. Use a shorter displayname prefix.", name);
		}
	}

	@Override
	public void updateDisplayName()
	{
		@Cleanup
		final ISettingsComponent settings = getContext().getSettings();
		settings.acquireReadLock();
		if (isOnline() && settings.getData().getChat().getChangeDisplayname())
		{
			setDisplayNick();
		}
	}

	@Override
	public double getMoney()
	{
		if (PaymentMethods.hasMethod())
		{
			try
			{
				final Method method = PaymentMethods.getMethod();
				if (!method.hasAccount(this.getName()))
				{
					throw new Exception();
				}
				final Method.MethodAccount account = PaymentMethods.getMethod().getAccount(this.getName());
				return account.balance();
			}
			catch (Throwable ex)
			{
			}
		}

		acquireReadLock();
		try
		{
			Double money = getData().getMoney();
			final ISettingsComponent settings = getContext().getSettings();
			settings.acquireReadLock();
			if (money == null)
			{
				money = settings.getData().getEconomy().getStartingBalance();
			}
			if (Math.abs(money) > settings.getData().getEconomy().getMaxMoney())
			{
				money = money < 0 ? -settings.getData().getEconomy().getMaxMoney() : settings.getData().getEconomy().getMaxMoney();
			}
			return money;
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setAfk(final boolean set)
	{
		acquireWriteLock();
		try
		{
			this.setSleepingIgnored(Permissions.SLEEPINGIGNORED.isAuthorized(this) ? true : set);
			if (set && !getData().isAfk())
			{
				afkPosition = getLocation();
			}
			getData().setAfk(set);
		}
		finally
		{
			unlock();
		}
	}

	//Returns true if status expired during this check
	@Override
	public boolean checkJailTimeout(final long currentTime)
	{
		acquireReadLock();
		try
		{
			if (getTimeStamp(TimeStampType.JAIL) > 0 && getTimeStamp(TimeStampType.JAIL) < currentTime && getData().isJailed())
			{
				acquireWriteLock();

				setTimeStamp(TimeStampType.JAIL, 0);
				getData().setJailed(false);
				sendMessage($("haveBeenReleased"));
				getData().setJail(null);

				try
				{
					teleporter.back();
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
			if (getTimeStamp(TimeStampType.MUTE) > 0 && getTimeStamp(TimeStampType.MUTE) < currentTime && getData().isMuted())
			{
				acquireWriteLock();
				setTimeStamp(TimeStampType.MUTE, 0);
				sendMessage($("canTalkAgain"));
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
					getContext().getMessager().broadcastMessage(this, $("userIsNotAway", getDisplayName()));
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
		// Do NOT @Cleanup this.
		final ISettingsComponent settings = getContext().getSettings();
		settings.acquireReadLock();
		final long autoafkkick = settings.getData().getCommands().getAfk().getAutoAFKKick();
		if (autoafkkick > 0 && lastActivity > 0 && (lastActivity + (autoafkkick * 1000)) < System.currentTimeMillis()
			&& !hidden
			&& !Permissions.KICK_EXEMPT.isAuthorized(this)
			&& !Permissions.AFK_KICKEXEMPT.isAuthorized(this))
		{
			final String kickReason = $("autoAfkKickReason", autoafkkick / 60.0);
			lastActivity = 0;
			kickPlayer(kickReason);


			for (Player player : getContext().getServer().getOnlinePlayers())
			{
				final IUserComponent user = getContext().getUser(player);
				if (Permissions.KICK_NOTIFY.isAuthorized(user))
				{
					player.sendMessage($("playerKicked", Console.NAME, getName(), kickReason));
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
					getContext().getMessager().broadcastMessage(this, $("userIsAway", getDisplayName()));
				}
			}
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean toggleGodModeEnabled()
	{
		if (!isGodModeEnabled())
		{
			setFoodLevel(20);
		}

		acquireWriteLock();
		try
		{
			final boolean toggle = !getData().isGodModeEnabled();
			getData().setGodModeEnabled(toggle);
			return toggle;
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean isGodModeEnabled()
	{
		acquireReadLock();
		try
		{
			@Cleanup
			final ISettingsComponent settings = getContext().getSettings();
			settings.acquireReadLock();
			return (getData().isGodModeEnabled()
					&& !settings.getData().getWorldOptions(getLocation().getWorld().getName()).isGodmode())
				   || (getData().isAfk() && settings.getData().getCommands().getAfk().isFreezeAFKPlayers());
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public Location getHome(String name)
	{
		final Map<String, LocationData> homes = getHomes();
		final String lname = name.toLowerCase(Locale.ENGLISH).intern();
		if (homes.containsKey(lname))
		{
			try
			{
				return homes.get(lname).getBukkitLocation();
			}
			catch (Throwable ex)
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public void updateCompass()
	{
		try
		{
			Location loc = getHome(getLocation());
			if (loc == null)
			{
				loc = getBedSpawnLocation();
			}
			if (loc != null)
			{
				setCompassTarget(loc);
			}
		}
		catch (Exception ex)
		{
			// Ignore
		}
	}

	@Override
	public void giveItems(ItemStack itemStack, Boolean canSpew) throws ChargeException
	{
		if (giveItemStack(itemStack, canSpew))
		{
			sendMessage($("InvFull"));
		}
		updateInventory();
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
			sendMessage($("InvFull"));
		}
		updateInventory();
	}

	@Override
	public Map<String, LocationData> getHomes()
	{
		return Collections.unmodifiableMap(getUnsafeHomes());
	}

	private Map<String, LocationData> getUnsafeHomes()
	{
		acquireReadLock();
		try
		{
			return getData().getHomes();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean gotMailInfo()
	{
		return gotMailInfo.get();
	}

	@Override
	public void close()
	{
		base = null;

		super.close();
	}

	@Override
	public int compareTo(final IUserComponent t)
	{
		return Util.stripColor(this.getDisplayName()).compareTo(Util.stripColor(t.getDisplayName()));
	}

	private boolean giveItemStack(ItemStack itemStack, Boolean canSpew) throws ChargeException
	{
		boolean spew = false;

		if (itemStack == null || itemStack.getType() == Material.AIR)
		{
			return spew;
		}

		final Map<Integer, ItemStack> overfilled;
		if (Permissions.OVERSIZEDSTACKS.isAuthorized(this))
		{
			final ISettingsComponent settings = getContext().getSettings();
			int oversizedStackSize;
			settings.acquireReadLock();
			try
			{
				oversizedStackSize = settings.getData().getGeneral().getOversizedStacksize();
			}
			finally
			{
				settings.unlock();
			}
			overfilled = InventoryWorkaround.addItem(getInventory(), true, oversizedStackSize, itemStack);
		}
		else
		{
			overfilled = InventoryWorkaround.addItem(getInventory(), true, itemStack);
		}
		if (canSpew)
		{
			for (ItemStack overflowStack : overfilled.values())
			{
				getWorld().dropItemNaturally(getLocation(), overflowStack);
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
	public boolean isMuted()
	{
		acquireReadLock();
		try
		{
			return getData().isMuted();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setMuted(boolean muted)
	{
		acquireWriteLock();
		try
		{
			getData().setMuted(muted);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setLastInventory(final Inventory inventory)
	{
		acquireWriteLock();
		try
		{
			getData().setLastInventory(inventory);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public Inventory getLastInventory()
	{
		acquireReadLock();
		try
		{
			return getData().getLastInventory();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setIpAddress(String ipAddress)
	{
		acquireWriteLock();
		try
		{
			getData().setIpAddress(ipAddress);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public String getIpAddress()
	{
		acquireReadLock();
		try
		{
			return getData().getIpAddress();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setNpc(boolean npc)
	{
		acquireWriteLock();
		try
		{
			getData().setNpc(npc);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean isNpc()
	{
		acquireReadLock();
		try
		{
			return getData().isNpc();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public Ban getBan()
	{
		acquireReadLock();
		try
		{
			return getData().getBan();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setBan(Ban ban)
	{
		acquireWriteLock();
		try
		{
			getData().setBan(ban);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean hasPowerTools()
	{
		acquireReadLock();
		try
		{
			return getData().hasPowerTools();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean isPowerToolsEnabled()
	{
		acquireReadLock();
		try
		{
			return getData().isPowerToolsEnabled();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean isSocialSpy()
	{
		acquireReadLock();
		try
		{
			return getData().isSocialSpy();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setSocialSpy(boolean socialSpy)
	{
		acquireWriteLock();
		try
		{
			getData().setSocialSpy(socialSpy);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setPowerToolsEnabled(boolean powerToolsEnabled)
	{
		acquireWriteLock();
		try
		{
			getData().setPowerToolsEnabled(powerToolsEnabled);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public List<String> getPowerTool(Material material)
	{
		acquireReadLock();
		try
		{
			return getData().getPowerTool(material);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setPowerTool(Material material, List<String> powerTool)
	{
		try
		{
			getData().getPowerTools().put(material, powerTool);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean isAfk()
	{
		acquireReadLock();
		try
		{
			return getData().isAfk();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean hasUnlimited(Material type)
	{
		acquireReadLock();
		try
		{
			return getData().hasUnlimited(type);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setJail(String jail)
	{
		acquireWriteLock();
		try
		{
			getData().setJail(jail);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean isJailed()
	{
		acquireReadLock();
		try
		{
			return getData().isJailed();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public String getJail()
	{
		acquireReadLock();
		try
		{
			return getData().getJail();
		}
		finally
		{
			unlock();
		}
	}
}