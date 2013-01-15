package net.ess3;

import java.util.Calendar;
import java.util.GregorianCalendar;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.ITeleport;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import net.ess3.user.CooldownException;
import net.ess3.user.UserData.TimestampType;
import net.ess3.utils.DateUtil;
import net.ess3.utils.LocationUtil;
import net.ess3.utils.Target;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Teleport implements Runnable, ITeleport
{
	private static final double MOVE_CONSTANT = 0.3;
	private IUser user;
	private IUser teleportUser;
	private int teleTimer = -1;
	private long started;    // time this task was initiated
	private long tpDelay;        // how long to delay the teleport
	private int health;
	// note that I initially stored a clone of the location for reference, but...
	// when comparing locations, I got incorrect mismatches (rounding errors, looked like)
	// so, the X/Y/Z values are stored instead and rounded off
	private long initX;
	private long initY;
	private long initZ;
	private Target teleportTarget;
	private Trade chargeFor;
	private final IEssentials ess;
	private TeleportCause cause;

	private void initTimer(long delay, Target target, Trade chargeFor, TeleportCause cause)
	{
		initTimer(delay, user, target, chargeFor, cause);
	}

	private void initTimer(long delay, IUser teleportUser, Target target, Trade chargeFor, TeleportCause cause)
	{
		this.started = System.currentTimeMillis();
		this.tpDelay = delay;
		this.health = teleportUser.getPlayer().getHealth();
		this.initX = Math.round(teleportUser.getPlayer().getLocation().getX() * MOVE_CONSTANT);
		this.initY = Math.round(teleportUser.getPlayer().getLocation().getY() * MOVE_CONSTANT);
		this.initZ = Math.round(teleportUser.getPlayer().getLocation().getZ() * MOVE_CONSTANT);
		this.teleportUser = teleportUser;
		this.teleportTarget = target;
		this.chargeFor = chargeFor;
		this.cause = cause;
	}

	@Override
	public void run()
	{

		if (user == null || !user.isOnline() || user.getPlayer().getLocation() == null)
		{
			cancel();
			return;
		}

		if (teleportUser == null || !teleportUser.isOnline() || teleportUser.getPlayer().getLocation() == null)
		{
			cancel(false);
			return;
		}

		if (!Permissions.TELEPORT_TIMER_MOVE.isAuthorized(user) && (Math.round(
																	teleportUser.getPlayer().getLocation().getX() * MOVE_CONSTANT) != initX || Math.round(
																	teleportUser.getPlayer().getLocation().getY() * MOVE_CONSTANT) != initY || Math.round(
																	teleportUser.getPlayer().getLocation().getZ() * MOVE_CONSTANT) != initZ || teleportUser.getPlayer().getHealth() < health))
		{    // user moved, cancel teleport
			cancel(true);
			return;
		}

		health = teleportUser.getPlayer().getHealth();  // in case user healed, then later gets injured

		final long now = System.currentTimeMillis();
		if (now > started + tpDelay)
		{
			try
			{
				cooldown(false);
				teleportUser.sendMessage(_("teleportationCommencing"));
				try
				{

					teleportUser.getTeleport().now(teleportTarget, cause);
					if (chargeFor != null)
					{
						chargeFor.charge(user);
					}
				}
				catch (Throwable ex)
				{
					ess.getCommandHandler().showCommandError(user, "teleport", ex);
				}
			}
			catch (Exception ex)
			{
				user.sendMessage(_("cooldownWithMessage", ex.getMessage()));
				if (user != teleportUser)
				{
					teleportUser.sendMessage(_("cooldownWithMessage", ex.getMessage()));
				}
			}
		}
	}

	public Teleport(IUser user, IEssentials ess)
	{
		this.user = user;
		this.ess = ess;
	}

	public void cooldown(boolean check) throws Exception
	{
		try
		{
			user.checkCooldown(TimestampType.LASTTELEPORT, ess.getRanks().getTeleportCooldown(user), !check, Permissions.TELEPORT_COOLDOWN_BYPASS);
		}
		catch (CooldownException ex)
		{
			throw new Exception(_("timeBeforeTeleport", ex.getMessage()));

		}
	}

	//If we need to cancel a pending teleport call this method
	public void cancel(boolean notifyUser)
	{
		if (teleTimer == -1)
		{
			return;
		}
		try
		{
			ess.getPlugin().cancelTask(teleTimer);
			if (notifyUser)
			{
				user.sendMessage(_("pendingTeleportCancelled"));
				if (teleportUser != user)
				{
					teleportUser.sendMessage(_("pendingTeleportCancelled"));
				}
			}
		}
		finally
		{
			teleTimer = -1;
		}
	}

	public void cancel()
	{
		cancel(false);
	}

	@Override
	public void teleport(Location loc, Trade chargeFor, TeleportCause cause) throws Exception
	{
		teleport(new Target(loc), chargeFor, cause);
	}

	@Override
	public void teleport(Entity entity, Trade chargeFor, TeleportCause cause) throws Exception
	{
		teleport(new Target(entity), chargeFor, cause);
	}

	private void teleport(Target target, Trade chargeFor, TeleportCause cause) throws Exception
	{
		final double delay = ess.getRanks().getTeleportDelay(user);

		if (chargeFor != null)
		{
			chargeFor.isAffordableFor(user);
		}
		cooldown(true);
		if (delay <= 0 || Permissions.TELEPORT_TIMER_BYPASS.isAuthorized(user))
		{
			cooldown(false);
			now(target, cause);
			if (chargeFor != null)
			{
				chargeFor.charge(user);
			}
			return;
		}

		cancel();
		warnUser(user, delay);
		initTimer((long)(delay * 1000.0), target, chargeFor, cause);

		teleTimer = ess.getPlugin().scheduleSyncRepeatingTask(this, 10, 10);
	}

	@Override
	public void now(final Target target, final TeleportCause cause) throws Exception
	{
		cancel();
		user.setLastLocation();
		final Location loc = LocationUtil.getSafeDestination(target.getLocation());
		ess.getPlugin().scheduleSyncDelayedTask(
				new Runnable()
				{
					@Override
					public void run()
					{
						user.getPlayer().teleport(loc, cause);
					}
				});
	}

	@Override
	public void now(Location loc, boolean cooldown, TeleportCause cause) throws Exception
	{
		if (cooldown)
		{
			cooldown(false);
		}
		now(new Target(loc), cause);
	}

	@Override
	//The now function is used when you want to skip tp delay when teleporting someone to a location or player.
	public void now(Entity entity, boolean cooldown, TeleportCause cause) throws Exception
	{
		if (cooldown)
		{
			cooldown(false);
		}
		now(new Target(entity), cause);
	}

	public void now(Location loc, Trade chargeFor, TeleportCause cause) throws Exception
	{
		cooldown(false);
		chargeFor.charge(user);
		now(new Target(loc), cause);
	}

	//The teleportToMe function is a wrapper used to handle teleporting players to them, like /tphere
	@Override
	public void teleportToMe(IUser otherUser, Trade chargeFor, TeleportCause cause) throws Exception
	{
		final Target target = new Target(user.getPlayer());

		double delay = ess.getRanks().getTeleportDelay(user);

		if (chargeFor != null)
		{
			chargeFor.isAffordableFor(user);
		}
		cooldown(true);
		if (delay <= 0 || Permissions.TELEPORT_TIMER_BYPASS.isAuthorized(user))
		{
			cooldown(false);
			otherUser.getTeleport().now(target, cause);
			if (chargeFor != null)
			{
				chargeFor.charge(user);
			}
			return;
		}

		cancel(false);
		warnUser(otherUser, delay);
		initTimer((long)(delay * 1000.0), otherUser, target, chargeFor, cause);

		teleTimer = ess.getPlugin().scheduleSyncRepeatingTask(this, 10, 10);
	}

	private void warnUser(final IUser user, final double delay)
	{
		final Calendar c = new GregorianCalendar();
		c.add(Calendar.SECOND, (int)delay);
		c.add(Calendar.MILLISECOND, (int)((delay * 1000.0) % 1000.0));
		user.sendMessage(_("dontMoveMessage", DateUtil.formatDateDiff(c.getTimeInMillis())));
	}

	@Override
	public void respawn(final Trade chargeFor, TeleportCause cause) throws Exception
	{
		final Location bed = user.getBedSpawnLocation();
		final Location respawnLoc = ess.getPlugin().callRespawnEvent(
				user.getPlayer(), bed == null ? user.getPlayer().getWorld().getSpawnLocation() : bed, bed != null);
		teleport(new Target(respawnLoc), chargeFor, cause);
	}

	@Override
	public void warp(String warp, Trade chargeFor, TeleportCause cause) throws Exception
	{
		final Location loc = ess.getWarps().getWarp(warp);
		user.sendMessage(_("warpingTo", warp));
		teleport(new Target(loc), chargeFor, cause);
	}

	@Override
	public void back(Trade chargeFor) throws Exception
	{
		teleport(new Target(user.getData().getLastLocation().getStoredLocation()), chargeFor, TeleportCause.COMMAND);
	}

	@Override
	public void back() throws Exception
	{
		now(new Target(user.getData().getLastLocation().getStoredLocation()), TeleportCause.COMMAND);
	}

	@Override
	public void home(Location loc, Trade chargeFor) throws Exception
	{
		teleport(new Target(loc), chargeFor, TeleportCause.COMMAND);
	}
}
