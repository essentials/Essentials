package net.ess3;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Logger;
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
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Teleport implements Runnable, ITeleport
{
	private static final double MOVE_CONSTANT = 0.3;


	private static class Target
	{
		private final Location location;
		private final Entity entity;

		public Target(Location location)
		{
			this.location = location;
			this.entity = null;
		}

		public Target(Entity entity)
		{
			this.entity = entity;
			this.location = null;
		}

		public Location getLocation()
		{
			if (this.entity != null)
			{
				return this.entity.getLocation();
			}
			return location;
		}
	}
	private IUser user;
	private int teleTimer = -1;
	private long started;	// time this task was initiated
	private long delay;		// how long to delay the teleport
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
	private static final Logger logger = Logger.getLogger("Minecraft");
	private TeleportCause cause;

	private void initTimer(long delay, Target target, Trade chargeFor, TeleportCause cause)
	{
		this.started = System.currentTimeMillis();
		this.delay = delay;
		this.health = user.getPlayer().getHealth();
		this.initX = Math.round(user.getPlayer().getLocation().getX() * MOVE_CONSTANT);
		this.initY = Math.round(user.getPlayer().getLocation().getY() * MOVE_CONSTANT);
		this.initZ = Math.round(user.getPlayer().getLocation().getZ() * MOVE_CONSTANT);
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
		if (Math.round(user.getPlayer().getLocation().getX() * MOVE_CONSTANT) != initX
			|| Math.round(user.getPlayer().getLocation().getY() * MOVE_CONSTANT) != initY
			|| Math.round(user.getPlayer().getLocation().getZ() * MOVE_CONSTANT) != initZ
			|| user.getPlayer().getHealth() < health)
		{	// user moved, cancel teleport
			cancel(true);
			return;
		}

		health = user.getPlayer().getHealth();  // in case user healed, then later gets injured

		long now = System.currentTimeMillis();
		if (now > started + delay)
		{
			try
			{
				cooldown(false);
				user.sendMessage(_("teleportationCommencing"));
				try
				{

					now(teleportTarget, cause);
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
			}
		}
	}

	public Teleport(IUser user, IEssentials ess)
	{
		this.user = user;
		this.ess = ess;
	}

	@Override
	public void respawn(final Trade chargeFor, TeleportCause cause) throws Exception
	{
		final Location bed = user.getBedSpawnLocation();
		final Location respawnLoc = ess.getPlugin().callRespawnEvent(user.getPlayer(), bed == null ? user.getPlayer().getWorld().getSpawnLocation() : bed, bed != null);
		teleport(new Target(respawnLoc), chargeFor, cause);
	}

	@Override
	public void warp(String warp, Trade chargeFor, TeleportCause cause) throws Exception
	{
		final Location loc = ess.getWarps().getWarp(warp);
		teleport(new Target(loc), chargeFor, cause);
		user.sendMessage(_("warpingTo", warp));
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

	public void teleport(Location loc, Trade chargeFor) throws Exception
	{
		teleport(new Target(loc), chargeFor, TeleportCause.PLUGIN);
	}

	@Override
	public void teleport(Location loc, Trade chargeFor, TeleportCause cause) throws Exception
	{
		teleport(new Target(loc), chargeFor, cause);
	}

	public void teleport(Entity entity, Trade chargeFor, TeleportCause cause) throws Exception
	{
		teleport(new Target(entity), chargeFor, cause);
	}

	private void teleport(Target target, Trade chargeFor, TeleportCause cause) throws Exception
	{
		double tDelay = ess.getRanks().getTeleportDelay(user);

		if (chargeFor != null)
		{
			chargeFor.isAffordableFor(user);
		}
		cooldown(true);
		if (tDelay <= 0 || Permissions.TELEPORT_TIMER_BYPASS.isAuthorized(user))
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
		Calendar c = new GregorianCalendar();
		c.add(Calendar.SECOND, (int)tDelay);
		c.add(Calendar.MILLISECOND, (int)((tDelay * 1000.0) % 1000.0));
		user.sendMessage(_("dontMoveMessage", DateUtil.formatDateDiff(c.getTimeInMillis())));
		initTimer((long)(tDelay * 1000.0), target, chargeFor, cause);

		teleTimer = ess.getPlugin().scheduleSyncRepeatingTask(this, 10, 10);
	}

	private void now(Target target, TeleportCause cause) throws Exception
	{
		cancel();
		user.setLastLocation();
		user.getPlayer().teleport(LocationUtil.getSafeDestination(target.getLocation()), cause);
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

	public void now(Location loc, Trade chargeFor, TeleportCause cause) throws Exception
	{
		cooldown(false);
		chargeFor.charge(user);
		now(new Target(loc), cause);
	}

	public void now(Entity entity, boolean cooldown, TeleportCause cause) throws Exception
	{
		if (cooldown)
		{
			cooldown(false);
		}
		now(new Target(entity), cause);
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
