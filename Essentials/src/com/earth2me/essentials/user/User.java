package com.earth2me.essentials.user;

import com.earth2me.essentials.Console;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.Teleport;
import com.earth2me.essentials.api.*;
import com.earth2me.essentials.craftbukkit.InventoryWorkaround;
import com.earth2me.essentials.economy.register.Method;
import com.earth2me.essentials.permissions.Permissions;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.Util;
import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


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
	private static final Logger logger = Bukkit.getLogger();
	private AtomicBoolean gotMailInfo = new AtomicBoolean(false);

	public User(final Player base, final IEssentials ess)
	{
		super(base, ess);
		teleport = new Teleport(this, ess);
	}

	public User(final OfflinePlayer offlinePlayer, final IEssentials ess)
	{
		super(offlinePlayer, ess);
		teleport = new Teleport(this, ess);
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
				initiator.sendMessage(_("addedToOthersAccount", Util.displayCurrency(value, ess), this.getDisplayName()));
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
			sendMessage(_("moneySentTo", Util.displayCurrency(value, ess), reciever.getDisplayName()));
			reciever.sendMessage(_("moneyRecievedFrom", Util.displayCurrency(value, ess), getDisplayName()));
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
			initiator.sendMessage(_("takenFromOthersAccount", Util.displayCurrency(value, ess), this.getDisplayName()));
		}
	}

	public void setHome()
	{
		setHome("home", getLocation());
	}

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
			getData().setLastLocation(new com.earth2me.essentials.storage.Location(getLocation()));
		}
		finally
		{
			unlock();
		}
	}

	public void requestTeleport(final User player, final boolean here)
	{
		teleportRequestTime = System.currentTimeMillis();
		teleportRequester = player;
		tpRequestHere = here;
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
			displayname = displayname.replace("{WORLDNAME}", this.getWorld().getName());
			displayname = displayname.replace('&', '\u00a7');
			displayname = displayname.concat("\u00a7f");

			return displayname;
		}
		finally
		{
			unlock();
		}
	}

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
			name = name.substring(0, name.charAt(15) == '\u00a7' ? 15 : 16);
		}
		try
		{
			setPlayerListName(name);
		}
		catch (IllegalArgumentException e)
		{
			logger.info("Playerlist for " + name + " was not updated. Use a shorter displayname prefix.");
		}
	}

	@Override
	public String getDisplayName()
	{
		return super.getDisplayName() == null ? super.getName() : super.getDisplayName();
	}

	@Override
	public void updateDisplayName()
	{
		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		if (isOnlineUser() && settings.getData().getChat().getChangeDisplayname())
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

	@Override
	public boolean toggleAfk()
	{
		final boolean now = super.toggleAfk();
		this.setSleepingIgnored(Permissions.SLEEPINGIGNORED.isAuthorized(this) ? true : now);
		return now;
	}

	//Returns true if status expired during this check
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
					ess.broadcastMessage(this, _("userIsNotAway", getDisplayName()));
				}
			}
			lastActivity = System.currentTimeMillis();
		}
		finally
		{
			unlock();
		}
	}

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
			kickPlayer(kickReason);


			for (Player player : ess.getServer().getOnlinePlayers())
			{
				final IUser user = ess.getUser(player);
				if (Permissions.KICK_NOTIFY.isAuthorized(user))
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
					ess.broadcastMessage(this, _("userIsAway", getDisplayName()));
				}
			}
		}
		finally
		{
			unlock();
		}
	}

	public Location getAfkPosition()
	{
		return afkPosition;
	}

	public boolean toggleGodModeEnabled()
	{
		if (!isGodModeEnabled())
		{
			setFoodLevel(20);
		}
		return super.toggleGodmode();
	}

	public boolean isGodModeEnabled()
	{
		acquireReadLock();
		try
		{
			@Cleanup
			final ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			return (getData().isGodmode()
					&& !settings.getData().getWorldOptions(getLocation().getWorld().getName()).isGodmode())
				   || (getData().isAfk() && settings.getData().getCommands().getAfk().isFreezeAFKPlayers());
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public Location getHome(String name) throws Exception
	{
		throw new UnsupportedOperationException("Not supported yet.");
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
	public List<String> getHomes()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int compareTo(final IUser t)
	{
		return Util.stripColor(this.getDisplayName()).compareTo(Util.stripColor(t.getDisplayName()));
	}

	@Override
	public void requestTeleport(IUser user, boolean b)
	{
		throw new UnsupportedOperationException("Not supported yet.");
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
			sendMessage(_("InvFull"));
		}
		updateInventory();
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
			@Cleanup
			final ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			int oversizedStackSize = settings.getData().getGeneral().getOversizedStacksize();

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
		else {
			if (!overfilled.isEmpty()) {
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
	public void updateMoneyCache(double userMoney) {
		if (super.getMoney() != userMoney) {
			super.setMoney(userMoney);
		}
	}

	@Override
	public boolean canAfford(double amount, boolean b) {
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
	
	public void toggleVanished()
	{
		vanished = !vanished;
		if (vanished)
		{
			ess.getVanishedPlayers().add(getName());
		}
		else
		{
			ess.getVanishedPlayers().remove(getName());
		}
	}
	
	
	//start//
	@Override
	public void setDisplayName(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getPlayerListName()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setPlayerListName(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setCompassTarget(Location lctn)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Location getCompassTarget()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public InetSocketAddress getAddress()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void sendRawMessage(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void kickPlayer(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void chat(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean performCommand(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isSneaking()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setSneaking(boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isSprinting()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setSprinting(boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void saveData()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void loadData()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setSleepingIgnored(boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isSleepingIgnored()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void playNote(Location lctn, byte b, byte b1)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void playNote(Location lctn, Instrument i, Note note)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void playEffect(Location lctn, Effect effect, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public <T> void playEffect(Location lctn, Effect effect, T t)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void sendBlockChange(Location lctn, Material mtrl, byte b)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean sendChunkChange(Location lctn, int i, int i1, int i2, byte[] bytes)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void sendBlockChange(Location lctn, int i, byte b)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void sendMap(MapView mv)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void updateInventory()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void awardAchievement(Achievement a)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void incrementStatistic(Statistic ststc)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void incrementStatistic(Statistic ststc, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void incrementStatistic(Statistic ststc, Material mtrl)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void incrementStatistic(Statistic ststc, Material mtrl, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setPlayerTime(long l, boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long getPlayerTime()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long getPlayerTimeOffset()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isPlayerTimeRelative()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void resetPlayerTime()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void giveExp(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float getExp()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setExp(float f)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getLevel()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setLevel(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getTotalExperience()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setTotalExperience(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float getExhaustion()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setExhaustion(float f)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float getSaturation()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setSaturation(float f)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getFoodLevel()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setFoodLevel(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setBedSpawnLocation(Location lctn)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean getAllowFlight()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setAllowFlight(boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void hidePlayer(Player player)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void showPlayer(Player player)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean canSee(Player player)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isFlying()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setFlying(boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PlayerInventory getInventory()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean setWindowProperty(Property prprt, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public InventoryView getOpenInventory()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public InventoryView openInventory(Inventory invntr)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public InventoryView openWorkbench(Location lctn, boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public InventoryView openEnchanting(Location lctn, boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void openInventory(InventoryView iv)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void closeInventory()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ItemStack getItemInHand()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setItemInHand(ItemStack is)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ItemStack getItemOnCursor()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setItemOnCursor(ItemStack is)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isSleeping()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getSleepTicks()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public GameMode getGameMode()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setGameMode(GameMode gm)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isBlocking()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getHealth()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setHealth(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getMaxHealth()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public double getEyeHeight()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public double getEyeHeight(boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Location getEyeLocation()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Block> getLineOfSight(HashSet<Byte> hs, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Block getTargetBlock(HashSet<Byte> hs, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> hs, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Egg throwEgg()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Snowball throwSnowball()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Arrow shootArrow()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> type)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getRemainingAir()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setRemainingAir(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getMaximumAir()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setMaximumAir(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void damage(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void damage(int i, Entity entity)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getMaximumNoDamageTicks()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setMaximumNoDamageTicks(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getLastDamage()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setLastDamage(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getNoDamageTicks()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setNoDamageTicks(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Player getKiller()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean addPotionEffect(PotionEffect pe)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean addPotionEffect(PotionEffect pe, boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean addPotionEffects(Collection<PotionEffect> clctn)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean hasPotionEffect(PotionEffectType pet)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void removePotionEffect(PotionEffectType pet)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Collection<PotionEffect> getActivePotionEffects()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Location getLocation()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setVelocity(Vector vector)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Vector getVelocity()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public World getWorld()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean teleport(Location lctn)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean teleport(Location lctn, TeleportCause tc)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean teleport(Entity entity)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean teleport(Entity entity, TeleportCause tc)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Entity> getNearbyEntities(double d, double d1, double d2)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getEntityId()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getFireTicks()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getMaxFireTicks()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setFireTicks(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isDead()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Server getServer()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Entity getPassenger()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean setPassenger(Entity entity)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isEmpty()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean eject()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public float getFallDistance()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setFallDistance(float f)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setLastDamageCause(EntityDamageEvent ede)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public EntityDamageEvent getLastDamageCause()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public UUID getUniqueId()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getTicksLived()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setTicksLived(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void playEffect(EntityEffect ee)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public EntityType getType()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isInsideVehicle()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean leaveVehicle()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Entity getVehicle()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setMetadata(String string, MetadataValue mv)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<MetadataValue> getMetadata(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean hasMetadata(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void removeMetadata(String string, Plugin plugin)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isPermissionSet(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isPermissionSet(Permission prmsn)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean hasPermission(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String string, boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String string, boolean bln, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void removeAttachment(PermissionAttachment pa)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void recalculatePermissions()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isOp()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setOp(boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isConversing()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void acceptConversationInput(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean beginConversation(Conversation c)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void abandonConversation(Conversation c)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void abandonConversation(Conversation c, ConversationAbandonedEvent cae)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void sendMessage(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void sendMessage(String[] strings)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isOnline()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isBanned()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isWhitelisted()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setWhitelisted(boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Player getPlayer()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long getFirstPlayed()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long getLastPlayed()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean hasPlayedBefore()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Map<String, Object> serialize()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void sendPluginMessage(Plugin plugin, String string, byte[] bytes)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Set<String> getListeningPluginChannels()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setName(String name)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
	//end//
}
