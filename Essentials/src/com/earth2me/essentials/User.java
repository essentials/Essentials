package com.earth2me.essentials;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.commands.IEssentialsCommand;
import com.earth2me.essentials.register.payment.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class User extends UserData implements Comparable<User>, IReplyTo, IUser
{
	private CommandSender replyTo = null;
	private transient User teleportRequester;
	private transient boolean teleportRequestHere;
	private transient final Teleport teleport;
	private transient long teleportRequestTime;
	private transient long lastOnlineActivity;
	private transient long lastActivity = System.currentTimeMillis();
	private boolean hidden = false;
	private transient Location afkPosition;
	private static final Logger logger = Logger.getLogger("Minecraft");
	
	User(final Player base, final IEssentials ess)
	{
		super(base, ess);
		teleport = new Teleport(this, ess);
		afkPosition = getLocation();
	}
	
	User update(final Player base)
	{
		setBase(base);
		return this;
	}
	
	@Override
	public boolean isAuthorized(final IEssentialsCommand cmd)
	{
		return isAuthorized(cmd, "essentials.");
	}
	
	@Override
	public boolean isAuthorized(final IEssentialsCommand cmd, final String permissionPrefix)
	{
		return isAuthorized(permissionPrefix + (cmd.getName().equals("r") ? "msg" : cmd.getName()));
	}
	
	@Override
	public boolean isAuthorized(final String node)
	{
		if (base instanceof OfflinePlayer)
		{
			return false;
		}
		
		if (isOp())
		{
			return true;
		}
		
		if (isJailed())
		{
			return false;
		}
		
		return ess.getPermissionsHandler().hasPermission(base, node);
	}
	
	public void healCooldown() throws Exception
	{
		final Calendar now = new GregorianCalendar();
		if (getLastHealTimestamp() > 0)
		{
			final double cooldown = ess.getSettings().getHealCooldown();
			final Calendar cooldownTime = new GregorianCalendar();
			cooldownTime.setTimeInMillis(getLastHealTimestamp());
			cooldownTime.add(Calendar.SECOND, (int)cooldown);
			cooldownTime.add(Calendar.MILLISECOND, (int)((cooldown * 1000.0) % 1000.0));
			if (cooldownTime.after(now) && !isAuthorized("essentials.heal.cooldown.bypass"))
			{
				throw new Exception(_("timeBeforeHeal", Util.formatDateDiff(cooldownTime.getTimeInMillis())));
			}
		}
		setLastHealTimestamp(now.getTimeInMillis());
	}
	
	@Override
	public void giveMoney(final double value)
	{
		giveMoney(value, null);
	}
	
	public void giveMoney(final double value, final CommandSender initiator)
	{
		if (value == 0)
		{
			return;
		}
		setMoney(getMoney() + value);
		sendMessage(_("addedToAccount", Util.formatCurrency(value, ess)));
		if (initiator != null)
		{
			initiator.sendMessage(_("addedToOthersAccount", Util.formatCurrency(value, ess), this.getDisplayName()));
		}
	}
	
	public void payUser(final User reciever, final double value) throws Exception
	{
		if (value == 0)
		{
			return;
		}
		if (canAfford(value))
		{
			setMoney(getMoney() - value);
			reciever.setMoney(reciever.getMoney() + value);
			sendMessage(_("moneySentTo", Util.formatCurrency(value, ess), reciever.getDisplayName()));
			reciever.sendMessage(_("moneyRecievedFrom", Util.formatCurrency(value, ess), getDisplayName()));
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
	
	public void takeMoney(final double value, final CommandSender initiator)
	{
		if (value == 0)
		{
			return;
		}
		setMoney(getMoney() - value);
		sendMessage(_("takenFromAccount", Util.formatCurrency(value, ess)));
		if (initiator != null)
		{
			initiator.sendMessage(_("takenFromOthersAccount", Util.formatCurrency(value, ess), this.getDisplayName()));
		}
	}
	
	public boolean canAfford(final double cost)
	{
		final double mon = getMoney();
		return mon >= cost || isAuthorized("essentials.eco.loan");
	}
	
	public void dispose()
	{
		this.base = new OfflinePlayer(getName(), ess);
	}
	
	@Override
	public void setReplyTo(final CommandSender user)
	{
		replyTo = user;
	}
	
	@Override
	public CommandSender getReplyTo()
	{
		return replyTo;
	}
	
	@Override
	public int compareTo(final User other)
	{
		return Util.stripColor(this.getDisplayName()).compareToIgnoreCase(Util.stripColor(other.getDisplayName()));
	}
	
	@Override
	public boolean equals(final Object object)
	{
		if (!(object instanceof User))
		{
			return false;
		}
		return this.getName().equalsIgnoreCase(((User)object).getName());
		
	}
	
	@Override
	public int hashCode()
	{
		return this.getName().hashCode();
	}
	
	public Boolean canSpawnItem(final int itemId)
	{
		return !ess.getSettings().itemSpawnBlacklist().contains(itemId);
	}
	
	public Location getHome() throws Exception
	{
		return getHome(getHomes().get(0));
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
		setLastLocation(getLocation());
	}
	
	public void requestTeleport(final User player, final boolean here)
	{
		teleportRequestTime = System.currentTimeMillis();
		teleportRequester = player;
		teleportRequestHere = here;
	}
	
	public User getTeleportRequest()
	{
		return teleportRequester;
	}
	
	public boolean isTeleportRequestHere()
	{
		return teleportRequestHere;
	}
	
	public String getNick(boolean addprefixsuffix)
	{
		final StringBuilder nickname = new StringBuilder();
		final String nick = getNickname();
		if (ess.getSettings().isCommandDisabled("nick") || nick == null || nick.isEmpty() || nick.equals(getName()))
		{
			nickname.append(getName());
		}
		else
		{
			nickname.append(ess.getSettings().getNicknamePrefix()).append(nick);
		}
		if (isOp())
		{
			try
			{
				nickname.insert(0, ess.getSettings().getOperatorColor().toString());
				nickname.append("§f");
			}
			catch (Exception e)
			{
			}
		}
		
		if (addprefixsuffix && ess.getSettings().addPrefixSuffix())
		{
			if (!ess.getSettings().disablePrefix())
			{
				final String prefix = ess.getPermissionsHandler().getPrefix(base).replace('&', '§').replace("{WORLDNAME}", this.getWorld().getName());
				nickname.insert(0, prefix);
			}
			if (!ess.getSettings().disableSuffix())
			{
				final String suffix = ess.getPermissionsHandler().getSuffix(base).replace('&', '§').replace("{WORLDNAME}", this.getWorld().getName());
				nickname.append(suffix);
				if (suffix.length() < 2 || !suffix.substring(suffix.length() - 2, suffix.length() - 1).equals("§"))
				{
					nickname.append("§f");
				}
			}
			else
			{
				nickname.append("§f");
			}
		}
		
		return nickname.toString();
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
			name = name.substring(0, name.charAt(15) == '§' ? 15 : 16);
		}
		try
		{
			setPlayerListName(name);
		}
		catch (IllegalArgumentException e)
		{
			logger.log(Level.INFO, "Playerlist for " + name + " was not updated. Use a shorter displayname prefix.");
		}
	}
	
	@Override
	public String getDisplayName()
	{
		if (!(base instanceof OfflinePlayer) && ess.getSettings().changeDisplayName())
		{
			setDisplayNick();
		}
		return super.getDisplayName() == null ? super.getName() : super.getDisplayName();
	}
	
	public Teleport getTeleport()
	{
		return teleport;
	}
	
	public long getLastOnlineActivity()
	{
		return lastOnlineActivity;
	}
	
	public void setLastOnlineActivity(final long timestamp)
	{
		lastOnlineActivity = timestamp;
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
	
	@Override
	public void setAfk(final boolean set)
	{
		this.setSleepingIgnored(this.isAuthorized("essentials.sleepingignored") ? true : set);
		if (set && !isAfk())
		{
			afkPosition = getLocation();
		}
		super.setAfk(set);
	}
	
	@Override
	public boolean toggleAfk()
	{
		final boolean now = super.toggleAfk();
		this.setSleepingIgnored(this.isAuthorized("essentials.sleepingignored") ? true : now);
		return now;
	}
	
	@Override
	public boolean isHidden()
	{
		return hidden;
	}
	
	public void setHidden(final boolean hidden)
	{
		this.hidden = hidden;
	}

	//Returns true if status expired during this check
	public boolean checkJailTimeout(final long currentTime)
	{
		if (getJailTimeout() > 0 && getJailTimeout() < currentTime && isJailed())
		{
			setJailTimeout(0);
			setJailed(false);
			sendMessage(_("haveBeenReleased"));
			setJail(null);
			try
			{
				getTeleport().back();
			}
			catch (Exception ex)
			{
			}
			return true;
		}
		return false;
	}

	//Returns true if status expired during this check
	public boolean checkMuteTimeout(final long currentTime)
	{
		if (getMuteTimeout() > 0 && getMuteTimeout() < currentTime && isMuted())
		{
			setMuteTimeout(0);
			sendMessage(_("canTalkAgain"));
			setMuted(false);
			return true;
		}
		return false;
	}

	//Returns true if status expired during this check
	public boolean checkBanTimeout(final long currentTime)
	{
		if (getBanTimeout() > 0 && getBanTimeout() < currentTime && isBanned())
		{
			setBanTimeout(0);
			setBanned(false);
			return true;
		}
		return false;
	}
	
	public void updateActivity(final boolean broadcast)
	{
		if (isAfk())
		{
			setAfk(false);
			if (broadcast && !isHidden())
			{
				ess.broadcastMessage(this, _("userIsNotAway", getDisplayName()));
			}
		}
		lastActivity = System.currentTimeMillis();
	}
	
	public void checkActivity()
	{
		final long autoafkkick = ess.getSettings().getAutoAfkKick();
		if (autoafkkick > 0 && lastActivity > 0 && (lastActivity + (autoafkkick * 1000)) < System.currentTimeMillis()
			&& !isHidden() && !isAuthorized("essentials.kick.exempt") && !isAuthorized("essentials.afk.kickexempt"))
		{
			final String kickReason = _("autoAfkKickReason", autoafkkick / 60.0);
			lastActivity = 0;
			kickPlayer(kickReason);
			
			
			for (Player player : ess.getServer().getOnlinePlayers())
			{
				final User user = ess.getUser(player);
				if (user.isAuthorized("essentials.kick.notify"))
				{
					player.sendMessage(_("playerKicked", Console.NAME, getName(), kickReason));
				}
			}
		}
		final long autoafk = ess.getSettings().getAutoAfk();
		if (!isAfk() && autoafk > 0 && lastActivity + autoafk * 1000 < System.currentTimeMillis() && isAuthorized("essentials.afk"))
		{
			setAfk(true);
			if (!isHidden())
			{
				ess.broadcastMessage(this, _("userIsAway", getDisplayName()));
			}
		}
	}
	
	public Location getAfkPosition()
	{
		return afkPosition;
	}
	
	@Override
	public boolean toggleGodModeEnabled()
	{
		if (!isGodModeEnabled())
		{
			setFoodLevel(20);
		}
		return super.toggleGodModeEnabled();
	}
	
	@Override
	public boolean isGodModeEnabled()
	{
		return (super.isGodModeEnabled() && !ess.getSettings().getNoGodWorlds().contains(getLocation().getWorld().getName()))
			   || (isAfk() && ess.getSettings().getFreezeAfkPlayers());
	}
	
	public boolean isGodModeEnabledRaw()
	{
		return super.isGodModeEnabled();
	}
	
	public String getGroup()
	{
		return ess.getPermissionsHandler().getGroup(base);
	}
	
	public boolean inGroup(final String group)
	{
		return ess.getPermissionsHandler().inGroup(base, group);
	}
	
	public boolean canBuild()
	{
		return ess.getPermissionsHandler().canBuild(base, getGroup());
	}
	
	public long getTeleportRequestTime()
	{
		return teleportRequestTime;
	}	
}
